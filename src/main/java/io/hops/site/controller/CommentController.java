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
import io.hops.site.dao.entity.DatasetRating;
import io.hops.site.dao.entity.Users;
import io.hops.site.dao.facade.CommentFacade;
import io.hops.site.dao.facade.CommentIssueFacade;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dao.facade.UsersFacade;
import io.hops.site.dto.CommentDTO;
import io.hops.site.dto.CommentIssueDTO;
import java.util.ArrayList;
import java.util.List;
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
    if (comment == null || comment.getDataset() == null || comment.getUser() == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (comment.getUser().getEmail() == null) {
      throw new IllegalArgumentException("User email not assigned.");
    }
    if (comment.getDataset().getPublicId() == null) {
      throw new IllegalArgumentException("Dataset id not assigned.");
    }
    if (comment.getContent().isEmpty()) {
      throw new IllegalArgumentException("Comment content can not be empty.");
    }

    Dataset dataset;
    dataset = datasetFacade.findByPublicId(comment.getDataset().getPublicId());

    if (dataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }

    Users user = userFacade.findByEmail(comment.getUser().getEmail());
    if (user == null) {
      throw new IllegalArgumentException("User not found.");
    }
    Comment newComment = new Comment(comment.getContent(), user, dataset);
    commentFacade.create(newComment);
    LOGGER.log(Level.INFO, "Adding new comment.");
  }

  /**
   * Report abuse on commentIssue.comment
   *
   * @param commentIssue
   */
  public void reportAbuse(CommentIssueDTO commentIssue) {
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

    Comment comment = commentFacade.find(commentIssue.getId());
    if (comment == null) {
      throw new IllegalArgumentException("Comment not found.");
    }

    Users user = userFacade.findByEmail(comment.getUsers().getEmail());
    if (user == null) {
      throw new IllegalArgumentException("User not found.");
    }

    CommentIssue newCommentIssue = new CommentIssue(commentIssue.getType(), commentIssue.getMsg(), user, comment);
    commentIssueFacade.create(newCommentIssue);
    LOGGER.log(Level.INFO, "Adding new issue for comment: {0}.", comment.getId());
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
    if (comment == null || comment.getId() == null) {
      throw new IllegalArgumentException("Comment id not assigned.");
    }
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
   * Get all dataset ratings for the given public dataset id
   *
   * @param publicId
   * @return
   */
  public List<DatasetRating> getAllDatasets(String publicId) {
    Dataset dataset = datasetFacade.findByPublicId(publicId);
    if (dataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    List<DatasetRating> ratings = new ArrayList(dataset.getDatasetRatingCollection());
    return ratings;
  }

  /**
   * Get all dataset ratings for the given dataset id
   *
   * @param datasetId
   * @return
   */
  public List<DatasetRating> getAllDatasets(Integer datasetId) {
    Dataset dataset = datasetFacade.find(datasetId);
    if (dataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    List<DatasetRating> ratings = new ArrayList(dataset.getDatasetRatingCollection());
    return ratings;
  }

}
