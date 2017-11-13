package io.hops.site.dao.facade;

import io.hops.site.controller.HopsSiteSettings;
import io.hops.site.dao.entity.Heartbeat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
@Stateless
public class HeartbeatFacade extends AbstractFacade<Heartbeat> {

  @PersistenceContext(unitName = "hops-sitePU")
  private EntityManager em;

  @EJB 
  private HopsSiteSettings settings;
  
  public HeartbeatFacade() {
    super(Heartbeat.class);
  }

  public HeartbeatFacade(Class<Heartbeat> entityClass) {
    super(entityClass);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public Optional<Heartbeat> findByClusterId(int clusterId) {
    return Optional.ofNullable(find(clusterId));
  }
  
  public List<Heartbeat> findHeartbeats(Date olderThan) {
    TypedQuery<Heartbeat> query = em.createNamedQuery("Heartbeat.findOlderThan", Heartbeat.class)
      .setParameter("date", olderThan);
    return query.getResultList();
  }
}
