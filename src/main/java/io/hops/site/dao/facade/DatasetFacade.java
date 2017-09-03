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
import io.hops.site.dao.entity.RegisteredCluster;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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

  public Collection<Integer> findIds(Collection<String> publicIdList) {
    Collection<Dataset> datasets = findByPublicId(publicIdList);
    List<Integer> datasetIds = new LinkedList<>();
    for(Dataset dataset : datasets) {
      datasetIds.add(dataset.getId());
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

  public Dataset createDataset(String publicId, String name, String description, String readmePath,
    Collection<Category> categories, RegisteredCluster cluster) {
    create(new Dataset(publicId, name, description, readmePath, categories, cluster));
    Optional<Dataset> d = findByPublicId(publicId);
    return d.get();
  }
}
