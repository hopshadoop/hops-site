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
import io.hops.site.rest.exception.ThirdPartyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.core.Response;

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
   * Calculates the rating for the given public dataset id
   *
   * @param publicDSId
   * @return
   */
  public RatingDTO getRating(String publicDSId) throws ThirdPartyException {
    Dataset dataset = getDataset(publicDSId);
    List<DatasetRating> ratings = new ArrayList(dataset.getDatasetRatingCollection());
    if (ratings.isEmpty()) {
      return new RatingDTO(publicDSId, 0, 0);
    }
    int rated = calculateRating(ratings);
    return new RatingDTO(publicDSId, rated, ratings.size());
  }

  /**
   * Get all ratings by public id
   *
   * @param publicDSId
   * @return
   */
  public List<DatasetRating> getAllRatings(String publicDSId) throws ThirdPartyException {
    Dataset dataset = getDataset(publicDSId);
    List<DatasetRating> ratings = new ArrayList(dataset.getDatasetRatingCollection());
    return ratings;
  }

  /**
   * Adds rating for a dataset
   *
   * @param datasetRating
   */
  public void addRating(String publicCId, RateDTO datasetRating) throws ThirdPartyException {
    rateDTOSanityCheck(datasetRating);
    Dataset dataset = getDataset(datasetRating.getDatasetId());
    Users user = getUser(datasetRating.getUser().getEmail(), publicCId);
    DatasetRating managedRating = datasetRatingFacade.findByDatasetAndUser(dataset, user);
    if (managedRating != null) {
      if (managedRating.getRating() != datasetRating.getRating()) {
        managedRating.setRating(datasetRating.getRating());
        datasetRatingFacade.edit(managedRating);
      } else {
        //no update in rating - so we do not need to recompute
        return;
      }
    } else {
      DatasetRating newDatasetRating = new DatasetRating(datasetRating.getRating(), user, dataset);
      datasetRatingFacade.create(newDatasetRating);
    }
    updateRatingInDataset(dataset);
  }

  /**
   * Removes rating with the given id.
   *
   * @param ratingId
   */
  public void deleteRating(Integer ratingId) {
    DatasetRating datasetRating = getDatasetRating(ratingId);
    Dataset dataset = datasetRating.getDatasetId();
    datasetRatingFacade.remove(datasetRating);
    updateRatingInDataset(dataset);
  }

  /**
   *
   * @param datasetRating
   */
  public void updateRating(RateDTO datasetRating) {
    DatasetRating rating = updateRateDTOSanityCheck(datasetRating);

    if (rating.getRating() == datasetRating.getRating()) {
      return;
    }
    rating.setRating(datasetRating.getRating());
    datasetRatingFacade.edit(rating);
    updateRatingInDataset(rating.getDatasetId());
  }

  private void updateRatingInDataset(Dataset dataset) {
    //update dataset
    List<DatasetRating> ratings = new ArrayList(dataset.getDatasetRatingCollection());
    int rated = calculateRating(ratings);
    dataset.setRating(rated);
    datasetFacade.edit(dataset);
  }

  private int calculateRating(List<DatasetRating> ratings) {
    int rated = 0;
    for (DatasetRating rate : ratings) {
      rated += rate.getRating();
    }
    rated /= ratings.size();
    return rated;
  }

  private DatasetRating updateRateDTOSanityCheck(RateDTO rateDTO) {
    rateDTOSanityCheck(rateDTO);

    DatasetRating rating = getDatasetRating(rateDTO.getId());
    if (!rating.getUsers().getEmail().equalsIgnoreCase(rateDTO.getUser().getEmail())) {
      throw new IllegalArgumentException("Rating not found for given user.");
    }
    return rating;
  }

  private DatasetRating getDatasetRating(Integer ratingId) {
    if (ratingId == null) {
      throw new IllegalArgumentException("Rating not found.");
    }

    DatasetRating rating = datasetRatingFacade.find(ratingId);
    if (rating == null) {
      throw new IllegalArgumentException("Rating not found.");
    }
    return rating;
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
}
