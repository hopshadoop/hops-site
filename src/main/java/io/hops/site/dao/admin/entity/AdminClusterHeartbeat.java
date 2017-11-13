package io.hops.site.dao.admin.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.concurrent.Immutable;
import javax.annotation.security.RolesAllowed;

@Entity(name = "AdminClusterHeartbeat")
@Immutable
@RolesAllowed({"admin"})
@Table(name = "cluster_heartbeat")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AdminClusterHeartbeat.findAll",
      query = "SELECT c FROM AdminClusterHeartbeat c")
  ,
    @NamedQuery(name = "AdminClusterHeartbeat.findByClusterId",
      query
      = "SELECT c FROM AdminClusterHeartbeat c WHERE c.clusterId = :clusterId")
  ,
    @NamedQuery(name = "AdminClusterHeartbeat.findByDateLastPing",
      query
      = "SELECT c FROM AdminClusterHeartbeat c WHERE c.dateLastPing = :dateLastPing")})
public class AdminClusterHeartbeat implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "cluster_id")
  private Integer clusterId;
  @Basic(optional = false)
  @NotNull
  @Column(name = "date_last_ping")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateLastPing;
  @OneToMany(cascade = CascadeType.ALL,
      mappedBy = "clusterHeartbeat")
  private Collection<AdminLiveDataset> liveDatasetCollection;
  @JoinColumn(name = "cluster_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  @OneToOne(optional = false)
  private AdminRegisteredCluster registeredCluster;

  public AdminClusterHeartbeat() {
  }

  public AdminClusterHeartbeat(Integer clusterId) {
    this.clusterId = clusterId;
  }

  public AdminClusterHeartbeat(Integer clusterId, Date dateLastPing) {
    this.clusterId = clusterId;
    this.dateLastPing = dateLastPing;
  }

  public Integer getClusterId() {
    return clusterId;
  }

  public void setClusterId(Integer clusterId) {
    this.clusterId = clusterId;
  }

  public Date getDateLastPing() {
    return dateLastPing;
  }

  public void setDateLastPing(Date dateLastPing) {
    this.dateLastPing = dateLastPing;
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
  public AdminRegisteredCluster getRegisteredCluster() {
    return registeredCluster;
  }

  public void setRegisteredCluster(AdminRegisteredCluster registeredCluster) {
    this.registeredCluster = registeredCluster;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (clusterId != null ? clusterId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AdminClusterHeartbeat)) {
      return false;
    }
    AdminClusterHeartbeat other = (AdminClusterHeartbeat) object;
    if ((this.clusterId == null && other.clusterId != null) ||
        (this.clusterId != null && !this.clusterId.equals(other.clusterId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "AdminClusterHeartbeat{" + "clusterId=" + clusterId + '}';
  }
  
}
