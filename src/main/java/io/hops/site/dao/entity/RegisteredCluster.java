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
import javax.persistence.FetchType;
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
   @NamedQuery(name = "RegisteredCluster.findById",
    query = "SELECT r FROM RegisteredCluster r WHERE r.id = :id"),
  @NamedQuery(name = "RegisteredCluster.findByPublicId",
    query = "SELECT r FROM RegisteredCluster r WHERE r.publicId = :publicId"),
  @NamedQuery(name = "RegisteredCluster.findBySearchEndpoint",
    query = "SELECT r FROM RegisteredCluster r WHERE r.httpEndpoint = :httpEndpoint"),
  @NamedQuery(name = "RegisteredCluster.findByEmail",
    query = "SELECT r FROM RegisteredCluster r WHERE r.email = :email"),
  @NamedQuery(name = "RegisteredCluster.findByCert",
    query = "SELECT r FROM RegisteredCluster r WHERE r.cert = :cert"),
  @NamedQuery(name = "RegisteredCluster.findByDelaEndpoint",
    query = "SELECT r FROM RegisteredCluster r WHERE r.delaEndpoint = :delaEndpoint"),
  @NamedQuery(name = "RegisteredCluster.findByDateRegistered",
    query = "SELECT r FROM RegisteredCluster r WHERE r.dateRegistered = :dateRegistered")})
public class RegisteredCluster implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @NotNull
  @Column(name = "id")
  private int id;
  @NotNull
  @Basic(optional = false)
  @Size(min = 1,
    max = 200)
  @Column(name = "public_id")
  private String publicId;
  @NotNull
  @Basic(optional = false)
  @Size(min = 1,
    max = 100)
  @Column(name = "http_endpoint")
  private String httpEndpoint;
  @Pattern(regexp
    = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
    message = "Invalid email")
  @NotNull
  @Basic(optional = false)
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
  @Size(min = 1,
    max = 100)
  @Column(name = "dela_endpoint")
  private String delaEndpoint;
  @Column(name = "date_registered")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateRegistered;
  @OneToMany(fetch = FetchType.LAZY,
    mappedBy = "ownerCluster")
  private Collection<Dataset> datasetCollection;
  @OneToMany(fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "cluster")
  private Collection<Users> usersCollection;

  public RegisteredCluster() {
  }

  public RegisteredCluster(String publicId, String httpEndpoint, String email, byte[] cert, String delaEndpoint) {
    this.publicId = publicId;
    this.httpEndpoint = httpEndpoint;
    this.email = email;
    this.cert = cert;
    this.delaEndpoint = delaEndpoint;
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

  public Date getDateRegistered() {
    return dateRegistered;
  }

  public void setDateRegistered(Date dateRegistered) {
    this.dateRegistered = dateRegistered;
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
    hash += (publicId != null ? publicId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof RegisteredCluster)) {
      return false;
    }
    RegisteredCluster other = (RegisteredCluster) object;
    if ((this.publicId == null && other.publicId != null) || (this.publicId != null && !this.publicId.equals(
      other.publicId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.entity.RegisteredCluster[ clusterId=" + publicId + " ]";
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPublicId() {
    return publicId;
  }

  public void setPublicId(String publicId) {
    this.publicId = publicId;
  }

  public String getHttpEndpoint() {
    return httpEndpoint;
  }

  public void setHttpEndpoint(String httpEndpoint) {
    this.httpEndpoint = httpEndpoint;
  }

  public String getDelaEndpoint() {
    return delaEndpoint;
  }

  public void setDelaEndpoint(String delaEndpoint) {
    this.delaEndpoint = delaEndpoint;
  }

  
}
