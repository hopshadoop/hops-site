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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hops.site.dao.entity.Category;
import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.entity.DatasetIssue;
import io.hops.site.dao.entity.PopularDataset;
import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dao.entity.Users;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dao.facade.DatasetIssueFacade;
import io.hops.site.dao.facade.PopularDatasetFacade;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import io.hops.site.dao.facade.UsersFacade;
import io.hops.site.dto.AddressJSON;
import io.hops.site.dto.DatasetDTO;
import io.hops.site.dto.DatasetIssueDTO;
import io.hops.site.dto.ManifestJSON;
import io.hops.site.dto.PopularDatasetJSON;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class DatasetController {

  private final static Logger LOGGER = Logger.getLogger(DatasetController.class.getName());

  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private DatasetIssueFacade datasetIssueFacade;
  @EJB
  private UsersFacade userFacade;
  @EJB
  private RegisteredClusterFacade registeredClusterFacade;
  @EJB
  private HelperFunctions helperFunctions;
  @EJB
  private DatasetFacade dsFacade;
  @EJB
  private PopularDatasetFacade popularDatasetsFacade;

  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * Add dataset to table
   *
   * @param dataset
   */
  public void addDataset(DatasetDTO dataset) {
    if (dataset == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (dataset.getPublicId() == null || dataset.getPublicId().isEmpty()) {
      throw new IllegalArgumentException("Public id not assigned.");
    }
    if (dataset.getName() == null || dataset.getName().isEmpty()) {
      throw new IllegalArgumentException("Dataset name not assigned.");
    }
    if (dataset.getOwner() == null || dataset.getOwner().isEmpty()) {
      throw new IllegalArgumentException("Dataset owner not assigned.");
    }
    if (dataset.getClusterId() == null || dataset.getClusterId().isEmpty()) {
      throw new IllegalArgumentException("Cluster id not assigned.");
    }
    RegisteredCluster registeredCluster = registeredClusterFacade.find(dataset.getClusterId());
    if (registeredCluster == null) {
      throw new IllegalArgumentException("Cluster not found.");
    }

    Dataset newDataset = new Dataset(dataset.getPublicId(), dataset.getName(), dataset.getDescription(), dataset.
            getMadePublicOn(), dataset.getOwner(), dataset.getReadme(), createCategoryCollection(dataset.
                    getCategoryCollection()), registeredCluster);
    datasetFacade.create(newDataset);
    LOGGER.log(Level.INFO, "Adding new dataset with public id: {0}.", dataset.getPublicId());
  }

  /**
   * Report dataset issue
   *
   * @param datasetIssue
   */
  public void reportDatasetIssue(DatasetIssueDTO datasetIssue) {
    if (datasetIssue == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (datasetIssue.getDataset() == null) {
      throw new IllegalArgumentException("Dataset not assigned.");
    }
    if (datasetIssue.getDataset().getPublicId() == null) {
      throw new IllegalArgumentException("Dataset id not assigned.");
    }
    if (datasetIssue.getUser().getEmail() == null) {
      throw new IllegalArgumentException("User not assigned.");
    }
    if (datasetIssue.getType() == null && datasetIssue.getType().isEmpty()) {
      throw new IllegalArgumentException("Issue type not assigned.");
    }

    Dataset dataset;
    dataset = datasetFacade.findByPublicId(datasetIssue.getDataset().getPublicId());

    if (dataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }

    Users user = userFacade.findByEmail(datasetIssue.getUser().getEmail());
    if (user == null) {
      throw new IllegalArgumentException("User not found.");
    }

    DatasetIssue newDatasetIssue = new DatasetIssue(datasetIssue.getType(), datasetIssue.getMsg(), user, dataset);
    datasetIssueFacade.create(newDatasetIssue);
    LOGGER.log(Level.INFO, "Adding new dataset issue for dataset with public id: {0}.", datasetIssue.getDataset().
            getPublicId());
  }

  /**
   * Update dataset. Will overwrite categories, use addCategory to append.
   *
   * @param dataset
   */
  public void updateDataset(DatasetDTO dataset) {
    if (dataset == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (dataset.getPublicId() == null) {
      throw new IllegalArgumentException("Dataset id not assigned.");
    }

    Dataset manageddataset;
    manageddataset = datasetFacade.findByPublicId(dataset.getPublicId());
    if (manageddataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }

    if (dataset.getDescription() != null && !dataset.getDescription().equals(manageddataset.getDescription())) {
      manageddataset.setDescription(dataset.getDescription());
    }
    if (dataset.getReadme() != null && !dataset.getReadme().equals(manageddataset.getDescription())) {
      manageddataset.setReadme(dataset.getReadme());
    }
    if (dataset.getCategoryCollection() != null && !dataset.getCategoryCollection().isEmpty()) {
      manageddataset.setCategoryCollection(createCategoryCollection(dataset.getCategoryCollection()));
    }

    datasetFacade.edit(manageddataset);
    LOGGER.log(Level.INFO, "Update dataset with public id: {0}.", dataset.getPublicId());
  }

  /**
   * Add categories to dataset.
   *
   * @param dataset
   */
  public void addCategory(DatasetDTO dataset) {
    if (dataset == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (dataset.getPublicId() == null) {
      throw new IllegalArgumentException("Dataset id not assigned.");
    }

    Dataset manageddataset;
    manageddataset = datasetFacade.findByPublicId(dataset.getPublicId());
    if (manageddataset == null) {
      throw new IllegalArgumentException("Dataset not found.");
    }
    if (manageddataset.getCategoryCollection() == null || manageddataset.getCategoryCollection().isEmpty()) {
      manageddataset.setCategoryCollection(createCategoryCollection(dataset.getCategoryCollection()));
    } else {
      List<Category> categorysList = new ArrayList<>(manageddataset.getCategoryCollection());
      categorysList.addAll(createCategoryCollection(dataset.getCategoryCollection()));
      manageddataset.setCategoryCollection(categorysList);
    }
    datasetFacade.edit(manageddataset);
    LOGGER.log(Level.INFO, "Add category to dataset: {0}.", manageddataset.getId());
  }

  /**
   * Remove a dataset by id
   *
   * @param datasetId
   */
  public void removeDataset(Integer datasetId) {
    if (datasetId == null) {
      throw new IllegalArgumentException("Dataset id not assigned.");
    }
    Dataset manageddataset = datasetFacade.find(datasetId);
    datasetFacade.remove(manageddataset);
    LOGGER.log(Level.INFO, "Remove dataset with id: {0}.", datasetId);
  }

  /**
   * Remove a dataset by public id
   *
   * @param publicId
   */
  public void removeDataset(String publicId) {
    if (publicId == null) {
      throw new IllegalArgumentException("Dataset public id not assigned.");
    }
    Dataset manageddataset = datasetFacade.findByPublicId(publicId);
    datasetFacade.remove(manageddataset);
    LOGGER.log(Level.INFO, "Remove dataset with public id: {0}.", publicId);
  }

  private Collection<Category> createCategoryCollection(Collection<String> categoryCollection) {
    ArrayList<Category> categories = new ArrayList<>();
    if (categoryCollection == null || categoryCollection.isEmpty()) {
      return categories;
    }

    for (String c : categoryCollection) {
      categories.add(new Category(c));
    }

    return categories;
  }

  /**
   * Get all datasets
   *
   * @return
   */
  public List<Dataset> findAllDatasets() {
    return datasetFacade.findAll();
  }

  /**
   * Get dataset with the given id
   *
   * @param datasetId
   * @return
   */
  public Dataset findDataset(Integer datasetId) {
    return datasetFacade.find(datasetId);
  }

  /**
   * Get dataset with the given public id
   *
   * @param publicId
   * @return
   */
  public Dataset findDatasetByPublicId(String publicId) {
    return datasetFacade.findByPublicId(publicId);
  }

  /**
   * Get top 10 datasets
   *
   * @return
   * @throws IOException
   */
  public List<PopularDatasetJSON> getPopularDatasets() throws IOException {
    List<PopularDatasetJSON> popularDatasetsJsons = new LinkedList<>();
    for (PopularDataset pd : helperFunctions.getTopTenDatasets()) {
      ManifestJSON manifestJson = mapper.readValue(pd.getManifest(), ManifestJSON.class);
      List<AddressJSON> gvodEndpoints = mapper.readValue(pd.getPartners(), new TypeReference<List<AddressJSON>>() {
      });
      popularDatasetsJsons.add(new PopularDatasetJSON(manifestJson, pd.getDatasetId().getPublicId(), pd.getLeeches(),
              pd.getSeeds(),
              gvodEndpoints));
    }
    return popularDatasetsJsons;
  }

  public void addPopularDatasets(PopularDatasetJSON popularDatasetsJson) {
    if (popularDatasetsJson.getClusterId() != null && helperFunctions.ClusterRegisteredWithId(popularDatasetsJson.
            getClusterId())) {
      Dataset ds = dsFacade.findByPublicId(popularDatasetsJson.getDatasetId());
      if (ds == null) {
        throw new IllegalArgumentException("Invalid id.");
      }
      PopularDataset popularDataset;
      try {
        popularDataset
                = new PopularDataset(ds, mapper.writeValueAsString(popularDatasetsJson.
                        getManifestJson()), mapper.writeValueAsString(popularDatasetsJson.getGvodEndpoints()),
                        popularDatasetsJson.getLeeches(), popularDatasetsJson.getSeeds());
      } catch (JsonProcessingException ex) {
        throw new IllegalArgumentException("Invalid input.");
      }
      popularDatasetsFacade.create(popularDataset);
    }
  }
}
