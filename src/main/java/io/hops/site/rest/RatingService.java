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
package io.hops.site.rest;

import io.hops.site.controller.HopsSiteSettings;
import io.hops.site.controller.RatingController;
import io.hops.site.dao.entity.DatasetRating;
import io.hops.site.old_dto.RateDTO;
import io.hops.site.old_dto.RatingDTO;
import io.hops.site.rest.annotation.NoCache;
import io.hops.site.rest.exception.ThirdPartyException;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("rating")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "rating",
        description = "Rating service")
@TransactionAttribute(TransactionAttributeType.NEVER)
public class RatingService {

  private final static Logger LOG = Logger.getLogger(RatingService.class.getName());

  @EJB
  private RatingController ratingController;
  
  //**************************************************PUBLIC************************************************************
  @GET
  @NoCache
  @Path("dataset/{publicDSId}")
  public Response getRatingByPublicId(@PathParam("publicDSId") String publicDSId) throws ThirdPartyException {
    RatingDTO ratingDto = ratingController.getRating(publicDSId);
    LOG.log(Level.INFO, "Get all rating for dataset: {0}", publicDSId);
    return Response.ok().entity(ratingDto).build();
  }

  @GET
  @NoCache
  @Path("all/{publicDSId}")
  public Response getAllRatings(@PathParam("publicDSId") String publicDSId) throws ThirdPartyException {
    List<DatasetRating> ratings = ratingController.getAllRatings(publicDSId);
    GenericEntity<List<DatasetRating>> datasetRatings = new GenericEntity<List<DatasetRating>>(ratings) {};
    LOG.log(Level.INFO, "Get all rating for dataset: {0}", publicDSId);
    return Response.ok().entity(datasetRatings).build();
  }

  @GET
  @NoCache
  @Path("all/byPublicId/{publicDSId}")
  public Response getAllRatingsByPublicId(@PathParam("publicDSId") String publicDSId) throws ThirdPartyException {
    List<DatasetRating> ratings = ratingController.getAllRatings(publicDSId);
    GenericEntity<List<DatasetRating>> datasetRatings = new GenericEntity<List<DatasetRating>>(ratings) {};
    LOG.log(Level.INFO, "Get all rating for dataset: {0}", publicDSId);
    return Response.ok().entity(datasetRatings).build();
  }

  //****************************************************CLUSTER ID*****************************************************
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/add/{publicCId}")
  public Response addRating(@PathParam("publicCId") String publicCId, RateDTO datasetRating) 
    throws ThirdPartyException {
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:rating:add {0}", publicCId);
    ratingController.addRating(publicCId, datasetRating);
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:rating:add - done {0}", publicCId);
    return Response.ok("ok").build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateRating(RateDTO datasetRating) {
    ratingController.updateRating(datasetRating);
    LOG.log(Level.INFO, "Update rating to: {0}", datasetRating.getRating());
    return Response.ok("OK").build();
  }

  @DELETE
  @Path("{ratingId}")
  @RolesAllowed({"admin"})
  public Response deleteRating(@PathParam("ratingId") Integer ratingId) {
    ratingController.deleteRating(ratingId);
    LOG.log(Level.INFO, "Delete rating with id: {0}", ratingId);
    return Response.ok("OK").build();
  }
}
