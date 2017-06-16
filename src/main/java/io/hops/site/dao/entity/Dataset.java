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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
  @NamedQuery(name = "Dataset.findByPublicId",
          query = "SELECT d FROM Dataset d WHERE d.publicId = :publicId"),
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Basic(optional = false)
  @Column(name = "Id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 1000)
  @Column(name = "public_id")
  private String publicId;
  @NotNull
  @Size(max = 255)
  @Column(name = "name")
  private String name;
  @Size(max = 2000)
  @Column(name = "description")
  private String description;
  @Column(name = "made_public_on")
  @Temporal(TemporalType.TIMESTAMP)
  private Date madePublicOn;
  @NotNull
  @Size(max = 150)
  @Column(name = "owner")
  private String owner;
  @Lob
  @Size(max = 16777215)
  @Column(name = "readme")
  private String readme;
  @Column(name = "status")
  private Integer status;
  @JoinTable(name = "hops_site.dataset_category",
          joinColumns = {
            @JoinColumn(name = "dataset_id",
                    referencedColumnName = "Id")},
          inverseJoinColumns
          = {
            @JoinColumn(name = "category_id",
                    referencedColumnName = "id")})
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Collection<Category> categoryCollection;
  @OneToMany(cascade = CascadeType.ALL,
          mappedBy = "datasetId")
  private Collection<DatasetIssue> datasetIssueCollection;
  @OneToOne(cascade = CascadeType.ALL,
          mappedBy = "datasetId")
  private PopularDataset popularDataset;
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
          mappedBy = "datasetId")
  private Collection<Comment> commentCollection;
  @JoinColumn(name = "cluster_id",
          referencedColumnName = "cluster_id")
  @ManyToOne
  private RegisteredCluster clusterId;
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
          mappedBy = "datasetId")
  private Collection<DatasetRating> datasetRatingCollection;

  public Dataset() {
  }

  public Dataset(Integer id) {
    this.id = id;
  }

  public Dataset(Integer id, String publicId) {
    this.id = id;
    this.publicId = publicId;
  }

  public Dataset(String publicId, String name, String description, Date madePublicOn, String owner, String readme,
          Collection<Category> categoryCollection, RegisteredCluster clusterId) {
    this.publicId = publicId;
    this.name = name;
    this.description = description;
    this.madePublicOn = madePublicOn;
    this.owner = owner;
    this.readme = readme;
    this.categoryCollection = categoryCollection;
    this.clusterId = clusterId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getPublicId() {
    return publicId;
  }

  public void setPublicId(String publicId) {
    this.publicId = publicId;
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

  public String getReadme() {
    return readme;
  }

  public void setReadme(String readme) {
    this.readme = readme;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Collection<Category> getCategoryCollection() {
    return categoryCollection;
  }

  public void setCategoryCollection(Collection<Category> categoryCollection) {
    this.categoryCollection = categoryCollection;
  }

  public Collection<DatasetIssue> getDatasetIssueCollection() {
    return datasetIssueCollection;
  }

  public void setDatasetIssueCollection(Collection<DatasetIssue> datasetIssueCollection) {
    this.datasetIssueCollection = datasetIssueCollection;
  }

  public PopularDataset getPopularDataset() {
    return popularDataset;
  }

  public void setPopularDataset(PopularDataset popularDataset) {
    this.popularDataset = popularDataset;
  }

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
    return "io.hops.site.dao.entity.Dataset[ id=" + id + " ]";
  }
  
}
