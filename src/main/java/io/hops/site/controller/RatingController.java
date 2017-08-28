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

import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.entity.DatasetRating;
import io.hops.site.dao.entity.Users;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dao.facade.DatasetRatingFacade;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import io.hops.site.dao.facade.UsersFacade;
import io.hops.site.old_dto.RateDTO;
import io.hops.site.old_dto.RatingDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class RatingController {

  @EJB
  private RegisteredClusterFacade clusterFacade;
  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private UsersFacade userFacade;
  @EJB
  private DatasetRatingFacade datasetRatingFacade;

  /**
   * Calculates the rating for the given public id
   *
   * @param datasetPublicId
   * @return
   */
  public RatingDTO getRating(String datasetPublicId) {
    Optional<Dataset> dataset = datasetFacade.findByPublicId(datasetPublicId);
    if (!dataset.isPresent()) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    List<DatasetRating> ratings = new ArrayList(dataset.get().getDatasetRatingCollection());
    if (ratings.isEmpty()) {
      return new RatingDTO(datasetPublicId, 0, 0);
    }
    int rated = 0;
    for (DatasetRating rate : ratings) {
      rated += rate.getRating();
    }
    rated /= ratings.size();
    return new RatingDTO(datasetPublicId, rated, ratings.size());
  }

  /**
   * Get all ratings by dataset id
   *
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
   *
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
    rateDTOSanityCheck(datasetRating);
    Optional<Dataset> dataset = datasetFacade.findByPublicId(datasetRating.getDatasetId());
    if (!dataset.isPresent()) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    Optional<Users> user = userFacade.findByEmailAndPublicClusterId(datasetRating.getUser().getEmail(), 
      datasetRating.getUser().getClusterId());
    if (!user.isPresent()) {
      throw new IllegalArgumentException("User not found.");
    }
    DatasetRating managedRating = datasetRatingFacade.findByDatasetAndUser(dataset.get(), user.get());
    if (managedRating != null) {
      if (managedRating.getRating() != datasetRating.getRating()) {
        managedRating.setRating(datasetRating.getRating());
        datasetRatingFacade.edit(managedRating);
      }
      return;
    }
    DatasetRating newDatasetRating = new DatasetRating(datasetRating.getRating(), user.get(), dataset.get());
    datasetRatingFacade.create(newDatasetRating);
  }
  
  private void rateDTOSanityCheck(RateDTO rateDTO) {
    if (rateDTO == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (rateDTO.getUser() == null || rateDTO.getUser().getEmail() == null) {
      throw new IllegalArgumentException("User email not assigned.");
    }
    if (rateDTO.getDatasetId() == null) {
      throw new IllegalArgumentException("Dataset id not assigned.");
    }
    if (rateDTO.getRating() <= 0) {
      throw new IllegalArgumentException("Rating should be positive int.");
    }
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

  /**
   *
   * @param datasetRating
   */
  public void updateRating(RateDTO datasetRating) {
    if (datasetRating == null || datasetRating.getId() == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (datasetRating.getRating() < 1) {
      throw new IllegalArgumentException("Rating should be positive int.");
    }

    DatasetRating rating = datasetRatingFacade.find(datasetRating.getId());
    if (rating == null) {
      throw new IllegalArgumentException("Rating not found.");
    }

    if (!rating.getUsers().getEmail().equalsIgnoreCase(datasetRating.getUser().getEmail())) {
      throw new IllegalArgumentException("Rating not found for given user.");
    }

    if (rating.getRating() == datasetRating.getRating()) {
      return;
    }
    rating.setRating(datasetRating.getRating());
    datasetRatingFacade.edit(rating);
  }
}
