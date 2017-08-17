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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

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
   * Add new comment by comment.user for comment.dataset
   *
   * @param comment
   */
  public void addComment(CommentDTO comment) {
    commentDTOSanityCheck(comment);
    Optional<Dataset> dataset = datasetFacade.findByPublicId(comment.getDatasetId());
    if (!dataset.isPresent()) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    Optional<Users> user = userFacade.findByEmailAndPublicClusterId(comment.getUser().getEmail(),
      comment.getDatasetId());
    if (user.isPresent()) {
      throw new IllegalArgumentException("User not found.");
    }
    Comment newComment = new Comment(comment.getContent(), user.get(), dataset.get());
    commentFacade.create(newComment);
    LOGGER.log(Level.INFO, "Adding new comment.");
  }

  private void commentDTOSanityCheck(CommentDTO comment) {
    if (comment == null || comment.getDatasetId() == null || comment.getUser() == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (comment.getUser().getEmail() == null) {
      throw new IllegalArgumentException("User email not assigned.");
    }
    if (comment.getContent().isEmpty()) {
      throw new IllegalArgumentException("Comment content can not be empty.");
    }
  }

  /**
   * Report abuse on commentIssue.comment
   *
   * @param commentIssue
   */
  public void reportAbuse(CommentIssueDTO commentIssue) {
    commentIssueDTOSanityCheck(commentIssue);
    Comment comment = commentFacade.find(commentIssue.getCommentId());
    if (comment == null) {
      throw new IllegalArgumentException("Comment not found.");
    }
    CommentIssue newCommentIssue = new CommentIssue(commentIssue.getType(), commentIssue.getMsg(), comment.getUsers(),
      comment);
    commentIssueFacade.create(newCommentIssue);
    LOGGER.log(Level.INFO, "Adding new issue for comment: {0}.", comment.getId());
  }
  
  private void commentIssueDTOSanityCheck(CommentIssueDTO commentIssue) {
    if (commentIssue == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (commentIssue.getUser() == null || commentIssue.getUser().getEmail() == null) {
      throw new IllegalArgumentException("User email not assigned.");
    }
    if (commentIssue.getCommentId() == null) {
      throw new IllegalArgumentException("Dataset id not assigned.");
    }
    if (commentIssue.getType().isEmpty()) {
      throw new IllegalArgumentException("Issue type not assigned.");
    }
  }

  /**
   * Remove comment by the author
   * <p>
   * @param comment
   */
  public void removeOwnComment(CommentDTO comment) {
    commentDTOSanityCheck(comment);
    Comment managedComment = commentFacade.find(comment.getId());
    if (managedComment == null) {
      throw new IllegalArgumentException("Comment not found.");
    }
    if (!managedComment.getUsers().getEmail().equalsIgnoreCase(comment.getUser().getEmail())) {
      throw new IllegalArgumentException("Comment not found for given user.");
    }
    LOGGER.log(Level.INFO, "Removing comment with id: {0} by the author.", comment.getId());
    commentFacade.remove(managedComment);
  }

  /**
   * Remove comment identified by commentId
   *
   * @param commentId
   */
  public void removeComment(Integer commentId) {
    if (commentId == null) {
      throw new IllegalArgumentException("Comment id not assigned.");
    }
    Comment comment = commentFacade.find(commentId);
    if (comment == null) {
      throw new IllegalArgumentException("Comment not found.");
    }
    LOGGER.log(Level.INFO, "Removing comment: {0}.", comment.getId());
    commentFacade.remove(comment);
  }

  /**
   * Update comment content
   *
   * @param comment
   */
  public void updateComment(CommentDTO comment) {
    commentDTOSanityCheck(comment);
    Comment managedComment = commentFacade.find(comment.getId());
    if (managedComment == null) {
      throw new IllegalArgumentException("Comment not found.");
    }
    if (!managedComment.getUsers().getEmail().equalsIgnoreCase(comment.getUser().getEmail())) {
      throw new IllegalArgumentException("Comment not found for given user.");
    }
    if (comment.getContent().isEmpty()) {
      throw new IllegalArgumentException("Comment content can not be empty.");
    }
    if (managedComment.getContent().equals(comment.getContent())) {
      return;
    }
    LOGGER.log(Level.INFO, "Updating comment: {0}.", comment.getId());
    managedComment.setContent(comment.getContent());
    commentFacade.edit(managedComment);
  }

  /**
   * Get all dataset comments for the given public dataset id
   *
   * @param publicId
   * @return
   */
  public List<Comment> getAllComments(String publicId) {
    Optional<Dataset> dataset = datasetFacade.findByPublicId(publicId);
    if (dataset.isPresent()) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    List<Comment> comments = new ArrayList(dataset.get().getCommentCollection());
    return comments;
  }

  /**
   * Get all dataset comments for the given dataset id
   *
   * @param datasetId
   * @return
   */
  public List<Comment> getAllComments(Integer datasetId) {
    Dataset dataset = datasetFacade.find(datasetId);
    if (dataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    List<Comment> comments = new ArrayList(dataset.getCommentCollection());
    return comments;
  }

}
