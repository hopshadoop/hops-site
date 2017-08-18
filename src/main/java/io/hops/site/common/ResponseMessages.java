package io.hops.site.common;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class ResponseMessages {
    public final static String ELASTIC_SERVER_NOT_AVAILABLE
          = "The Elasticsearch Server is either down or misconfigured.";
    public final static String ELASTIC_INDEX_NOT_FOUND
          = "Elasticsearch indices do not exist";
     public final static String ELASTIC_SERVER_ERROR
          = "Problem when reaching the Elasticsearch server";
}