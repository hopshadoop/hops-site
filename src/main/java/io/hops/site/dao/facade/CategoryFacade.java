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
public class CategoryFacade extends AbstractFacade<Category> {

  @PersistenceContext(unitName = "hops-sitePU")
  private EntityManager em;

  public CategoryFacade() {
    super(Category.class);
  }

  public CategoryFacade(Class<Category> entityClass) {
    super(entityClass);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public Collection<Category> getAndStoreCategories(Collection<String> categoryNames) {
    List<Category> categories = new LinkedList<>();
    if (categoryNames == null || categoryNames.isEmpty()) {
      return categories;
    }

    for (String categoryName : categoryNames) {
      Optional<Category> c = getByName(categoryName);
      if (!c.isPresent()) {
        create(new Category(categoryName));
        c = getByName(categoryName);
      }
      categories.add(c.get());
    }

    return categories;
  }

  public Optional<Category> getByName(String category) {
    TypedQuery<Category> query = em.createNamedQuery(Category.FIND_BY_NAME, Category.class)
      .setParameter(Category.NAME, category);
    try {
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
