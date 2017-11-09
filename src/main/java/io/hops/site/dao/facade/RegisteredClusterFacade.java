package io.hops.site.dao.facade;

import io.hops.site.dao.entity.RegisteredCluster;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

  public Optional<RegisteredCluster> findBySubject(String subject) {
    TypedQuery<RegisteredCluster> query = em.
        createNamedQuery("RegisteredCluster.findBySubject", RegisteredCluster.class)
        .setParameter(RegisteredCluster.SUBJECT, subject);
    try {
      RegisteredCluster cluster = query.getSingleResult();
      return Optional.of(cluster);
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }

  public Optional<RegisteredCluster> findByPublicId(String publicId) {
    try {
      RegisteredCluster cluster = em.createNamedQuery("RegisteredCluster.findByPublicId", RegisteredCluster.class)
          .setParameter("publicId", publicId).getSingleResult();
      return Optional.of(cluster);
    } catch (NoResultException nre) {
      return Optional.empty();
    }
  }

  public Optional<RegisteredCluster> findById(int clusterId) {
    try {
      RegisteredCluster cluster = em.createNamedQuery("RegisteredCluster.findById", RegisteredCluster.class)
          .setParameter("id", clusterId).getSingleResult();
      return Optional.of(cluster);
    } catch (NoResultException nre) {
      return Optional.empty();
    }
  }

  public Optional<RegisteredCluster> findByOrgName(String orgName) {
    try {
      RegisteredCluster cluster = em.createNamedQuery("RegisteredCluster.findByOrgName", RegisteredCluster.class)
          .setParameter("orgName", orgName).getSingleResult();
      return Optional.of(cluster);
    } catch (NoResultException nre) {
      return Optional.empty();
    }
  }
}
