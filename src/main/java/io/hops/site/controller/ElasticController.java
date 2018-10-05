package io.hops.site.controller;

import io.hops.site.common.Ip;
import io.hops.site.common.ResponseMessages;
import io.hops.site.dto.internal.ElasticDoc;
import io.hops.site.rest.exception.AppException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
@Stateless
public class ElasticController {

  private final static Logger LOG = Logger.getLogger(ElasticController.class.getName());
  @EJB
  private HopsSiteSettings settings;
  
  private Client elasticClient = null;
  
  @PostConstruct
  private void initClient() {
    try {
      getClient();
    } catch (AppException ex) {
      LOG.log(Level.SEVERE, null, ex);
    }
  }

  @PreDestroy
  private void closeClient(){
    shutdownClient();
  }
  
  private Client getClient() throws AppException {
    if (elasticClient == null) {
      final org.elasticsearch.common.settings.Settings settings
          = org.elasticsearch.common.settings.Settings.builder()
              .put("client.transport.sniff", true) //being able to retrieve other nodes
              .put("cluster.name", "hops").build();

      elasticClient = new PreBuiltTransportClient(settings)
          .addTransportAddress(new TransportAddress(
              new InetSocketAddress(getElasticIpAsString(),
                  this.settings.getElasticPort())));
    }
    return elasticClient;
  }
  
  private void shutdownClient() {
    if (elasticClient != null) {
//      elasticClient.admin().indices().clearCache(new ClearIndicesCacheRequest(Settings.META_INDEX));
      elasticClient.close();
      elasticClient = null;
    }
  }
  
  public SearchHits search(String index, String[] docTypes, QueryBuilder qb) throws AppException {
    Client client = getClient();
    try {
      if (!this.indexExists(client, index)) {
        LOG.log(Level.INFO, ResponseMessages.ELASTIC_INDEX_NOT_FOUND);
        throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
          ResponseMessages.ELASTIC_INDEX_NOT_FOUND);
      }

      SearchRequestBuilder srb = client.prepareSearch(index).setTypes(docTypes).setQuery(qb);
      ActionFuture<SearchResponse> futureResponse = srb.execute();
      SearchResponse response = futureResponse.actionGet();

      int respStatus = response.status().getStatus();
      if (respStatus == 200) {
        return response.getHits();
      } else {
        LOG.log(Level.WARNING, "Elasticsearch error code: {0}", respStatus);
        throw new AppException(respStatus, ResponseMessages.ELASTIC_SERVER_ERROR);
      }
    } finally {
      shutdownClient();
    }
  }

  public void add(String index, String docType, String docId, ElasticDoc doc) throws AppException {
    Client client = getClient();
    try {
      if (!this.indexExists(client, index)) {
        LOG.log(Level.INFO, ResponseMessages.ELASTIC_INDEX_NOT_FOUND);
        throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
          ResponseMessages.ELASTIC_INDEX_NOT_FOUND);
      }
      
      XContentBuilder bDoc;
      try {
        bDoc = doc.elasticSerialize();
      } catch (IOException ex) {
        throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), 
          ResponseMessages.ELASTIC_SERVER_ERROR);
      }
      IndexRequestBuilder irb = client.prepareIndex(index, docType, docId).setSource(bDoc);
      ActionFuture<IndexResponse> futureResponse = irb.execute();
      IndexResponse response = futureResponse.actionGet();

      int respStatus = response.getShardInfo().status().getStatus();
      if (respStatus == 200) {
        LOG.log(HopsSiteSettings.DEBUG, "added to elastic:{0}", docId);
        return;
      } else {
        LOG.log(Level.WARNING, "Elasticsearch error code: {0}", respStatus);
        throw new AppException(respStatus, ResponseMessages.ELASTIC_SERVER_ERROR);
      }
    } finally {
      shutdownClient();
    }
  }

  public void delete(String index, String docType, String docId) throws AppException {
    Client client = getClient();
    try {
      if (!this.indexExists(client, index)) {
        LOG.log(Level.INFO, ResponseMessages.ELASTIC_INDEX_NOT_FOUND);
        throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
          ResponseMessages.ELASTIC_INDEX_NOT_FOUND);
      }

      DeleteRequestBuilder irb = client.prepareDelete(index, docType, docId);
      ActionFuture<DeleteResponse> futureResponse = irb.execute();
      DeleteResponse response = futureResponse.actionGet();

      int respStatus = response.getShardInfo().status().getStatus();
      if (respStatus == 200) {
        return;
      } else {
        LOG.log(Level.WARNING, "Elasticsearch error code: {0}", respStatus);
        throw new AppException(respStatus, ResponseMessages.ELASTIC_SERVER_ERROR);
      }
    } finally {
      shutdownClient();
    }
  }

  private boolean indexExists(Client client, String indexName) {
    AdminClient admin = client.admin();
    IndicesAdminClient indices = admin.indices();
    IndicesExistsRequestBuilder indicesExistsRequestBuilder = indices.prepareExists(indexName);
    IndicesExistsResponse response = indicesExistsRequestBuilder.execute().actionGet();
    return response.isExists();
  }
  
  private String getElasticIpAsString() throws AppException {
    String addr = settings.getElasticIp();

    // Validate the ip address pulled from the variables
    if (Ip.validIp(addr) == false) {
      try {
        InetAddress.getByName(addr);
      } catch (UnknownHostException ex) {
        LOG.log(Level.SEVERE, ResponseMessages.ELASTIC_SERVER_NOT_AVAILABLE);
        throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.
          getStatusCode(), ResponseMessages.ELASTIC_SERVER_NOT_AVAILABLE);
      }
    }
    return addr;
  }
}
