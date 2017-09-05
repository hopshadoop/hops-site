package io.hops.site.dao.facade;

import io.hops.site.dao.entity.DatasetHealth;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

  public Optional<DatasetHealth> findByDatasetId(Integer datasetId) {
    TypedQuery<DatasetHealth> query = em.createNamedQuery("DatasetHealth.findByPublicId", DatasetHealth.class)
      .setParameter("datasetId", datasetId);
    try {
      return Optional.of(query.getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }

  public List<DatasetHealth> findByDatasetId(List<Integer> datasetIdList) {

    TypedQuery<DatasetHealth> query = em.createNamedQuery("DatasetHealth.findByPublicIdList", DatasetHealth.class)
      .setParameter("datasetIdList", datasetIdList);
    return query.getResultList();
  }

  public int updateAllDatasetHealth() {
    int updates = em.createNativeQuery("INSERT INTO hops_site.dataset_health (dataset_id, status, count) "
      + "SELECT ld.dataset_id, ld.status, COUNT(*)"
      + "FROM hops_site.live_dataset ld GROUP BY ld.dataset_id, ld.status "
      + "ON DUPLICATE KEY UPDATE count = VALUES(count)").executeUpdate();
    return updates;
  }
}
