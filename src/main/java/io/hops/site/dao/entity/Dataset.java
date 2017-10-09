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
import java.util.LinkedList;
import java.util.List;
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
import javax.persistence.ManyToMany;
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
  @NamedQuery(name = Dataset.FIND_BY_PUBLIC_ID,
    query = "SELECT d FROM Dataset d WHERE d.publicId = :" + Dataset.PUBLIC_ID),
  @NamedQuery(name = Dataset.FIND_BY_PUBLIC_ID_LIST,
    query = "SELECT d FROM Dataset d WHERE d.publicId IN :" + Dataset.PUBLIC_ID_LIST),
  @NamedQuery(name = "Dataset.findByName",
    query = "SELECT d FROM Dataset d WHERE d.name = :name"),
  @NamedQuery(name = "Dataset.findByDescription",
    query = "SELECT d FROM Dataset d WHERE d.description = :description"),
  @NamedQuery(name = "Dataset.findByMadePublicOn",
    query = "SELECT d FROM Dataset d WHERE d.madePublicOn = :madePublicOn"),
  @NamedQuery(name = "Dataset.findByStatus",
    query = "SELECT d FROM Dataset d WHERE d.status = :status")})
public class Dataset implements Serializable {
  public static final String FIND_BY_PUBLIC_ID = "Dataset.findByPublicId";
  public static final String FIND_BY_PUBLIC_ID_LIST = "Dataset.findByPublicIdList";
  public static final String PUBLIC_ID = "publicId";
  public static final String PUBLIC_ID_LIST = "publicIdList"; 
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Basic(optional = false)
  @Column(name = "id")
  private int id;
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
  @Column(name = "published_on")
  @Temporal(TemporalType.TIMESTAMP)
  private Date madePublicOn;
  @Column(name = "readme_path")
  private String readmePath;
  @Column(name = "status")
  private Integer status;
  @Column(name = "size")
  private long dsSize;
  @Column(name = "rating")
  private Integer rating;
  @JoinTable(name = "hops_site.dataset_category",
    joinColumns = {
      @JoinColumn(name = "dataset_id",
        referencedColumnName = "id")},
    inverseJoinColumns
    = {
      @JoinColumn(name = "category_id",
        referencedColumnName = "id")})
  @ManyToMany(fetch = FetchType.LAZY)
  private Collection<Category> categoryCollection;
  @OneToMany(cascade = CascadeType.REMOVE,
    fetch = FetchType.LAZY,
    mappedBy = "datasetId")
  private Collection<DatasetIssue> datasetIssueCollection;
  @OneToMany(cascade = CascadeType.REMOVE,
    fetch = FetchType.LAZY,
    mappedBy = "datasetId")
  private Collection<Comment> commentCollection;
  @JoinColumn(name = "owner_user_id",
    referencedColumnName = "id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Users owner;
  @OneToMany(cascade = CascadeType.ALL,
    fetch = FetchType.LAZY,
    mappedBy = "datasetId")
  private Collection<DatasetRating> datasetRatingCollection;

  public Dataset() {
  }

  public Dataset(String publicId, String name, String description, String readmePath,
    Collection<Category> categoryCollection, Users owner, long dsSize) {
    this.publicId = publicId;
    this.name = name;
    this.description = description;
    this.readmePath = readmePath;
    this.categoryCollection = categoryCollection;
    this.owner = owner;
    this.dsSize = dsSize;
  }

  @JsonIgnore
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

  @JsonIgnore
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public long getDsSize() {
    return dsSize;
  }

  public void setDsSize(long dsSize) {
    this.dsSize = dsSize;
  }

  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
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

  public Collection<Comment> getCommentCollection() {
    return commentCollection;
  }

  public void setCommentCollection(Collection<Comment> commentCollection) {
    this.commentCollection = commentCollection;
  }

  public Users getOwner() {
    return owner;
  }

  public void setOwner(Users owner) {
    this.owner = owner;
  }

  public Collection<DatasetRating> getDatasetRatingCollection() {
    return datasetRatingCollection;
  }

  public void setDatasetRatingCollection(Collection<DatasetRating> datasetRatingCollection) {
    this.datasetRatingCollection = datasetRatingCollection;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 79 * hash + this.id;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Dataset other = (Dataset) obj;
    if (this.id != other.id) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.entity.Dataset[ id=" + id + " ]";
  }

  public String getReadmePath() {
    return readmePath;
  }

  public void setReadmePath(String readmePath) {
    this.readmePath = readmePath;
  }
  
  public Collection<String> getCategories() {
    List<String> result = new LinkedList<>();
    for(Category c : categoryCollection) {
      result.add(c.getName());
    }
    return result;
  }
}
