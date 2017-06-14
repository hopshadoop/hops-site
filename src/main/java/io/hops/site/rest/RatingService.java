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
import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.entity.DatasetRating;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dto.RatingDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
public class RatingService {

  private final static Logger LOGGER = Logger.getLogger(RatingService.class.getName());

  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private RatingController ratingController;

  @GET
  @Path("{datasetId}")
  public Response getRating(@PathParam("datasetId") Integer datasetId) {
    Dataset dataset = datasetFacade.find(datasetId);
    if (dataset == null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    RatingDTO ratingDto = ratingController.getRating(dataset);
    LOGGER.log(Level.INFO, "Get all rating for dataset: {0}", datasetId);
    return Response.ok().entity(ratingDto).build();
  }

  @GET
  @Path("{publicId}")
  public Response getRatingByPublicId(@PathParam("publicId") String publicId) {
    Dataset dataset = datasetFacade.findByPublicId(publicId);
    if (dataset == null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    RatingDTO ratingDto = ratingController.getRating(dataset);
    LOGGER.log(Level.INFO, "Get all rating for dataset: {0}", publicId);
    return Response.ok().entity(ratingDto).build();
  }

  @GET
  @Path("all/{datasetId}")
  public Response getAllRatings(@PathParam("datasetId") Integer datasetId) {
    Dataset dataset = datasetFacade.find(datasetId);
    if (dataset == null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    List<DatasetRating> ratings = new ArrayList(dataset.getDatasetRatingCollection());
    GenericEntity<List<DatasetRating>> datasetRatings
            = new GenericEntity<List<DatasetRating>>(ratings) {
    };
    LOGGER.log(Level.INFO, "Get all rating for dataset: {0}", datasetId);
    return Response.ok().entity(datasetRatings).build();
  }

  @GET
  @Path("all/byPublicId/{publicId}")
  public Response getAllRatingsByPublicId(@PathParam("publicId") String publicId) {
    Dataset dataset = datasetFacade.findByPublicId(publicId);
    if (dataset == null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    List<DatasetRating> ratings = new ArrayList(dataset.getDatasetRatingCollection());
    GenericEntity<List<DatasetRating>> datasetRatings
            = new GenericEntity<List<DatasetRating>>(ratings) {
    };
    LOGGER.log(Level.INFO, "Get all rating for dataset: {0}", publicId);
    return Response.ok().entity(datasetRatings).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addRating(DatasetRating datasetRating) {
    ratingController.addRating(datasetRating);
    LOGGER.log(Level.INFO, "Add rating for dataset: {0}", datasetRating.getDatasetId().getId());
    return Response.ok().build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateRating(DatasetRating datasetRating) {
    ratingController.addRating(datasetRating);
    LOGGER.log(Level.INFO, "Update rating with id: {0}", datasetRating.getId());
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
