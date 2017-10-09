package io.hops.site.dao.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alex Ormenisan <aaor@kth.se>
 */
@Entity
@Table(name = "live_dataset",
  catalog = "hops_site",
  schema = "")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "LiveDataset.datasetPeers",
    query = "SELECT ld FROM LiveDataset ld WHERE ld.id.datasetId = :datasetId"),
  @NamedQuery(name = "LiveDataset.peerDatasets",
    query = "SELECT ld FROM LiveDataset ld WHERE ld.id.clusterId = :clusterId ORDER BY ld.heartbeat.lastPinged"),
  @NamedQuery(name = "LiveDataset.byId",
    query = "SELECT ld FROM LiveDataset ld WHERE ld.id.datasetId = :datasetId AND ld.id.clusterId = :clusterId")
})
public class LiveDataset implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private PK id;

  @Column(name = "status")
  private int status;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cluster_id", insertable=false, updatable=false)
  private Heartbeat heartbeat;

  public LiveDataset() {
  }

  public LiveDataset(int datasetId, int clusterId, int status) {
    this.id = new PK(datasetId, clusterId);
    this.status = status;
  }

  public PK getId() {
    return id;
  }

  public void setId(PK id) {
    this.id = id;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Embeddable
  public static class PK implements Serializable {

    @Column(name = "dataset_id")
    @NotNull
    private int datasetId;
    @Column(name = "cluster_id")
    @NotNull
    private int clusterId;

    public PK() {
    }

    public PK(int datasetId, int clusterId) {
      this.datasetId = datasetId;
      this.clusterId = clusterId;
    }

    public int getDatasetId() {
      return datasetId;
    }

    public void setDatasetId(int datasetId) {
      this.datasetId = datasetId;
    }

    public int getClusterId() {
      return clusterId;
    }

    public void setClusterId(int clusterId) {
      this.clusterId = clusterId;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 41 * hash + Objects.hashCode(this.datasetId);
      hash = 41 * hash + Objects.hashCode(this.clusterId);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final PK other = (PK) obj;
      if (!Objects.equals(this.datasetId, other.datasetId)) {
        return false;
      }
      if (!Objects.equals(this.clusterId, other.clusterId)) {
        return false;
      }
      return true;
    }
  }
}
