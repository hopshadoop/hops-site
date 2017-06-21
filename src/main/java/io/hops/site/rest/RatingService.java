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

import io.hops.site.controller.RatingController;
import io.hops.site.dao.entity.DatasetRating;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dto.RateDTO;
import io.hops.site.dto.RatingDTO;
import io.hops.site.rest.annotation.NoCache;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@Api(value = "/rating", description = "Rating service")
@TransactionAttribute(TransactionAttributeType.NEVER)
public class RatingService {

  private final static Logger LOGGER = Logger.getLogger(RatingService.class.getName());

  @EJB
  private RatingController ratingController;

  @GET
  @NoCache
  @Path("{datasetId}")
  public Response getRating(@PathParam("datasetId") Integer datasetId) {
    RatingDTO ratingDto = ratingController.getRating(datasetId);
    LOGGER.log(Level.INFO, "Get all rating for dataset: {0}", datasetId);
    return Response.ok().entity(ratingDto).build();
  }

  @GET
  @NoCache
  @Path("byPublicId/{publicId}")
  public Response getRatingByPublicId(@PathParam("publicId") String publicId) {
    RatingDTO ratingDto = ratingController.getRating(publicId);
    LOGGER.log(Level.INFO, "Get all rating for dataset: {0}", publicId);
    return Response.ok().entity(ratingDto).build();
  }

  @GET
  @NoCache
  @Path("all/{datasetId}")
  public Response getAllRatings(@PathParam("datasetId") Integer datasetId) {
    List<DatasetRating> ratings = ratingController.getAllRatings(datasetId);
    GenericEntity<List<DatasetRating>> datasetRatings
            = new GenericEntity<List<DatasetRating>>(ratings) {
    };
    LOGGER.log(Level.INFO, "Get all rating for dataset: {0}", datasetId);
    return Response.ok().entity(datasetRatings).build();
  }

  @GET
  @NoCache
  @Path("all/byPublicId/{publicId}")
  public Response getAllRatingsByPublicId(@PathParam("publicId") String publicId) {
    List<DatasetRating> ratings = ratingController.getAllRatings(publicId);
    GenericEntity<List<DatasetRating>> datasetRatings
            = new GenericEntity<List<DatasetRating>>(ratings) {
    };
    LOGGER.log(Level.INFO, "Get all rating for dataset: {0}", publicId);
    return Response.ok().entity(datasetRatings).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addRating(RateDTO datasetRating) {
    ratingController.addRating(datasetRating);
    LOGGER.log(Level.INFO, "Add rating for dataset: {0}", datasetRating.getDatasetId());
    return Response.ok().build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateRating(RateDTO datasetRating) {
    ratingController.addRating(datasetRating);
    LOGGER.log(Level.INFO, "Update rating to: {0}", datasetRating.getRating());
    return Response.ok().build();
  }

  @DELETE
  @Path("{ratingId}")
  public Response deleteRating(@PathParam("ratingId") Integer ratingId) {
    ratingController.deleteRating(ratingId);
    LOGGER.log(Level.INFO, "Delete rating with id: {0}", ratingId);
    return Response.ok().build();
  }
}
