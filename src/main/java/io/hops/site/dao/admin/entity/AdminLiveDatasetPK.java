package io.hops.site.dao.admin.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;


@Embeddable
public class AdminLiveDatasetPK implements Serializable {

  @Basic(optional = false)
  @NotNull
  @Column(name = "dataset_id")
  private int datasetId;
  @Basic(optional = false)
  @NotNull
  @Column(name = "cluster_id")
  private int clusterId;

  public AdminLiveDatasetPK() {
  }

  public AdminLiveDatasetPK(int datasetId, int clusterId) {
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
    int hash = 0;
    hash += (int) datasetId;
    hash += (int) clusterId;
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AdminLiveDatasetPK)) {
      return false;
    }
    AdminLiveDatasetPK other = (AdminLiveDatasetPK) object;
    if (this.datasetId != other.datasetId) {
      return false;
    }
    if (this.clusterId != other.clusterId) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.admin.entity.LiveDatasetPK[ datasetId=" + datasetId + ", clusterId=" +
        clusterId + " ]";
  }
  
}
