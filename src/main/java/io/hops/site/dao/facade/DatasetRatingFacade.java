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

import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.entity.DatasetRating;
import io.hops.site.dao.entity.Users;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class DatasetRatingFacade extends AbstractFacade<DatasetRating> {

  @PersistenceContext(unitName = "hops-sitePU")
  private EntityManager em;

  public DatasetRatingFacade() {
    super(DatasetRating.class);
  }

  public DatasetRatingFacade(Class<DatasetRating> entityClass) {
    super(entityClass);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public DatasetRating findByDatasetAndUser(Dataset dataset, Users user) {
    TypedQuery<DatasetRating> query = em.createNamedQuery("DatasetRating.findByDatasetAndUser", DatasetRating.class)
            .setParameter("publicId", dataset.getPublicId()).setParameter("userId", user.getId());
    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
