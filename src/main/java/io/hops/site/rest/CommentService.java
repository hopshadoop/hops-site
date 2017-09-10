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

import io.hops.site.controller.CommentController;
import io.hops.site.controller.HopsSiteSettings;
import io.hops.site.dao.entity.Comment;
import io.hops.site.dto.CommentDTO;
import io.hops.site.dto.CommentIssueDTO;
import io.hops.site.rest.annotation.NoCache;
import io.hops.site.rest.exception.ThirdPartyException;
import io.swagger.annotations.Api;
import java.util.List;
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

@Path("comment")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/comment",
  description = "Comment service")
@TransactionAttribute(TransactionAttributeType.NEVER)
public class CommentService {

  private final static Logger LOG = Logger.getLogger(CommentService.class.getName());

  @EJB
  private CommentController commentController;

  @GET
  @NoCache
  @Path("dataset/{publicDSId}/all")
  public Response getAllByPublicId(@PathParam("publicDSId") String publicDSId) throws ThirdPartyException {
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:comment:all <{0}>", new Object[]{publicDSId});
    List<Comment> comments = commentController.getAllComments(publicDSId);
    GenericEntity<List<Comment>> datasetComments = new GenericEntity<List<Comment>>(comments) {
    };
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:comment:all - done <{0}>", new Object[]{publicDSId});
    return Response.ok().entity(datasetComments).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("cluster/{publicCId}/dataset/{publicDSId}/add")
  public Response addComment(@PathParam("publicCId") String publicCId, @PathParam("publicDSId") String publicDSId,
    CommentDTO.Publish comment) throws ThirdPartyException {
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:comment:add <{0}>", new Object[]{publicDSId});
    commentController.addComment(publicCId, publicDSId, comment);
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:comment:add - done <{0}>", new Object[]{publicDSId});
    return Response.ok("ok").build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("cluster/{publicCId}/dataset/{publicDSId}/update/{commentId}")
  public Response updateComment(@PathParam("publicCId") String publicCId, @PathParam("publicDSId") String publicDSId,
    @PathParam("commentId") Integer commentId, CommentDTO.Publish comment) throws ThirdPartyException {
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:comment:update <{0},{1}>", new Object[]{publicDSId, commentId});
    commentController.updateComment(publicCId, publicDSId, commentId, comment);
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:comment:update - done <{0}, {1}>",
      new Object[]{publicDSId, commentId});
    return Response.ok("ok").build();
  }

  @DELETE
  @Path("cluster/{publicCId}/dataset/{publicDSId}/delete/{commentId}")
  @RolesAllowed({"admin"})
  public Response deleteCommentById(@PathParam("publicCId") String publicCId, @PathParam("publicDSId") String publicDSId,
    @PathParam("commentId") Integer commentId, String userEmail) throws ThirdPartyException {
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:comment:delete <{0},{1}>", new Object[]{publicDSId, commentId});
    commentController.removeComment(publicCId, publicDSId, commentId, userEmail);
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:comment:delete - done <{0},{1}>",
      new Object[]{publicDSId, commentId});
    return Response.ok("ok").build();
  }

  @POST
  @Path("cluster/{publicCId}/dataset/{publicDSId}/report/{commentId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response reportAbuse(@PathParam("publicCId") String publicCId, @PathParam("publicDSId") String publicDSId, 
    @PathParam("commentId") Integer commentId, CommentIssueDTO commentIssue) throws ThirdPartyException {
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:comment:issue <{0},{1}>", new Object[]{publicDSId, commentId});
    commentController.reportAbuse(publicCId, publicDSId, commentId, commentIssue);
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:comment:issue - done <{0},{1}>",
      new Object[]{publicDSId, commentId});
    return Response.ok("ok").build();
  }
}
