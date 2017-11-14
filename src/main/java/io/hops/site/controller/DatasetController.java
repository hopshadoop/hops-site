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
import io.hops.site.controller.HopsSiteController.Session;
import io.hops.site.dao.entity.Category;
import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.entity.DatasetHealth;
import io.hops.site.dao.entity.DatasetIssue;
import io.hops.site.dao.entity.LiveDataset;
import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dao.entity.Users;
import io.hops.site.dao.facade.CategoryFacade;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dao.facade.DatasetHealthFacade;
import io.hops.site.dao.facade.DatasetIssueFacade;
import io.hops.site.dao.facade.LiveDatasetFacade;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import io.hops.site.dao.facade.UsersFacade;
import io.hops.site.dto.ClusterAddressDTO;
import io.hops.site.dto.DatasetDTO;
import io.hops.site.dto.SearchServiceDTO;
import io.hops.site.dto.internal.ElasticDoc;
import io.hops.site.old_dto.DatasetIssueDTO;
import io.hops.site.rest.exception.AppException;
import io.hops.site.rest.exception.ThirdPartyException;
import io.hops.site.util.ClusterHelper;
import io.hops.site.util.DatasetHelper;
import io.hops.site.util.UserHelper;
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
  private HopsSiteSettings settings;
  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private DatasetHealthFacade datasetHealthFacade;
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

    DatasetDTO.Owner owner = new DatasetDTO.Owner(dataset.get().getOwner());
    DatasetDTO.Health datasetHealth = health(datasetHealthFacade.findByDatasetId(dataset.get().getId()));
    DatasetDTO.Details details = new DatasetDTO.Details(owner, dataset.get(), datasetHealth);
    return new SearchServiceDTO.ItemDetails(details, bootstrap);
  }

  private DatasetDTO.Health health(List<DatasetHealth> aux) {
    DatasetDTO.Health datasetHealth = new DatasetDTO.Health();
    for (DatasetHealth dh : aux) {
      if (HopsSiteSettings.DATASET_STATUS_UPLOAD == dh.getId().getStatus()) {
        datasetHealth.setSeeders(dh.getCount());
      } else if (HopsSiteSettings.DATASET_STATUS_DOWNLOAD == dh.getId().getStatus()) {
        datasetHealth.setLeechers(dh.getCount());
      }
    }
    return datasetHealth;
  }

  //******************************************************DATASET*******************************************************
  public String publishDataset(String publicCId, DatasetDTO.Proto msg) throws ThirdPartyException {
    RegisteredCluster cluster = ClusterHelper.getCluster(clusterFacade, publicCId);
    Users user = UserHelper.getUser(userFacade, publicCId, msg.getUserEmail());
    Collection<Category> categories = categoryFacade.getAndStoreCategories(msg.getCategories());
    //TODO Alex - Readme
    String readmePath = "";
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset:cluster {0} publishing",
      new Object[]{publicCId});
    List<Dataset> similar = datasetFacade.findSimilar(msg.getName());
    int version = getVersion(similar);
    String publicDSId = DatasetHelper.getPublicDatasetId(msg.getName(), version);
    Dataset dataset = datasetFacade.createDataset(publicDSId, msg.getName(), version,
      msg.getDescription(), readmePath, categories, user, msg.getSize());
    LOG.log(HopsSiteSettings.DELA_DEBUG, "dataset:{0} cluster:{1} live dataset",
      new Object[]{publicDSId, publicCId});
    liveDatasetFacade.uploadDataset(cluster.getId(), dataset.getId());
    ElasticDoc elasticDoc = elasticDoc(publicDSId, msg, version);
    try {
      elasticCtrl.add(settings.DELA_DOC_INDEX, ElasticDoc.DOC_TYPE, publicDSId, toJson(elasticDoc));
    } catch (AppException ex) {
      throw new ThirdPartyException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "elastic",
        ThirdPartyException.Source.LOCAL, "internal error");
    }
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset:publish - done {0}",
      new Object[]{publicDSId});
    return publicDSId;
  }

  private int getVersion(List<Dataset> datasets) {
    int version = 0;
    for (Dataset dataset : datasets) {
      if(version < dataset.getVersion()) {
         version = dataset.getVersion();
      }
    }
    return version+1;
  }

  private ElasticDoc elasticDoc(String publicDSId, DatasetDTO.Proto msg, int version) {
    return new ElasticDoc(publicDSId, msg.getName(), version, msg.getDescription());
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

  public void unpublishDataset(String datasetPublicId) throws AppException {
    Optional<Dataset> d = datasetFacade.findByPublicId(datasetPublicId);
    if (!d.isPresent()) {
      return;
    }
    Dataset dataset = d.get();
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
      connection.setStatus(settings.DATASET_STATUS_UPLOAD);
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
        int version = (Integer) hit.getSource().get(ElasticDoc.VERSION_FIELD);
        cachedHits.add(new CachedItem(datasetId, name, version, description, hit.getScore()));
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
    private int version;
    private String description;
    private final float score;

    public CachedItem(String publicDSId, String name, int version, String description, float score) {
      this.publicDSId = publicDSId;
      this.name = name;
      this.version = version;
      this.description = description;
      this.score = score;
    }

    public boolean complete() {
      return true;
    }

    public SearchServiceDTO.Item build() {
      DatasetDTO.Search dataset = new DatasetDTO.Search(name, version, description);
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
