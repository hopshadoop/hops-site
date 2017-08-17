package io.hops.site.dao.facade;

import io.hops.site.common.Settings;
import io.hops.site.dao.entity.Heartbeat;
import java.util.Date;
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
  private Settings settings;
  
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
  
  public int deleteHeartbeats(Date olderThan) {
    TypedQuery<Heartbeat> query = em.createNamedQuery("Dataset.deleteOlderThan", Heartbeat.class).setParameter("date",
      olderThan);
    int updates = query.executeUpdate();
    return updates;
  }
}
