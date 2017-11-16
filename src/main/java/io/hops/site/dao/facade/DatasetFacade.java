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
package io.hops.site.dao.facade;

import io.hops.site.dao.entity.Category;
import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.entity.Users;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class DatasetFacade extends AbstractFacade<Dataset> {

  @PersistenceContext(unitName = "hops-sitePU")
  private EntityManager em;

  public DatasetFacade() {
    super(Dataset.class);
  }

  public DatasetFacade(Class<Dataset> entityClass) {
    super(entityClass);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public Optional<Dataset> findByPublicId(String publicId) {
    TypedQuery<Dataset> query = em.createNamedQuery(Dataset.FIND_BY_PUBLIC_ID, Dataset.class)
      .setParameter(Dataset.PUBLIC_ID, publicId);
    try {
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  public Map<String, Integer> findIds(Collection<String> publicIdList) {
    List<Dataset> datasets = findByPublicId(publicIdList);
    Map<String, Integer> datasetIds = new HashMap<>();
    for(Dataset dataset : datasets) {
      datasetIds.put(dataset.getPublicId(), dataset.getId());
    }
    return datasetIds;
  }
  
  public List<Dataset> findByPublicId(Collection<String> publicIdList) {
    if(publicIdList.isEmpty()) {
      return new LinkedList<>();
    }
    TypedQuery<Dataset> query = em.createNamedQuery(Dataset.FIND_BY_PUBLIC_ID_LIST, Dataset.class)
      .setParameter(Dataset.PUBLIC_ID_LIST, publicIdList);
    return query.getResultList();
  }

  public Dataset createDataset(String publicId, String datasetName, int version, String description, 
    String readmePath, Collection<Category> categories, Users owner, long size) {
    create(new Dataset(publicId, datasetName, version, description, readmePath, categories, owner, size));
    em.flush();
    Optional<Dataset> d = findByPublicId(publicId);
    return d.get();
  }
  
  public List<Dataset> findSimilar(String datasetName) {
    TypedQuery<Dataset> query = em.createNamedQuery(Dataset.FIND_SIMILAR, Dataset.class)
      .setParameter(Dataset.DATASET_NAME, datasetName);
    return query.getResultList();
  }
}
