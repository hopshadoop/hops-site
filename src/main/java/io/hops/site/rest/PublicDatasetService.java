package io.hops.site.rest;

import com.google.gson.Gson;
import io.hops.site.controller.DatasetController;
import io.hops.site.controller.HopsSiteSettings;
import io.hops.site.dto.SearchServiceDTO;
import io.hops.site.old_dto.JsonResponse;
import io.hops.site.rest.annotation.NoCache;
import io.hops.site.rest.exception.AppException;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("public/dataset")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "public/dataset",
        description = "Dataset service")
@TransactionAttribute(TransactionAttributeType.NEVER)
public class PublicDatasetService {
  private final static Logger LOG = Logger.getLogger(PublicDatasetService.class.getName());
  @EJB
  private DatasetController datasetCtrl;
  
  @POST
  @NoCache
  @Path("search")
  @Produces(MediaType.APPLICATION_JSON)
  public Response search(SearchServiceDTO.Params searchParams) {
    searchDTOParamsSanityCheck(searchParams);
    try {
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset search");
      SearchServiceDTO.SearchResult searchResult = datasetCtrl.search(searchParams);
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset search - done");
      return Response.ok(searchResult).build();
    } catch (AppException ex) {
      return Response.status(ex.getStatus()).entity(new JsonResponse(ex.getMessage())).build();
    }
  }
  
  private void searchDTOParamsSanityCheck(SearchServiceDTO.Params req) {
  }
  
  @GET
  @NoCache
  @Path("search/{sessionId}/page/{startItem}/{nrItems}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPage(@PathParam("sessionId") String sessionId, @PathParam("startItem") Integer startItem, 
    @PathParam("nrItems") Integer nrItems) {
    try {
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset search page");
      List<SearchServiceDTO.Item> result = datasetCtrl.getSearchPage(sessionId, startItem, nrItems);
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset search page - done");
      return Response.ok(new Gson().toJson(result)).build();
    } catch (AppException ex) {
      return Response.status(ex.getStatus()).entity(new JsonResponse(ex.getMessage())).build();
    }
  }
  
  @GET
  @NoCache
  @Path("{publicDSId}/details")
  @Produces(MediaType.APPLICATION_JSON)
  public Response details(@PathParam("publicDSId") String publicDSId) throws AppException {
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset - details {0}", publicDSId);
    SearchServiceDTO.ItemDetails result = datasetCtrl.getDetails(publicDSId);
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset:done - details {0}", publicDSId);
    return Response.ok(new Gson().toJson(result)).build();
  }
}
