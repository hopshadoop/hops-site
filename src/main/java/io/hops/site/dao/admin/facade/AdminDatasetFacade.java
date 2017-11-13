package io.hops.site.dao.admin.facade;

import io.hops.site.dao.admin.entity.AdminDataset;
import io.hops.site.dao.facade.AbstractFacade;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@RolesAllowed({"admin"})
public class AdminDatasetFacade extends AbstractFacade<AdminDataset> {

  @PersistenceContext(unitName = "hops-sitePU")
  private EntityManager em;

  public AdminDatasetFacade() {
    super(AdminDataset.class);
  }

  public AdminDatasetFacade(Class<AdminDataset> entityClass) {
    super(entityClass);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  
}
