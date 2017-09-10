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
package io.hops.site.controller;

import io.hops.site.dao.entity.Comment;
import io.hops.site.dao.entity.CommentIssue;
import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.entity.Users;
import io.hops.site.dao.facade.CommentFacade;
import io.hops.site.dao.facade.CommentIssueFacade;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import io.hops.site.dao.facade.UsersFacade;
import io.hops.site.dto.CommentDTO;
import io.hops.site.dto.CommentIssueDTO;
import io.hops.site.dto.UserDTO;
import io.hops.site.rest.exception.ThirdPartyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.core.Response;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class CommentController {

  private final static Logger LOGGER = Logger.getLogger(CommentController.class.getName());

  @EJB
  private RegisteredClusterFacade clusterFacade;
  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private UsersFacade userFacade;
  @EJB
  private CommentFacade commentFacade;
  @EJB
  private CommentIssueFacade commentIssueFacade;

  /**
   * Get all dataset comments for the given public dataset id
   *
   * @param publicDSId
   * @return
   */
  public List<CommentDTO.RetrieveComment> getAllComments(String publicDSId) throws ThirdPartyException {
    Dataset dataset = getDataset(publicDSId);
    List<CommentDTO.RetrieveComment> comments = new ArrayList();
    for(Comment c : dataset.getCommentCollection()) {
      UserDTO user = new UserDTO(c.getUsers());
      comments.add(new CommentDTO.RetrieveComment(c.getId(), c.getContent(), user, c.getDatePublished()));
    }
    return comments;
  }
  
  /**
   * Add new comment by comment.user for comment.dataset
   *
   * @param comment
   */
  public void addComment(String publicCId, String publicDSId, CommentDTO.Publish comment) throws ThirdPartyException {
    commentDTOSanityCheck(publicDSId, comment);
    Dataset dataset = getDataset(publicDSId);
    Users user = getUser(publicCId, comment.getUserEmail());
    Comment newComment = new Comment(comment.getContent(), user, dataset);
    commentFacade.create(newComment);
    LOGGER.log(Level.INFO, "Adding new comment.");
  }

  /**
   * Update comment content
   *
   * @param comment
   */
  public void updateComment(String publicCId, String publicDSId, Integer commentId, CommentDTO.Publish comment) 
    throws ThirdPartyException {
    commentDTOSanityCheck(publicDSId, comment);
    Users user = getUser(publicCId, comment.getUserEmail());
    Comment managedComment = getComment(commentId);
    managedCommentCheck(user, managedComment);
    if (managedComment.getContent().equals(comment.getContent())) {
      return;
    }
    LOGGER.log(Level.INFO, "Updating comment: {0}.", commentId);
    managedComment.setContent(comment.getContent());
    commentFacade.edit(managedComment);
  }

  /**
   * Remove comment identified by commentId
   *
   * @param commentId
   */
  public void removeComment(String publicCId, String publicDSId, Integer commentId, String userEmail) 
    throws ThirdPartyException {
    if (commentId == null) {
      throw new IllegalArgumentException("Comment id not assigned.");
    }
    Users user = getUser(publicCId, userEmail);
    Comment managedComment = getComment(commentId);
    managedCommentCheck(user, managedComment);
    LOGGER.log(Level.INFO, "Removing comment: {0}.", managedComment.getId());
    commentFacade.remove(managedComment);
  }

  /**
   * Report abuse on commentIssue.comment
   *
   * @param commentIssue
   */
  public void reportAbuse(String publicCId, String publicDSId, Integer commentId, CommentIssueDTO commentIssue) 
    throws ThirdPartyException {
    commentIssueDTOSanityCheck(commentIssue);
    Users user = getUser(publicCId, commentIssue.getUserEmail());
    Comment managedComment = getComment(commentId);
    CommentIssue newCommentIssue = 
      new CommentIssue(commentIssue.getType(), commentIssue.getMsg(), managedComment.getUsers(), managedComment);
    commentIssueFacade.create(newCommentIssue);
    LOGGER.log(Level.INFO, "Adding new issue for comment: {0}.", managedComment.getId());
  }

  private void commentDTOSanityCheck(String publicDSId, CommentDTO.Publish comment) {
    if (comment == null || publicDSId == null || comment.getUserEmail()== null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (comment.getContent().isEmpty()) {
      throw new IllegalArgumentException("Comment content can not be empty.");
    }
  }
  
  private void managedCommentCheck(Users user, Comment comment) throws ThirdPartyException {
    if (!comment.getUsers().getId().equals(user.getId())) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(),
        "comment not found for given user", ThirdPartyException.Source.REMOTE_DELA, "bad request");
    }
  }

  private void commentIssueDTOSanityCheck(CommentIssueDTO commentIssue) {
    if (commentIssue == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (commentIssue.getUserEmail()== null) {
      throw new IllegalArgumentException("User email not assigned.");
    }
    if (commentIssue.getType().isEmpty()) {
      throw new IllegalArgumentException("Issue type not assigned.");
    }
  }

  private Dataset getDataset(String publicDSId) throws ThirdPartyException {
    Optional<Dataset> dataset = datasetFacade.findByPublicId(publicDSId);
    if (!dataset.isPresent()) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(),
        ThirdPartyException.Error.DATASET_DOES_NOT_EXIST, ThirdPartyException.Source.REMOTE_DELA, "bad request");
    }
    return dataset.get();
  }

  private Users getUser(String publicCId, String userEmail) throws ThirdPartyException {
    Optional<Users> user = userFacade.findByEmailAndPublicClusterId(userEmail, publicCId);
    if (!user.isPresent()) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(),
        ThirdPartyException.Error.USER_NOT_REGISTERED, ThirdPartyException.Source.REMOTE_DELA, "bad request");
    }
    return user.get();
  }

  private Comment getComment(Integer commentId) throws ThirdPartyException {
    Comment managedComment = commentFacade.find(commentId);
    if (managedComment == null) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(),
        "comment not found", ThirdPartyException.Source.REMOTE_DELA, "bad request");
    }
    return managedComment;
  }
}
