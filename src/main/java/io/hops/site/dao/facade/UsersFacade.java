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

import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dao.entity.Users;
import java.util.Optional;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class UsersFacade extends AbstractFacade<Users> {

  @PersistenceContext(unitName = "hops-sitePU")
  private EntityManager em;
  
  @EJB
  private RegisteredClusterFacade clusterFacade;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public UsersFacade() {
    super(Users.class);
  }

  public Optional<Users> findByEmailAndPublicClusterId(String email, String publicCId) {
    Optional<RegisteredCluster> cluster = clusterFacade.findByPublicId(publicCId);
    if(!cluster.isPresent()) {
      throw new IllegalArgumentException("Cluster not found.");
    }
    TypedQuery<Users> query = em.createNamedQuery("Users.findByEmailAndClusterId", Users.class)
            .setParameter("email", email)
            .setParameter("publicCId", publicCId);
    try {
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
