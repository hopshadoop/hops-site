package io.hops.site.dao.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.concurrent.Immutable;
import javax.annotation.security.RolesAllowed;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity(name = "AdminDatasetHealth")
@Immutable
@RolesAllowed({"admin"})
@Table(name = "dataset_health")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AdminDatasetHealth.findAll",
      query = "SELECT d FROM AdminDatasetHealth d")
  ,
    @NamedQuery(name = "AdminDatasetHealth.findByDatasetId",
      query
      = "SELECT d FROM AdminDatasetHealth d WHERE d.datasetHealthPK.datasetId = :datasetId")
  ,
    @NamedQuery(name = "AdminDatasetHealth.findByStatus",
      query
      = "SELECT d FROM AdminDatasetHealth d WHERE d.datasetHealthPK.status = :status")
  ,
    @NamedQuery(name = "AdminDatasetHealth.findByCount",
      query = "SELECT d FROM AdminDatasetHealth d WHERE d.count = :count")
  ,
    @NamedQuery(name = "AdminDatasetHealth.findByUpdated",
      query = "SELECT d FROM AdminDatasetHealth d WHERE d.updated = :updated")})
public class AdminDatasetHealth implements Serializable {

  private static final long serialVersionUID = 1L;
  @EmbeddedId
  protected AdminDatasetHealthPK datasetHealthPK;
  @Basic(optional = false)
  @NotNull
  @Column(name = "count")
  private int count;
  @Basic(optional = false)
  @NotNull
  @Column(name = "updated")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updated;
  @JoinColumn(name = "dataset_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  @ManyToOne(optional = false)
  private AdminDataset dataset;
  @JoinColumn(name = "status",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  @ManyToOne(optional = false)
  private AdminDatasetStatus datasetStatus;

  public AdminDatasetHealth() {
  }

  public AdminDatasetHealth(AdminDatasetHealthPK datasetHealthPK) {
    this.datasetHealthPK = datasetHealthPK;
  }

  public AdminDatasetHealth(AdminDatasetHealthPK datasetHealthPK, int count, Date updated) {
    this.datasetHealthPK = datasetHealthPK;
    this.count = count;
    this.updated = updated;
  }

  public AdminDatasetHealth(int datasetId, int status) {
    this.datasetHealthPK = new AdminDatasetHealthPK(datasetId, status);
  }

  public AdminDatasetHealthPK getDatasetHealthPK() {
    return datasetHealthPK;
  }

  public void setDatasetHealthPK(AdminDatasetHealthPK datasetHealthPK) {
    this.datasetHealthPK = datasetHealthPK;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  @XmlTransient
  @JsonIgnore
  public AdminDataset getDataset() {
    return dataset;
  }

  public void setDataset(AdminDataset dataset) {
    this.dataset = dataset;
  }

  public AdminDatasetStatus getDatasetStatus() {
    return datasetStatus;
  }

  public void setDatasetStatus(AdminDatasetStatus datasetStatus) {
    this.datasetStatus = datasetStatus;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (datasetHealthPK != null ? datasetHealthPK.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AdminDatasetHealth)) {
      return false;
    }
    AdminDatasetHealth other = (AdminDatasetHealth) object;
    if ((this.datasetHealthPK == null && other.datasetHealthPK != null) || (this.datasetHealthPK != null
        && !this.datasetHealthPK.equals(other.datasetHealthPK))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.admin.entity.DatasetHealth[ datasetHealthPK=" + datasetHealthPK + " ]";
  }

}
