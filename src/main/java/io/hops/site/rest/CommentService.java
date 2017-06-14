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

import io.hops.site.dao.entity.Comment;
import io.hops.site.dao.entity.CommentIssue;
import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.entity.DatasetRating;
import io.hops.site.dao.facade.CommentFacade;
import io.hops.site.dao.facade.CommentIssueFacade;
import io.hops.site.dao.facade.DatasetFacade;
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

@Path("comment")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
public class CommentService {

  private final static Logger LOGGER = Logger.getLogger(CommentService.class.getName());

  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private CommentFacade commentFacade;
  @EJB
  private CommentIssueFacade commentIssueFacade;

  @GET
  @Path("{datasetId}")
  public Response getAll(@PathParam("datasetId") Integer datasetId) {
    Dataset dataset = datasetFacade.find(datasetId);
    List<DatasetRating> ratings = new ArrayList(dataset.getDatasetRatingCollection());
    GenericEntity<List<DatasetRating>> datasetRatings
            = new GenericEntity<List<DatasetRating>>(ratings) {};
    LOGGER.log(Level.INFO, "Get all comments for dataset: {0}", datasetId);
    return Response.ok().entity(datasetRatings).build();
  }

  @GET
  @Path("byPublicId/{publicId}")
  public Response getAllByPublicId(@PathParam("publicId") String publicId) {
    Dataset dataset = datasetFacade.findByPublicId(publicId);
    List<DatasetRating> ratings = new ArrayList(dataset.getDatasetRatingCollection());
    GenericEntity<List<DatasetRating>> datasetRatings
            = new GenericEntity<List<DatasetRating>>(ratings) {};
    LOGGER.log(Level.INFO, "Get all comments for dataset: {0}", publicId);
    return Response.ok().entity(datasetRatings).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addComment(Comment comment) {

    LOGGER.log(Level.INFO, "Add comment for dataset: {0}", comment.getDatasetId().getId());
    return Response.ok().build();
  }

  @POST
  @Path("reportAbuse")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response reportAbuse(CommentIssue commentIssue) {

    LOGGER.log(Level.INFO, "Report abuse for comment: {0}", commentIssue.getCommentId().getId());
    return Response.ok().build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateComment(Comment comment) {

    LOGGER.log(Level.INFO, "Update comment with id: {0}", comment.getId());
    return Response.ok().build();
  }

  @DELETE
  @Path("{commentId}")
  public Response deleteComment(@PathParam("commentId") Integer commentId) {

    LOGGER.log(Level.INFO, "Delete comment with id: {0}", commentId);
    return Response.ok().build();
  }

}
