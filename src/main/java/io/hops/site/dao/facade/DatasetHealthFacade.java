package io.hops.site.dao.facade;

import io.hops.site.dao.entity.DatasetHealth;
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

  public List<DatasetHealth> findByDatasetId(List<Integer> datasetIdList) {

    TypedQuery<DatasetHealth> query = em.createNamedQuery("DatasetHealth.findByPublicIdList", DatasetHealth.class)
      .setParameter("datasetIdList", datasetIdList);
    return query.getResultList();
  }
  
  public int updateAllDatasetHealth() {
    int updates = em.createNativeQuery("INSERT INTO hops_site.dataset_health (dataset_id, status, count) "
      + "SELECT hops_site.dataset_id, hops_site.status, COUNT(*)"
      + "FROM hops_site.live_dataset ld GROUP BY hops_site.dataset_id, hops_site.status "
      + "ON DUPLICATE KEY UPDATE count = VALUES(count)").executeUpdate();
    return updates;
  }
}
