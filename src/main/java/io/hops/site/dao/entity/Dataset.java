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

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "dataset",
        catalog = "hops_site",
        schema = "")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Dataset.findAll",
          query = "SELECT d FROM Dataset d"),
  @NamedQuery(name = "Dataset.findById",
          query = "SELECT d FROM Dataset d WHERE d.id = :id"),
  @NamedQuery(name = "Dataset.findByName",
          query = "SELECT d FROM Dataset d WHERE d.name = :name"),
  @NamedQuery(name = "Dataset.findByDescription",
          query
          = "SELECT d FROM Dataset d WHERE d.description = :description"),
  @NamedQuery(name = "Dataset.findByMadePublicOn",
          query
          = "SELECT d FROM Dataset d WHERE d.madePublicOn = :madePublicOn"),
  @NamedQuery(name = "Dataset.findByOwner",
          query = "SELECT d FROM Dataset d WHERE d.owner = :owner"),
  @NamedQuery(name = "Dataset.findByStatus",
          query = "SELECT d FROM Dataset d WHERE d.status = :status")})
public class Dataset implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 1000)
  @Column(name = "Id")
  private String id;
  @Size(max = 255)
  @Column(name = "name")
  private String name;
  @Size(max = 2000)
  @Column(name = "description")
  private String description;
  @Column(name = "made_public_on")
  @Temporal(TemporalType.TIMESTAMP)
  private Date madePublicOn;
  @Size(max = 150)
  @Column(name = "owner")
  private String owner;
  @Lob
  @Column(name = "readme")
  private byte[] readme;
  @Column(name = "status")
  private Integer status;
  @OneToMany(cascade = CascadeType.ALL,
          mappedBy = "datasetId")
  private Collection<DatasetIssue> datasetIssueCollection;
  @OneToMany(cascade = CascadeType.ALL,
          mappedBy = "datasetId")
  private Collection<Comment> commentCollection;
  @JoinColumn(name = "cluster_id",
          referencedColumnName = "cluster_id")
  @ManyToOne
  private RegisteredCluster clusterId;
  @OneToMany(cascade = CascadeType.ALL,
          mappedBy = "datasetIs")
  private Collection<DatasetRating> datasetRatingCollection;

  public Dataset() {
  }

  public Dataset(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getMadePublicOn() {
    return madePublicOn;
  }

  public void setMadePublicOn(Date madePublicOn) {
    this.madePublicOn = madePublicOn;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public byte[] getReadme() {
    return readme;
  }

  public void setReadme(byte[] readme) {
    this.readme = readme;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @XmlTransient
  public Collection<DatasetIssue> getDatasetIssueCollection() {
    return datasetIssueCollection;
  }

  public void setDatasetIssueCollection(Collection<DatasetIssue> datasetIssueCollection) {
    this.datasetIssueCollection = datasetIssueCollection;
  }

  @XmlTransient
  public Collection<Comment> getCommentCollection() {
    return commentCollection;
  }

  public void setCommentCollection(Collection<Comment> commentCollection) {
    this.commentCollection = commentCollection;
  }

  public RegisteredCluster getClusterId() {
    return clusterId;
  }

  public void setClusterId(RegisteredCluster clusterId) {
    this.clusterId = clusterId;
  }

  @XmlTransient
  public Collection<DatasetRating> getDatasetRatingCollection() {
    return datasetRatingCollection;
  }

  public void setDatasetRatingCollection(Collection<DatasetRating> datasetRatingCollection) {
    this.datasetRatingCollection = datasetRatingCollection;
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
    if (!(object instanceof Dataset)) {
      return false;
    }
    Dataset other = (Dataset) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.Dataset[ id=" + id + " ]";
  }
  
}
