package io.hops.site.dao.admin.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.concurrent.Immutable;
import javax.annotation.security.RolesAllowed;


@Entity(name = "AdminDatasetStatus")
@Immutable
@RolesAllowed({"admin"})
@Table(name = "dataset_status")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AdminDatasetStatus.findAll",
      query = "SELECT d FROM AdminDatasetStatus d")
  ,
    @NamedQuery(name = "AdminDatasetStatus.findById",
      query = "SELECT d FROM AdminDatasetStatus d WHERE d.id = :id")
  ,
    @NamedQuery(name = "AdminDatasetStatus.findByStatus",
      query = "SELECT d FROM AdminDatasetStatus d WHERE d.status = :status")})
public class AdminDatasetStatus implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "id")
  private Integer id;
  @Size(max = 45)
  @Column(name = "status")
  private String status;
  @OneToMany(cascade = CascadeType.ALL,
      mappedBy = "status")
  private Collection<AdminLiveDataset> liveDatasetCollection;
  @OneToMany(cascade = CascadeType.ALL,
      mappedBy = "datasetStatus")
  private Collection<AdminDatasetHealth> datasetHealthCollection;

  public AdminDatasetStatus() {
  }

  public AdminDatasetStatus(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<AdminLiveDataset> getLiveDatasetCollection() {
    return liveDatasetCollection;
  }

  public void setLiveDatasetCollection(Collection<AdminLiveDataset> liveDatasetCollection) {
    this.liveDatasetCollection = liveDatasetCollection;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<AdminDatasetHealth> getDatasetHealthCollection() {
    return datasetHealthCollection;
  }

  public void setDatasetHealthCollection(Collection<AdminDatasetHealth> datasetHealthCollection) {
    this.datasetHealthCollection = datasetHealthCollection;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AdminDatasetStatus)) {
      return false;
    }
    AdminDatasetStatus other = (AdminDatasetStatus) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.admin.entity.DatasetStatus[ id=" + id + " ]";
  }
  
}
