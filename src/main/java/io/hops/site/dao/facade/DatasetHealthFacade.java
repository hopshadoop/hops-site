package io.hops.site.dao.facade;

import io.hops.site.controller.HopsSiteSettings;
import io.hops.site.dao.entity.DatasetHealth;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
@Stateless
public class DatasetHealthFacade extends AbstractFacade<DatasetHealth> {

  @PersistenceContext(unitName = "hops-sitePU")
  private EntityManager em;

  public DatasetHealthFacade() {
    super(DatasetHealth.class);
  }

  public DatasetHealthFacade(Class<DatasetHealth> entityClass) {
    super(entityClass);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public List<DatasetHealth> findByDatasetId(Integer datasetId) {
    TypedQuery<DatasetHealth> query = em.createNamedQuery("DatasetHealth.findByDatasetId", DatasetHealth.class)
      .setParameter("datasetId", datasetId);
    return query.getResultList();
  }

  public List<DatasetHealth> findByDatasetId(List<Integer> datasetIdList) {

    TypedQuery<DatasetHealth> query = em.createNamedQuery("DatasetHealth.findByDatasetIdList", DatasetHealth.class)
      .setParameter("datasetIdList", datasetIdList);
    return query.getResultList();
  }

  public int updateAllDatasetHealth() {
    //get timestamp 1h before starting the update
    Timestamp updated_now = (Timestamp) em.createNativeQuery("SELECT CURRENT_TIMESTAMP").getSingleResult();
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(updated_now.getTime());
    cal.add(Calendar.SECOND, (-1)*HopsSiteSettings.DATASET_HEALTH_OLDER_THAN);
    Timestamp old = new Timestamp(cal.getTime().getTime());
    //update
    int updates = em.createNativeQuery("INSERT INTO hops_site.dataset_health (dataset_id, status, count, updated) "
      + "SELECT ld.dataset_id, ld.status, COUNT(*), CURRENT_TIMESTAMP "
      + "FROM hops_site.live_dataset ld GROUP BY ld.dataset_id, ld.status "
      + "ON DUPLICATE KEY UPDATE count = VALUES(count), updated = VALUES(updated)")
      .executeUpdate();
    //delete old un-updated - as they are not live anymore
    updates += em.createNamedQuery("DatasetHealth.deleteOlderThan").setParameter("timestamp", old).executeUpdate();
    return updates;
  }
}
