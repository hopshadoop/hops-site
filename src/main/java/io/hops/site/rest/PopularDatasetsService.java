package io.hops.site.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.hops.site.controller.DatasetController;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.hops.site.dto.PopularDatasetJSON;
import io.hops.site.rest.annotation.NoCache;
import io.swagger.annotations.Api;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.GET;

@Path("populardatasets")
@Stateless
@Api(value = "/populardatasets",
        description = "Popular Dataset service")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@TransactionAttribute(TransactionAttributeType.NEVER)
public class PopularDatasetsService {

  private final static Logger LOGGER = Logger.getLogger(PopularDatasetsService.class.getName());

  @EJB
  private DatasetController datasetController;

  @GET
  @NoCache
  @Path("{clusterId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response popularDatasets(String clusterId) throws IOException {
    List<PopularDatasetJSON> popularDatasetsJsons = datasetController.getPopularDatasets(clusterId);
    GenericEntity<List<PopularDatasetJSON>> searchResults = new GenericEntity<List<PopularDatasetJSON>>(
            popularDatasetsJsons) {
    };
    return Response.status(Response.Status.OK).entity(searchResults).build();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Response popularDatasetsAdd(PopularDatasetJSON popularDatasetsJson) throws JsonProcessingException {
    datasetController.addPopularDatasets(popularDatasetsJson);
    LOGGER.log(Level.INFO, "Add new popular dataset with public id: {0}", popularDatasetsJson.getDatasetId());
    return Response.ok().build();
  }

}
