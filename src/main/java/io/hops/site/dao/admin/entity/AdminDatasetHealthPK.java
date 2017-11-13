package io.hops.site.dao.admin.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;


@Embeddable
public class AdminDatasetHealthPK implements Serializable {

  @Basic(optional = false)
  @NotNull
  @Column(name = "dataset_id")
  private int datasetId;
  @Basic(optional = false)
  @NotNull
  @Column(name = "status")
  private int status;

  public AdminDatasetHealthPK() {
  }

  public AdminDatasetHealthPK(int datasetId, int status) {
    this.datasetId = datasetId;
    this.status = status;
  }

  public int getDatasetId() {
    return datasetId;
  }

  public void setDatasetId(int datasetId) {
    this.datasetId = datasetId;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (int) datasetId;
    hash += (int) status;
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AdminDatasetHealthPK)) {
      return false;
    }
    AdminDatasetHealthPK other = (AdminDatasetHealthPK) object;
    if (this.datasetId != other.datasetId) {
      return false;
    }
    if (this.status != other.status) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.admin.entity.DatasetHealthPK[ datasetId=" + datasetId + ", status=" + status +
        " ]";
  }
  
}
