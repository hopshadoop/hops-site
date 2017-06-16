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

import static io.hops.site.dao.entity.Comment_.datasetId;
import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.entity.DatasetRating;
import io.hops.site.dao.entity.Users;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dao.facade.DatasetRatingFacade;
import io.hops.site.dao.facade.UsersFacade;
import io.hops.site.dto.RateDTO;
import io.hops.site.dto.RatingDTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class RatingController {

  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private UsersFacade userFacade;
  @EJB
  private DatasetRatingFacade datasetRatingFacade;

  /**
   * Calculates the rating for the given dataset
   *
   * @param datasetId
   * @return
   */
  public RatingDTO getRating(Integer datasetId) {
    Dataset dataset = datasetFacade.find(datasetId);
    if (dataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    List<DatasetRating> ratings = new ArrayList(dataset.getDatasetRatingCollection());
    if (ratings.isEmpty()) {
      return new RatingDTO(dataset.getId(), 0, 0);
    }
    int rated = 0;
    for (DatasetRating rate : ratings) {
      rated += rate.getRating();
    }
    rated /= ratings.size();
    return new RatingDTO(dataset.getId(), rated, ratings.size());
  }

  /**
   * Calculates the rating for the given public id
   *
   * @param publicId
   * @return
   */
  public RatingDTO getRating(String publicId) {
    Dataset dataset = datasetFacade.findByPublicId(publicId);
    if (dataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    List<DatasetRating> ratings = new ArrayList(dataset.getDatasetRatingCollection());
    if (ratings.isEmpty()) {
      return new RatingDTO(dataset.getId(), 0, 0);
    }
    int rated = 0;
    for (DatasetRating rate : ratings) {
      rated += rate.getRating();
    }
    rated /= ratings.size();
    return new RatingDTO(dataset.getId(), rated, ratings.size());
  }

  /**
   * Get all ratings by dataset id
   * @param datasetId
   * @return
   */
  public List<DatasetRating> getAllRatings(Integer datasetId) {
    Dataset dataset = datasetFacade.find(datasetId);
    if (dataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    List<DatasetRating> ratings = new ArrayList(dataset.getDatasetRatingCollection());
    return ratings;
  }

  /**
   * Get all ratings by public id
   * @param publicId
   * @return
   */
  public List<DatasetRating> getAllRatings(String publicId) {
    Dataset dataset = datasetFacade.find(publicId);
    if (dataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    List<DatasetRating> ratings = new ArrayList(dataset.getDatasetRatingCollection());
    return ratings;
  }

  /**
   * Adds rating for a dataset
   *
   * @param datasetRating
   */
  public void addRating(RateDTO datasetRating) {
    if (datasetRating == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (datasetRating.getUserEmail() == null) {
      throw new IllegalArgumentException("User email not assigned.");
    }
    if (datasetRating.getDatasetId() == null && datasetRating.getPublicId() == null) {
      throw new IllegalArgumentException("Dataset id not assigned.");
    }
    if (datasetRating.getRating() <= 0) {
      throw new IllegalArgumentException("Rating should be positive int.");
    }
    Dataset dataset;
    if (datasetRating.getDatasetId() == null) {
      dataset = datasetFacade.findByPublicId(datasetRating.getPublicId());
    } else {
      dataset = datasetFacade.find(datasetRating.getDatasetId());
    }

    if (dataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }

    Users user = userFacade.findByEmail(datasetRating.getUserEmail());
    if (user == null) {
      throw new IllegalArgumentException("User not found.");
    }

    DatasetRating newDatasetRating = new DatasetRating(datasetRating.getRating(), user, dataset);
    //get by user and ds and update.
    datasetRatingFacade.create(newDatasetRating);
  }

  /**
   * Removes rating with the given id.
   *
   * @param ratingId
   */
  public void deleteRating(Integer ratingId) {
    if (ratingId == null) {
      throw new IllegalArgumentException("Id not assigned.");
    }
    DatasetRating datasetRating = datasetRatingFacade.find(ratingId);
    datasetRatingFacade.remove(datasetRating);
  }
}
