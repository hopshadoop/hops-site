package io.hops.site.dao.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity(name = "AdminLiveDataset")
@Immutable
@RolesAllowed({"admin"})
@Table(name = "live_dataset")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AdminLiveDataset.findAll",
      query = "SELECT l FROM AdminLiveDataset l")
  ,
    @NamedQuery(name = "AdminLiveDataset.findByDatasetId",
      query
      = "SELECT l FROM AdminLiveDataset l WHERE l.liveDatasetPK.datasetId = :datasetId")
  ,
    @NamedQuery(name = "AdminLiveDataset.findByClusterId",
      query
      = "SELECT l FROM AdminLiveDataset l WHERE l.liveDatasetPK.clusterId = :clusterId")})
public class AdminLiveDataset implements Serializable {

  private static final long serialVersionUID = 1L;
  @EmbeddedId
  protected AdminLiveDatasetPK liveDatasetPK;
  @JoinColumn(name = "dataset_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  @ManyToOne(optional = false)
  private AdminDataset dataset;
  @JoinColumn(name = "cluster_id",
      referencedColumnName = "cluster_id",
      insertable = false,
      updatable = false)
  @ManyToOne(optional = false)
  private AdminClusterHeartbeat clusterHeartbeat;
  @JoinColumn(name = "status",
      referencedColumnName = "id")
  @ManyToOne(optional = false)
  private AdminDatasetStatus status;

  public AdminLiveDataset() {
  }

  public AdminLiveDataset(AdminLiveDatasetPK liveDatasetPK) {
    this.liveDatasetPK = liveDatasetPK;
  }

  public AdminLiveDataset(int datasetId, int clusterId) {
    this.liveDatasetPK = new AdminLiveDatasetPK(datasetId, clusterId);
  }

  public AdminLiveDatasetPK getLiveDatasetPK() {
    return liveDatasetPK;
  }

  public void setLiveDatasetPK(AdminLiveDatasetPK liveDatasetPK) {
    this.liveDatasetPK = liveDatasetPK;
  }

  @XmlTransient
  @JsonIgnore
  public AdminDataset getDataset() {
    return dataset;
  }

  public void setDataset(AdminDataset dataset) {
    this.dataset = dataset;
  }

  public AdminClusterHeartbeat getClusterHeartbeat() {
    return clusterHeartbeat;
  }

  public void setClusterHeartbeat(AdminClusterHeartbeat clusterHeartbeat) {
    this.clusterHeartbeat = clusterHeartbeat;
  }

  public AdminDatasetStatus getStatus() {
    return status;
  }

  public void setStatus(AdminDatasetStatus status) {
    this.status = status;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (liveDatasetPK != null ? liveDatasetPK.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AdminLiveDataset)) {
      return false;
    }
    AdminLiveDataset other = (AdminLiveDataset) object;
    if ((this.liveDatasetPK == null && other.liveDatasetPK != null) || (this.liveDatasetPK != null
        && !this.liveDatasetPK.equals(other.liveDatasetPK))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.admin.entity.LiveDataset[ liveDatasetPK=" + liveDatasetPK + " ]";
  }

}
