package io.hops.site.dao.admin.facade;

import io.hops.site.dao.admin.entity.AdminRegisteredCluster;
import io.hops.site.dao.facade.AbstractFacade;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
@RolesAllowed({"admin"})
public class AdminRegisteredClusterFacade extends AbstractFacade<AdminRegisteredCluster> {

  @PersistenceContext(unitName = "hops-sitePU")
  private EntityManager em;

  public AdminRegisteredClusterFacade() {
    super(AdminRegisteredCluster.class);
  }

  public AdminRegisteredClusterFacade(Class<AdminRegisteredCluster> entityClass) {
    super(entityClass);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public Optional<AdminRegisteredCluster> findByOrgName(String orgName) {
    try {
      AdminRegisteredCluster cluster = em.createNamedQuery("AdminRegisteredCluster.findByOrgName",
          AdminRegisteredCluster.class).setParameter("orgName", orgName).getSingleResult();
      return Optional.of(cluster);
    } catch (NoResultException nre) {
      return Optional.empty();
    }
  }

}
