package io.hops.site.dao.facade;

import io.hops.site.controller.HopsSiteSettings;
import io.hops.site.dao.entity.DatasetHealth;
import io.hops.site.dao.entity.LiveDataset;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
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

  @EJB
  private HopsSiteSettings hsettings;

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

  public void publishDataset(int datasetId) {
    DatasetHealth udh = find(new DatasetHealth.PK(datasetId, LiveDataset.Status.UPLOAD));
    DatasetHealth ddh = find(new DatasetHealth.PK(datasetId, LiveDataset.Status.DOWNLOAD));
    if (udh != null || ddh != null) {
      throw new IllegalStateException("publish exception in dataset health");
    }
    udh = new DatasetHealth(datasetId, LiveDataset.Status.UPLOAD, 1);
    create(udh);
    ddh = new DatasetHealth(datasetId, LiveDataset.Status.DOWNLOAD, 0);
    create(ddh);
  }

  public void unpublishDataset(int datasetId) {
    DatasetHealth udh = find(new DatasetHealth.PK(datasetId, LiveDataset.Status.UPLOAD));
    DatasetHealth ddh = find(new DatasetHealth.PK(datasetId, LiveDataset.Status.DOWNLOAD));
    remove(udh);
    remove(ddh);
  }

  public void downloadDataset(int datasetId) {
    DatasetHealth ddh = find(new DatasetHealth.PK(datasetId, LiveDataset.Status.DOWNLOAD));
    if (ddh == null) {
      throw new IllegalStateException("download exception in dataset health");
    }
    ddh.incCount();
  }

  public void downloadDatasets(Collection<Integer> datasetIds) {
    for (int datasetId : datasetIds) {
      DatasetHealth ddh = find(new DatasetHealth.PK(datasetId, LiveDataset.Status.DOWNLOAD));
      if (ddh == null) {
        throw new IllegalStateException("download exception in dataset health");
      }
      ddh.incCount();
    }
  }
  
  public void uploadDatasets(Collection<Integer> datasetIds) {
    for (int datasetId : datasetIds) {
      DatasetHealth udh = find(new DatasetHealth.PK(datasetId, LiveDataset.Status.UPLOAD));
      if (udh == null) {
        throw new IllegalStateException("upload exception in dataset health");
      }
      udh.incCount();
    }
  }

  public void completeDataset(LiveDataset ld) {
    DatasetHealth udh = find(new DatasetHealth.PK(ld.getId().getDatasetId(), LiveDataset.Status.UPLOAD));
    DatasetHealth ddh = find(new DatasetHealth.PK(ld.getId().getDatasetId(), LiveDataset.Status.DOWNLOAD));
    if (udh == null || ddh == null) {
      throw new IllegalStateException("upload exception in dataset health");
    }
    udh.incCount();
    if (ld.downloadStatus()) {
      ddh.decCount();
    }
  }

  public void removeDataset(LiveDataset ld) {
    if (ld.uploadStatus()) {
      DatasetHealth udh = find(new DatasetHealth.PK(ld.getId().getDatasetId(), LiveDataset.Status.UPLOAD));
      if (udh != null) {
        udh.decCount();
      }
    } else {
      DatasetHealth ddh = find(new DatasetHealth.PK(ld.getId().getDatasetId(), LiveDataset.Status.DOWNLOAD));
      if (ddh != null) {
        ddh.decCount();
      }
    }
  }

//  public int updateAllDatasetHealth() {
//    //get timestamp 1h before starting the update
//    Timestamp updated_now = (Timestamp) em.createNativeQuery("SELECT CURRENT_TIMESTAMP").getSingleResult();
//    Calendar cal = Calendar.getInstance();
//    cal.setTimeInMillis(updated_now.getTime());
//    cal.add(Calendar.SECOND, (-1)*hsettings.getDATASET_HEALTH_OLDER_THAN());
//    Timestamp old = new Timestamp(cal.getTime().getTime());
//    //update
//    int updates = em.createNativeQuery("INSERT INTO hops_site.dataset_health (dataset_id, status, count, updated) "
//      + "SELECT ld.dataset_id, ld.status, COUNT(*), CURRENT_TIMESTAMP "
//      + "FROM hops_site.live_dataset ld GROUP BY ld.dataset_id, ld.status "
//      + "ON DUPLICATE KEY UPDATE count = VALUES(count), updated = VALUES(updated)")
//      .executeUpdate();
//    //delete old un-updated - as they are not live anymore
//    updates += em.createNamedQuery("DatasetHealth.deleteOlderThan").setParameter("timestamp", old).executeUpdate();
//    return updates;
//  }
}
