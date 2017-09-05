/*
 * Copyright 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hops.site.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.hops.site.common.AppException;
import io.hops.site.common.Settings;
import io.hops.site.controller.HopsSiteController.Session;
import io.hops.site.dao.entity.Category;
import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.entity.DatasetIssue;
import io.hops.site.dao.entity.LiveDataset;
import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dao.entity.Users;
import io.hops.site.dao.facade.CategoryFacade;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dao.facade.DatasetIssueFacade;
import io.hops.site.dao.facade.LiveDatasetFacade;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import io.hops.site.dao.facade.UsersFacade;
import io.hops.site.dto.ClusterAddressDTO;
import io.hops.site.dto.DatasetDTO;
import io.hops.site.dto.SearchServiceDTO;
import io.hops.site.dto.internal.ElasticDoc;
import io.hops.site.old_dto.DatasetIssueDTO;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.core.Response;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class DatasetController {

  private final static Logger LOG = Logger.getLogger(DatasetController.class.getName());

  @EJB
  private Settings settings;
  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private DatasetIssueFacade datasetIssueFacade;
  @EJB
  private UsersFacade userFacade;
  @EJB
  private RegisteredClusterFacade clusterFacade;
  @EJB
  private HelperFunctions helperFunctions;
  @EJB
  private ElasticController elasticCtrl;
  @EJB
  private HopsSiteController sessionCtrl;
  @EJB
  private LiveDatasetFacade liveDatasetFacade;
  @EJB
  private CategoryFacade categoryFacade;

  private final Random rand = new Random();
  private final ObjectMapper mapper = new ObjectMapper();

  //*****************************************************SEARCH*********************************************************
  public SearchServiceDTO.SearchResult search(SearchServiceDTO.Params searchParams) throws AppException {
    String sessionId = settings.getSessionId();
    QueryBuilder qb = DatasetElasticHelper.getNameDescriptionMetadataQuery(searchParams.getSearchTerm());
    SearchHits elasticResult = elasticCtrl.search(settings.DELA_DOC_INDEX,
      new String[]{ElasticDoc.DOC_TYPE}, qb);
    SearchSession session = SearchSession.create(searchParams, elasticResult);
    sessionCtrl.put(sessionId, session);
    return new SearchServiceDTO.SearchResult(sessionId, session.cachedItems.size());
  }

  public List<SearchServiceDTO.Item> getSearchPage(String sessionId, int startItem, int nrItems) throws AppException {
    Optional<SearchSession> session;
    try {
      session = (Optional) sessionCtrl.get(sessionId);
    } catch (ExecutionException ex) {
      throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "cache issue");
    }
    if (!session.isPresent()) {
      throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), "session expired");
    }

    List<SearchServiceDTO.Item> elem = new LinkedList<>();
    for (CachedItem e : session.get().getElem(startItem, nrItems)) {
      if (e.complete()) {
        elem.add(e.build());
      }
    }
    return elem;
  }

  public SearchServiceDTO.ItemDetails getDetails(String publicDSId) throws AppException {
    List<ClusterAddressDTO> bootstrap
      = liveDatasetFacade.datasetPeers(publicDSId, settings.LIVE_DATASET_BOOTSTRAP_PEERS);

    Optional<Dataset> dataset = datasetFacade.findByPublicId(publicDSId);
    if (!dataset.isPresent()) {
      throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "dataset issue");
    }

    long size = 1;
    DatasetDTO.Owner owner = new DatasetDTO.Owner(dataset.get().getOwner());
    DatasetDTO.Details details = new DatasetDTO.Details(owner, dataset.get().getCategories(), 
      dataset.get().getMadePublicOn(), size);
    return new SearchServiceDTO.ItemDetails(details, bootstrap);
  }

  //******************************************************DATASET*******************************************************
  public String publishDataset(String publicDSId, String publicCId, DatasetDTO.Proto msg) throws AppException {
    Optional<RegisteredCluster> cluster = clusterFacade.findByPublicId(publicCId);
    if (!cluster.isPresent()) {
      throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), "cluster not registered");
    }
    Optional<Users> user = userFacade.findByEmailAndPublicClusterId(msg.getUserEmail(), publicCId);
    if(!user.isPresent()) {
      throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 
        ThirdPartyException.Error.USER_NOT_REGISTERED.toString());
    }
    Collection<Category> categories = categoryFacade.getAndStoreCategories(msg.getCategories());
    //TODO Alex - Readme
    String readmePath = "";
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset:publish {0}",
      new Object[]{publicDSId});
    Optional<Dataset> dAux = datasetFacade.findByPublicId(publicDSId);
    if(dAux.isPresent())  {
      throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 
        ThirdPartyException.Error.DATASET_EXISTS.toString());
    }
    Dataset dataset = datasetFacade.createDataset(publicDSId, msg.getName(), msg.getDescription(),
      readmePath, categories, user.get(), msg.getSize());
    LOG.log(HopsSiteSettings.DELA_DEBUG, "dataset:{0} cluster:{1} live dataset",
      new Object[]{publicDSId, publicCId});
    liveDatasetFacade.uploadDataset(cluster.get().getId(), dataset.getId());
    ElasticDoc elasticDoc = elasticDoc(publicDSId, msg);
    elasticCtrl.add(settings.DELA_DOC_INDEX, ElasticDoc.DOC_TYPE, publicDSId, toJson(elasticDoc));
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset:publish - done {0}",
      new Object[]{publicDSId});
    return publicDSId;
  }

  private ElasticDoc elasticDoc(String publicDSId, DatasetDTO.Proto msg) {
    return new ElasticDoc(publicDSId, msg.getName(), msg.getDescription());
  }

  private String toJson(ElasticDoc doc) {
    return new Gson().toJson(doc);
  }

  public void unpublishDataset(String datasetPublicId, String clusterPublicId) throws AppException {
    Optional<RegisteredCluster> cluster = clusterFacade.findByPublicId(clusterPublicId);
    if (!cluster.isPresent()) {
      throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), "cluster not registered");
    }
    Optional<Dataset> d = datasetFacade.findByPublicId(datasetPublicId);
    if (!d.isPresent()) {
      return;
    }
    Dataset dataset = d.get();
    if (dataset.getOwner().getCluster().getId() != cluster.get().getId()) {
      throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), "only owner can unpublish");
    }
    datasetFacade.remove(dataset);
    elasticCtrl.delete(settings.DELA_DOC_INDEX, ElasticDoc.DOC_TYPE, datasetPublicId);
  }

  public void download(String datasetPublicId, String clusterPublicId) throws AppException {
    Optional<RegisteredCluster> cluster = clusterFacade.findByPublicId(clusterPublicId);
    if (!cluster.isPresent()) {
      throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), "cluster not registered");
    }
    Optional<Dataset> dataset = datasetFacade.findByPublicId(datasetPublicId);
    if (!dataset.isPresent()) {
      throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), "dataset not registered");
    }
    liveDatasetFacade.downloadDataset(cluster.get().getId(), dataset.get().getId());
  }

  public void remove(String datasetPublicId, String clusterPublicId) throws AppException {
    Optional<LiveDataset> c = liveDatasetFacade.connection(datasetPublicId, clusterPublicId);
    if (c.isPresent()) {
      liveDatasetFacade.remove(c.get());
    }
  }

  public void complete(String datasetPublicId, String clusterPublicId) throws AppException {
    Optional<LiveDataset> c = liveDatasetFacade.connection(datasetPublicId, clusterPublicId);
    if (c.isPresent()) {
      LiveDataset connection = c.get();
      connection.setStatus(settings.LIVE_DATASET_UPLOAD_STATUS);
      liveDatasetFacade.edit(connection);
    }
  }

  public static class SearchSession implements Session {

    public final SearchServiceDTO.Params searchParams;
    public final List<CachedItem> cachedItems;

    private SearchSession(SearchServiceDTO.Params searchParams, List<CachedItem> cachedItems) {
      this.searchParams = searchParams;
      this.cachedItems = cachedItems;
    }

    public static SearchSession create(SearchServiceDTO.Params searchParams, SearchHits searchHits) {
      List<CachedItem> cachedHits = new LinkedList<>();
      for (SearchHit hit : searchHits) {
        String datasetId = hit.getId();
        String name = (String) hit.getSource().get(ElasticDoc.NAME_FIELD);
        String description = (String) hit.getSource().get(ElasticDoc.DESCRIPTION_FIELD);
        cachedHits.add(new CachedItem(datasetId, name, description, hit.getScore()));
      }
      return new SearchSession(searchParams, cachedHits);
    }

    public List<CachedItem> getElem(int startElem, int nrElem) {
      List<CachedItem> result = new LinkedList<>();
      int page = 0;
      Iterator<CachedItem> it = cachedItems.iterator();
      while (it.hasNext() && page < startElem) {
        it.next();
        page++;
      }
      while (it.hasNext() && page < startElem + nrElem) {
        result.add(it.next());
        page++;
      }
      return result;
    }
  }

  public static class CachedItem {

    public final String publicDSId;
    private String name;
    private String description;
    private final float score;

    public CachedItem(String publicDSId, String name, String description, float score) {
      this.publicDSId = publicDSId;
      this.name = name;
      this.description = description;
      this.score = score;
    }

    public boolean complete() {
      return true;
    }

    public SearchServiceDTO.Item build() {
      DatasetDTO.Search dataset = new DatasetDTO.Search(name, description);
      return new SearchServiceDTO.Item(publicDSId, dataset, score);
    }
  }

  public static class DatasetElasticHelper {

    public static QueryBuilder getNameDescriptionMetadataQuery(String searchTerm) {
      return QueryBuilders.boolQuery()
        .should(getNameQuery(searchTerm))
        .should(getDescriptionQuery(searchTerm));
    }

    public static QueryBuilder getNameQuery(String searchTerm) {
      return QueryBuilders.boolQuery()
        .should(QueryBuilders.prefixQuery(ElasticDoc.NAME_FIELD, searchTerm))
        .should(QueryBuilders.matchPhraseQuery(ElasticDoc.NAME_FIELD, searchTerm))
        .should(QueryBuilders.fuzzyQuery(ElasticDoc.NAME_FIELD, searchTerm))
        .should(QueryBuilders.wildcardQuery(ElasticDoc.NAME_FIELD, String.format("*%s*", searchTerm)));
    }

    public static QueryBuilder getDescriptionQuery(String searchTerm) {
      return QueryBuilders.boolQuery()
        .should(QueryBuilders.prefixQuery(ElasticDoc.DESCRIPTION_FIELD, searchTerm))
        .should(QueryBuilders.termsQuery(ElasticDoc.DESCRIPTION_FIELD, searchTerm))
        .should(QueryBuilders.matchPhraseQuery(ElasticDoc.DESCRIPTION_FIELD, searchTerm))
        .should(QueryBuilders.fuzzyQuery(ElasticDoc.DESCRIPTION_FIELD, searchTerm))
        .should(QueryBuilders.wildcardQuery(ElasticDoc.DESCRIPTION_FIELD, String.format("*%s*", searchTerm)));
    }
  }

  /**
   * Report dataset issue
   *
   * @param datasetIssue
   */
  public void reportDatasetIssue(DatasetIssueDTO datasetIssue) {
    datasetIssueDTOSanityCheck(datasetIssue);
    String datasetPublicId = datasetIssue.getDatasetId();
    Optional<Dataset> dataset = datasetFacade.findByPublicId(datasetPublicId);
    if (!dataset.isPresent()) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    Optional<Users> user = userFacade.
      findByEmailAndPublicClusterId(datasetIssue.getUser().getEmail(), datasetPublicId);
    if (!user.isPresent()) {
      throw new IllegalArgumentException("User not found.");
    }

    DatasetIssue newDatasetIssue
      = new DatasetIssue(datasetIssue.getType(), datasetIssue.getMsg(), user.get(), dataset.get());
    datasetIssueFacade.create(newDatasetIssue);
    LOG.log(Level.INFO, "Adding new dataset issue for dataset with public id: {0}.", datasetPublicId);
  }

  private void datasetIssueDTOSanityCheck(DatasetIssueDTO datasetIssue) {
    if (datasetIssue == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (datasetIssue.getDatasetId() == null) {
      throw new IllegalArgumentException("Dataset id not assigned.");
    }
    if (datasetIssue.getUser().getEmail() == null) {
      throw new IllegalArgumentException("User not assigned.");
    }
    if (datasetIssue.getType() == null && datasetIssue.getType().isEmpty()) {
      throw new IllegalArgumentException("Issue type not assigned.");
    }
  }

  /**
   * Get all datasets
   *
   * @return
   */
  public List<Dataset> findAllDatasets() {
    return datasetFacade.findAll();
  }

  /**
   * Get dataset with the given id
   *
   * @param datasetId
   * @return
   */
  public Dataset findDataset(int datasetId) {
    return datasetFacade.find(datasetId);
  }

  /**
   * Get dataset with the given public id
   *
   * @param datasetPublicId
   * @return
   */
  public Optional<Dataset> findDatasetByPublicId(String datasetPublicId) {
    return datasetFacade.findByPublicId(datasetPublicId);
  }
}
