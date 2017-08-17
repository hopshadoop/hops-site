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
import io.hops.site.dto.AddressJSON;
import io.hops.site.dto.DatasetDTO;
import io.hops.site.dto.DatasetIssueDTO;
import io.hops.site.dto.ElasticDatasetDTO;
import io.hops.site.dto.PublishDatasetDTO;
import io.hops.site.dto.SearchDTO;
import java.util.Collection;
import java.util.Date;
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

  private final static Logger LOGGER = Logger.getLogger(DatasetController.class.getName());

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

  public SearchDTO.BaseResult search(SearchDTO.Params searchParams) throws AppException {
    String sessionId = settings.getSessionId();
    QueryBuilder qb = DatasetElasticHelper.getNameDescriptionMetadataQuery(searchParams.getSearchTerm());
    SearchHits elasticResult = elasticCtrl.search(settings.DELA_DOC_INDEX, new String[]{settings.DELA_DOC_TYPE}, qb);
    SearchSession session = SearchSession.create(searchParams, elasticResult);
    sessionCtrl.put(sessionId, session);
    return new SearchDTO.BaseResult(sessionId, session.cachedItems.size());
  }

  public SearchDTO.Page getSearchPage(String sessionId, int startItem, int nrItems) throws AppException {
    Optional<SearchSession> session;
    try {
      session = (Optional) sessionCtrl.get(sessionId);
    } catch (ExecutionException ex) {
      throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "cache issue");
    }
    if (!session.isPresent()) {
      throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), "session expired");
    }

    List<SearchDTO.Item> elem = new LinkedList<>();
    for (CachedItem e : session.get().getElem(startItem, nrItems)) {
      if (!e.hasPeers()) {
        e.setPeers(liveDatasetFacade.datasetPeers(e.datasetId, settings.LIVE_DATASET_BOOTSTRAP_PEERS));
      }
      if (e.complete()) {
        elem.add(e.build());
      }
    }
    return new SearchDTO.Page(startItem, elem);
  }

  public String publishDataset(PublishDatasetDTO.Request req) throws AppException {
    Optional<RegisteredCluster> cluster = clusterFacade.findByPublicId(req.getClusterId());
    if (!cluster.isPresent()) {
      throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), "cluster not registered");
    }
    LOGGER.log(Level.FINE, "cluster:<{0},{1}>", new Object[]{cluster.get().getId(), cluster.get().getPublicId()});
    String datasetPublicId = settings.getDatasetPublicId();
    Date publishedOn = settings.getDateNow();
    Collection<Category> categories = categoryFacade.getAndStoreCategories(req.getCategoryCollection());
    //TODO Readme
    String readmePath = "";
    Dataset dataset = datasetFacade.createDataset(datasetPublicId, req.getName(), req.getDescription(), publishedOn,
      readmePath, categories, cluster.get());
    LOGGER.log(Level.FINE, "cluster:<{0},{1}> dataset:<{2},{3}>", 
      new Object[]{cluster.get().getId(), cluster.get().getPublicId(), dataset.getId(), dataset.getPublicId()});
    liveDatasetFacade
      .create(new LiveDataset(dataset.getId(), cluster.get().getId(), settings.LIVE_DATASET_UPLOAD_STATUS));
    elasticCtrl.
      add(settings.DELA_DOC_INDEX, settings.DELA_DOC_TYPE, datasetPublicId, ElasticDatasetDTO.from(req).json());
    return datasetPublicId;
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
    if (dataset.getOwnerCluster().getId() != cluster.get().getId()) {
      throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), "only owner can unpublish");
    }
    datasetFacade.remove(dataset);
    elasticCtrl.delete(settings.DELA_DOC_INDEX, settings.DELA_DOC_TYPE, datasetPublicId);
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
    liveDatasetFacade.create(new LiveDataset(dataset.get().getId(), cluster.get().getId(),
      settings.LIVE_DATASET_DOWNLOAD_STATUS));
  }

  public void remove(String datasetPublicId, String clusterPublicId) throws AppException {
    Optional<LiveDataset> c = liveDatasetFacade.connection(datasetPublicId, clusterPublicId);
    if (c.isPresent()) {
      liveDatasetFacade.remove(c.get());
    }
  }

  public void downloadComplete(String datasetPublicId, String clusterPublicId) throws AppException {
    Optional<LiveDataset> c = liveDatasetFacade.connection(datasetPublicId, clusterPublicId);
    if (c.isPresent()) {
      LiveDataset connection = c.get();
      connection.setStatus(settings.LIVE_DATASET_UPLOAD_STATUS);
      liveDatasetFacade.edit(connection);
    }
  }

  public static class SearchSession implements Session {

    public final SearchDTO.Params searchParams;
    public final List<CachedItem> cachedItems;

    private SearchSession(SearchDTO.Params searchParams, List<CachedItem> cachedItems) {
      this.searchParams = searchParams;
      this.cachedItems = cachedItems;
    }

    public static SearchSession create(SearchDTO.Params searchParams, SearchHits searchHits) {
      List<CachedItem> cachedHits = new LinkedList<>();
      for (SearchHit hit : searchHits) {
        String datasetId = hit.getId();
        DatasetDTO dataset = DatasetDTO.parse(hit.getSourceAsString());
        cachedHits.add(new CachedItem(datasetId, dataset, hit.getScore()));
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

    public final String datasetId;
    private final DatasetDTO dataset;
    private final float score;
    private Optional<List<AddressJSON>> peers;

    public CachedItem(String datasetId, DatasetDTO dataset, float score) {
      this.datasetId = datasetId;
      this.dataset = dataset;
      this.score = score;
      this.peers = Optional.empty();
    }

    public boolean hasPeers() {
      return peers.isPresent();
    }

    public void setPeers(List<AddressJSON> peers) {
      this.peers = Optional.of(peers);
    }

    public boolean complete() {
      return peers.isPresent();
    }

    public SearchDTO.Item build() {
      return new SearchDTO.Item(datasetId, dataset, score, peers.get());
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
        .should(QueryBuilders.prefixQuery(Settings.DELA_DOC_NAME_FIELD, searchTerm))
        .should(QueryBuilders.matchPhraseQuery(Settings.DELA_DOC_NAME_FIELD, searchTerm))
        .should(QueryBuilders.fuzzyQuery(Settings.DELA_DOC_NAME_FIELD, searchTerm))
        .should(QueryBuilders.wildcardQuery(Settings.DELA_DOC_NAME_FIELD, String.format("*%s*", searchTerm)));
    }

    public static QueryBuilder getDescriptionQuery(String searchTerm) {
      return QueryBuilders.boolQuery()
        .should(QueryBuilders.prefixQuery(Settings.DELA_DOC_DESCRIPTION_FIELD, searchTerm))
        .should(QueryBuilders.termsQuery(Settings.DELA_DOC_DESCRIPTION_FIELD, searchTerm))
        .should(QueryBuilders.matchPhraseQuery(Settings.DELA_DOC_DESCRIPTION_FIELD, searchTerm))
        .should(QueryBuilders.fuzzyQuery(Settings.DELA_DOC_DESCRIPTION_FIELD, searchTerm))
        .should(QueryBuilders.wildcardQuery(Settings.DELA_DOC_DESCRIPTION_FIELD, String.format("*%s*", searchTerm)));
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
    LOGGER.log(Level.INFO, "Adding new dataset issue for dataset with public id: {0}.", datasetPublicId);
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
