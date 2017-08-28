package io.hops.site.dao.facade;

import io.hops.site.common.Settings;
import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.entity.LiveDataset;
import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dto.ClusterAddressDTO;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class LiveDatasetFacade extends AbstractFacade<LiveDataset> {

  @PersistenceContext(unitName = "hops-sitePU")
  private EntityManager em;

  @EJB
  private Settings settings;
  @EJB
  private RegisteredClusterFacade clusterFacade;
  @EJB
  private DatasetFacade datasetFacade;

  public LiveDatasetFacade() {
    super(LiveDataset.class);
  }

  public LiveDatasetFacade(Class<LiveDataset> entityClass) {
    super(entityClass);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public List<ClusterAddressDTO> datasetPeers(String datasetPublicId, int nrPeers) {
    List<ClusterAddressDTO> result = new LinkedList<>();
    Optional<Dataset> dataset = datasetFacade.findByPublicId(datasetPublicId);
    if (!dataset.isPresent()) {
      return result;
    }
    TypedQuery<LiveDataset> queryA = em.createNamedQuery("LiveDataset.datasetPeers", LiveDataset.class).
      setParameter("datasetId", dataset.get().getId()).setMaxResults(nrPeers);
    for (LiveDataset dp : queryA.getResultList()) {
      Optional<RegisteredCluster> cluster = clusterFacade.findById(dp.getId().getClusterId());
      if (cluster.isPresent()) {
        result.add(new ClusterAddressDTO(cluster.get().getPublicId(), cluster.get().getDelaEndpoint(), cluster.get().
          getHttpEndpoint()));
      } else {
        //cleanup? something is wrong with table
      }
    }
    return result;
  }

  public Optional<LiveDataset> connection(String datasetPublicId, String clusterPublicId) {
    Optional<Dataset> dataset = datasetFacade.findByPublicId(datasetPublicId);
    if (!dataset.isPresent()) {
      return Optional.empty();
    }
    Optional<RegisteredCluster> cluster = clusterFacade.findByPublicId(clusterPublicId);
    if (!cluster.isPresent()) {
      return Optional.empty();
    }
    TypedQuery<LiveDataset> query = em.createNamedQuery("LiveDataset.byId", LiveDataset.class).
      setParameter("datasetId", dataset.get().getId()).setParameter("clusterId", cluster.get().getId());
    try {
      LiveDataset c = query.getSingleResult();
      return Optional.of(c);
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }

  public void uploadDatasets(int clusterId, Collection<Integer> datasetIds) {
    for (Integer datasetId : datasetIds) {
      uploadDataset(clusterId, datasetId);
    }
  }

  public void downloadDatasets(int clusterId, Collection<Integer> datasetIds) {
    for (Integer datasetId : datasetIds) {
      downloadDataset(clusterId, datasetId);
    }
  }

  public void uploadDataset(int clusterId, int datasetId) {
    LiveDataset ld = find(new LiveDataset.PK(datasetId, clusterId));
    if (ld == null) {
      ld = new LiveDataset(datasetId, clusterId, settings.LIVE_DATASET_UPLOAD_STATUS);
      create(ld);
    } else if (ld.getStatus() != settings.LIVE_DATASET_UPLOAD_STATUS) {
      ld.setStatus(settings.LIVE_DATASET_UPLOAD_STATUS);
      edit(ld);
    }
  }

  public void downloadDataset(int clusterId, int datasetId) {
    LiveDataset ld = find(new LiveDataset.PK(datasetId, clusterId));
    if (ld == null) {
      ld = new LiveDataset(datasetId, clusterId, settings.LIVE_DATASET_DOWNLOAD_STATUS);
      create(ld);
    } else {
      //if status is upload - weird download status
      //if status is download - why update
    }
  }
}
