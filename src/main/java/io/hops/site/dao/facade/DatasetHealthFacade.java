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
      + "SELECT ld.dataset_id, ld.status, COUNT(*) as ld_count "
      + "FROM hops_site.live_dataset ld GROUP BY ld.dataset_id, ld.status "
      + "ON DUPLICATE KEY UPDATE count = ld_count").executeUpdate();
    return updates;
  }
}
