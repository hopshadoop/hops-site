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
import io.hops.site.dao.facade.UsersFacade;
import io.hops.site.dto.RatingDTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class RatingController {

  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private UsersFacade userFacade;
  @EJB
  private DatasetRatingFacade datasetRatingFacade;

  /**
   * Calculates the rating for the given dataset
   * @param dataset
   * @return 
   */
  public RatingDTO getRating(Dataset dataset) {
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
   * Adds rating for a dataset
   * @param datasetRating 
   */
  public void addRating(DatasetRating datasetRating) {
    if (datasetRating.getDatasetId() == null || datasetRating.getUsers() == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (datasetRating.getUsers().getEmail() == null) {
      throw new IllegalArgumentException("User email not assigned.");
    }
    if (datasetRating.getDatasetId().getId() == null && datasetRating.getDatasetId().getPublicId() == null) {
      throw new IllegalArgumentException("Dataset id not assigned.");
    }
    if (datasetRating.getRating() <= 0) {
      throw new IllegalArgumentException("Rating should be positive int.");
    }
    Dataset dataset;
    if (datasetRating.getDatasetId().getId() == null) {
      dataset = datasetFacade.findByPublicId(datasetRating.getDatasetId().getPublicId());
    } else {
      dataset = datasetFacade.find(datasetRating.getDatasetId().getId());
    }
    
    if (dataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    
    Users user = userFacade.findByEmail(datasetRating.getUsers().getEmail());
    if (user == null) {
      throw new IllegalArgumentException("User not found.");
    }
    
    datasetRating.setDatasetId(dataset);
    datasetRating.setUsers(user);  
    datasetRatingFacade.create(datasetRating);
  }

  /**
   * Removes rating with the given id.
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
