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
import io.hops.site.dto.RateDTO;
import io.hops.site.dto.RatingDTO;
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
  public RatingDTO getDatasetAllRating(String publicDSId) throws ThirdPartyException {
    Dataset dataset = getDataset(publicDSId);
    int rating = calculateRating(dataset);
    RatingDTO result = new RatingDTO(rating, dataset.getDatasetRatingCollection().size());
    return result;
  }
  
  public RatingDTO getDatasetUserRating(String publicCId, String publicDSId, String userEmail) 
    throws ThirdPartyException {
    Dataset dataset = getDataset(publicDSId);
    Optional<Users> user = userFacade.findByEmailAndPublicClusterId(userEmail, publicCId);
    if(!user.isPresent()) {
      return new RatingDTO(0, dataset.getDatasetRatingCollection().size());
    }
    DatasetRating managedRating = datasetRatingFacade.findByDatasetAndUser(dataset, user.get());
    if (managedRating == null) {
      return new RatingDTO(0, dataset.getDatasetRatingCollection().size());
    } else {
      return new RatingDTO(managedRating.getRating(), dataset.getDatasetRatingCollection().size());
    }
  }

  /**
   * Adds rating for a dataset
   *
   * @param ratingDTO
   */
  public void addRating(String publicCId, String publicDSId, RateDTO rateDTO) throws ThirdPartyException {
    rateDTOSanityCheck(rateDTO);
    Dataset dataset = getDataset(publicDSId);
    Users user = getUser(publicCId, rateDTO.getUserEmail());
    DatasetRating managedRating = datasetRatingFacade.findByDatasetAndUser(dataset, user);
    if (managedRating != null) {
      if (managedRating.getRating() == rateDTO.getRating()) {
        //no update in rating - so we do not need to recompute
        return;
      }
      managedRating.setRating(rateDTO.getRating());
      datasetRatingFacade.edit(managedRating);
    } else {
      DatasetRating newDatasetRating = new DatasetRating(rateDTO.getRating(), user, dataset);
      datasetRatingFacade.create(newDatasetRating);
    }
    updateRatingInDataset(dataset);
  }

  //********************************************************************************************************************
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

  //********************************************************************************************************************
  private void updateRatingInDataset(Dataset dataset) {
    //update dataset
    int rated = calculateRating(dataset);
    dataset.setRating(rated);
    datasetFacade.edit(dataset);
  }

  private int calculateRating(Dataset dataset) {
    if (dataset.getDatasetRatingCollection().isEmpty()) {
      return 0;
    }
    int rated = 0;
    for (DatasetRating rate : dataset.getDatasetRatingCollection()) {
      rated += rate.getRating();
    }
    rated /= dataset.getDatasetRatingCollection().size();
    return rated;
  }

  private DatasetRating updateRateDTOSanityCheck(int rateId, RateDTO rateDTO) {
    rateDTOSanityCheck(rateDTO);

    DatasetRating rating = getDatasetRating(rateId);
    if (!rating.getUsers().getEmail().equalsIgnoreCase(rateDTO.getUserEmail())) {
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
    if (rateDTO.getUserEmail() == null) {
      throw new IllegalArgumentException("User email not assigned.");
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
