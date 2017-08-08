/*
 * Copyright 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hops.site.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "registered_cluster",
        catalog = "hops_site",
        schema = "")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "RegisteredCluster.findAll",
          query = "SELECT r FROM RegisteredCluster r"),
  @NamedQuery(name = "RegisteredCluster.findByClusterId",
          query
          = "SELECT r FROM RegisteredCluster r WHERE r.clusterId = :clusterId"),
  @NamedQuery(name = "RegisteredCluster.findBySearchEndpoint",
          query
          = "SELECT r FROM RegisteredCluster r WHERE r.searchEndpoint = :searchEndpoint"),
  @NamedQuery(name = "RegisteredCluster.findByEmail",
          query
          = "SELECT r FROM RegisteredCluster r WHERE r.email = :email"),
  @NamedQuery(name = "RegisteredCluster.findByCert",
          query
          = "SELECT r FROM RegisteredCluster r WHERE r.cert = :cert"),
  @NamedQuery(name = "RegisteredCluster.findByGvodEndpoint",
          query
          = "SELECT r FROM RegisteredCluster r WHERE r.gvodEndpoint = :gvodEndpoint"),
  @NamedQuery(name = "RegisteredCluster.findByHeartbeatsMissed",
          query
          = "SELECT r FROM RegisteredCluster r WHERE r.heartbeatsMissed = :heartbeatsMissed"),
  @NamedQuery(name = "RegisteredCluster.findByDateRegistered",
          query
          = "SELECT r FROM RegisteredCluster r WHERE r.dateRegistered = :dateRegistered"),
  @NamedQuery(name = "RegisteredCluster.findByDateLastPing",
          query
          = "SELECT r FROM RegisteredCluster r WHERE r.dateLastPing = :dateLastPing")})
public class RegisteredCluster implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 200)
  @Column(name = "cluster_id")
  private String clusterId;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 100)
  @Column(name = "search_endpoint")
  private String searchEndpoint;
  @Pattern(regexp
          = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
          message = "Invalid email")
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 100)
  @Column(name = "email")
  private String email;
  @Basic(optional = false)
  @NotNull
  @Lob
  @Column(name = "cert")
  private byte[] cert;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "gvod_endpoint")
  private String gvodEndpoint;
  @Basic(optional = false)
  @NotNull
  @Column(name = "heartbeats_missed")
  private long heartbeatsMissed;
  @Column(name = "date_registered")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateRegistered;
  @Column(name = "date_last_ping")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateLastPing;
  @OneToMany(mappedBy = "clusterId")
  private Collection<Dataset> datasetCollection;
  @OneToMany(cascade = CascadeType.ALL,
          mappedBy = "clusterId")
  private Collection<Users> usersCollection;

  public RegisteredCluster() {
  }

  public RegisteredCluster(String clusterId) {
    this.clusterId = clusterId;
  }

  public RegisteredCluster(String clusterId, String searchEndpoint, String email, byte[] cert, String gvodEndpoint,
          long heartbeatsMissed) {
    this.clusterId = clusterId;
    this.searchEndpoint = searchEndpoint;
    this.email = email;
    this.cert = cert;
    this.gvodEndpoint = gvodEndpoint;
    this.heartbeatsMissed = heartbeatsMissed;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getSearchEndpoint() {
    return searchEndpoint;
  }

  public void setSearchEndpoint(String searchEndpoint) {
    this.searchEndpoint = searchEndpoint;
  }

  @XmlTransient
  @JsonIgnore
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public byte[] getCert() {
    return cert;
  }

  public void setCert(byte[] cert) {
    this.cert = cert;
  }
  
  public String getGvodEndpoint() {
    return gvodEndpoint;
  }

  public void setGvodEndpoint(String gvodEndpoint) {
    this.gvodEndpoint = gvodEndpoint;
  }

  public long getHeartbeatsMissed() {
    return heartbeatsMissed;
  }

  public void setHeartbeatsMissed(long heartbeatsMissed) {
    this.heartbeatsMissed = heartbeatsMissed;
  }

  public Date getDateRegistered() {
    return dateRegistered;
  }

  public void setDateRegistered(Date dateRegistered) {
    this.dateRegistered = dateRegistered;
  }

  public Date getDateLastPing() {
    return dateLastPing;
  }

  public void setDateLastPing(Date dateLastPing) {
    this.dateLastPing = dateLastPing;
  }

  @XmlTransient
  public Collection<Dataset> getDatasetCollection() {
    return datasetCollection;
  }

  public void setDatasetCollection(Collection<Dataset> datasetCollection) {
    this.datasetCollection = datasetCollection;
  }

  @XmlTransient
  public Collection<Users> getUsersCollection() {
    return usersCollection;
  }

  public void setUsersCollection(Collection<Users> usersCollection) {
    this.usersCollection = usersCollection;
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
    if (!(object instanceof RegisteredCluster)) {
      return false;
    }
    RegisteredCluster other = (RegisteredCluster) object;
    if ((this.clusterId == null && other.clusterId != null) || (this.clusterId != null && !this.clusterId.equals(
            other.clusterId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.entity.RegisteredCluster[ clusterId=" + clusterId + " ]";
  }

}
