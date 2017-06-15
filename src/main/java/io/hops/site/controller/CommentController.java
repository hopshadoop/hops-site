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
import io.hops.site.dao.facade.UsersFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
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
   * @param comment 
   */
  public void addComment(Comment comment) {
    if (comment == null || comment.getDatasetId() == null || comment.getUsers() == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (comment.getUsers().getEmail() == null) {
      throw new IllegalArgumentException("User email not assigned.");
    }
    if (comment.getDatasetId().getId() == null && comment.getDatasetId().getPublicId() == null) {
      throw new IllegalArgumentException("Dataset id not assigned.");
    }
    if (comment.getContent().isEmpty()) {
      throw new IllegalArgumentException("Comment content can not be empty.");
    }

    Dataset dataset;
    if (comment.getDatasetId().getId() == null) {
      dataset = datasetFacade.findByPublicId(comment.getDatasetId().getPublicId());
    } else {
      dataset = datasetFacade.find(comment.getDatasetId().getId());
    }

    if (dataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }

    Users user = userFacade.findByEmail(comment.getUsers().getEmail());
    if (user == null) {
      throw new IllegalArgumentException("User not found.");
    }
    LOGGER.log(Level.INFO, "Adding new comment.");
    comment.setDatasetId(dataset);
    comment.setUsers(user);
    commentFacade.create(comment);
  }

  /**
   * Report abuse on commentIssue.comment
   * @param commentIssue 
   */
  public void reportAbuse(CommentIssue commentIssue) {
    if (commentIssue == null || commentIssue.getCommentId() == null || commentIssue.getUsers() == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (commentIssue.getUsers().getEmail() == null) {
      throw new IllegalArgumentException("User email not assigned.");
    }
    if (commentIssue.getCommentId().getId() == null) {
      throw new IllegalArgumentException("Dataset id not assigned.");
    }
    if (commentIssue.getType().isEmpty()) {
      throw new IllegalArgumentException("Issue type not assigned.");
    }

    Comment comment = commentFacade.find(commentIssue.getCommentId().getId());
    if (comment == null) {
      throw new IllegalArgumentException("Comment not found.");
    }

    Users user = userFacade.findByEmail(comment.getUsers().getEmail());
    if (user == null) {
      throw new IllegalArgumentException("User not found.");
    }

    LOGGER.log(Level.INFO, "Adding new issue for comment: {0}.", comment.getId());
    commentIssue.setCommentId(comment);
    commentIssue.setUsers(user);
    commentIssueFacade.create(commentIssue);
  }

  /**
   * Remove comment identified by commentId
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
   * @param comment 
   */
  public void updateComment(Comment comment) {
    if (comment == null || comment.getDatasetId() == null) {
      throw new IllegalArgumentException("Comment id not assigned.");
    }
    Comment managedComment = commentFacade.find(comment.getId());
    if (comment == null) {
      throw new IllegalArgumentException("Comment not found.");
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

}
