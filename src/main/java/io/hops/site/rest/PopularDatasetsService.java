package io.hops.site.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hops.site.dao.entity.PopularDataset;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.hops.site.dao.facade.PopularDatasetFacade;
import io.hops.site.dto.FailJSON;
import io.hops.site.dto.IdentificationJSON;
import io.hops.site.dto.ManifestJSON;
import io.hops.site.dto.PopularDatasetJSON;
import io.hops.site.dto.AddressJSON;
import io.hops.site.controller.HelperFunctions;
import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.facade.DatasetFacade;
import io.swagger.annotations.Api;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Path("populardatasets")
@Stateless
@Api(value = "/populardatasets", description = "Popular Dataset service")
@TransactionAttribute(TransactionAttributeType.NEVER)
public class PopularDatasetsService {

  private final static Logger LOGGER = Logger.getLogger(PopularDatasetsService.class.getName());
  @EJB
  private PopularDatasetFacade popularDatasetsFacade;
  @EJB
  private DatasetFacade dsFacade;
  @EJB
  private HelperFunctions helperFunctions;

  private final ObjectMapper mapper = new ObjectMapper();

  @PUT
  @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  @Produces(MediaType.APPLICATION_JSON)
  public Response PopularDatasets(IdentificationJSON identification) throws IOException {

    if (!helperFunctions.ClusterRegisteredWithId(identification.getClusterId())) {
      LOGGER.log(Level.INFO, "Invalid cluster id.");
      return Response.status(Response.Status.FORBIDDEN).entity(new FailJSON("Invalid cluster id")).build();
    } else {
      List<PopularDatasetJSON> popularDatasetsJsons = new LinkedList<>();
      for (PopularDataset pd : helperFunctions.getTopTenDatasets()) {
        ManifestJSON manifestJson = mapper.readValue(pd.getManifest(), ManifestJSON.class);
        List<AddressJSON> gvodEndpoints = mapper.readValue(pd.getPartners(), new TypeReference<List<AddressJSON>>() {
        });
        popularDatasetsJsons.add(new PopularDatasetJSON(manifestJson, pd.getDatasetId().getPublicId(), pd.getLeeches(),
                pd.getSeeds(),
                gvodEndpoints));
      }

      GenericEntity<List<PopularDatasetJSON>> searchResults = new GenericEntity<List<PopularDatasetJSON>>(
              popularDatasetsJsons) {
      };
      return Response.status(Response.Status.OK).entity(searchResults).build();
    }

  }

  @POST
  @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  @Produces(MediaType.APPLICATION_JSON)
  public void PopularDatasetsAdd(PopularDatasetJSON popularDatasetsJson) throws JsonProcessingException {

    if (popularDatasetsJson.getIdentification().getClusterId() != null && helperFunctions.ClusterRegisteredWithId(
            popularDatasetsJson.getIdentification().getClusterId())) {
      Dataset ds = dsFacade.findByPublicId(popularDatasetsJson.getDatasetId());
      if (ds == null) {
        return;
      }
      PopularDataset popularDataset = new PopularDataset(ds, mapper.writeValueAsString(popularDatasetsJson.
              getManifestJson()), mapper.writeValueAsString(popularDatasetsJson.getGvodEndpoints()),
              popularDatasetsJson.getLeeches(), popularDatasetsJson.getSeeds());

      this.popularDatasetsFacade.create(popularDataset);
    }

  }

}
