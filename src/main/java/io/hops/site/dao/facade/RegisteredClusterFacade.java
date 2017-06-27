package io.hops.site.dao.facade;

import io.hops.site.dao.entity.RegisteredCluster;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
public class RegisteredClusterFacade extends AbstractFacade<RegisteredCluster> {

  @PersistenceContext(unitName = "hops-sitePU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public RegisteredClusterFacade() {
    super(RegisteredCluster.class);
  }

  public RegisteredCluster findByEmail(String email) {
    try {
      return em.createNamedQuery("RegisteredCluster.findByEmail", RegisteredCluster.class).setParameter("email", email).
              getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

}
