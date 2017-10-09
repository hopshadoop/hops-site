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
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

@Entity
@Table(name = "dataset_rating",
        catalog = "hops_site",
        schema = "")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "DatasetRating.findAll",
          query = "SELECT d FROM DatasetRating d"),
  @NamedQuery(name = "DatasetRating.findById",
          query = "SELECT d FROM DatasetRating d WHERE d.id = :id"),
  @NamedQuery(name = "DatasetRating.findByRating",
          query = "SELECT d FROM DatasetRating d WHERE d.rating = :rating"),
  @NamedQuery(name = "DatasetRating.findByDatasetAndUser",
          query = "SELECT d FROM DatasetRating d WHERE d.datasetId.publicId = :publicId AND d.users.id = :userId"),
  @NamedQuery(name = "DatasetRating.findByDatePublished",
          query
          = "SELECT d FROM DatasetRating d WHERE d.datePublished = :datePublished")})
public class DatasetRating implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Column(name = "rating")
  private int rating;
  @Basic(optional = false)
  @Column(name = "date_published")
  @Temporal(TemporalType.TIMESTAMP)
  private Date datePublished;
  @JoinColumn(name = "users",
          referencedColumnName = "id")
  @ManyToOne(optional = false)
  private Users users;
  @JoinColumn(name = "dataset_id",
          referencedColumnName = "Id")
  @ManyToOne(optional = false)
  private Dataset datasetId;

  public DatasetRating() {
  }

  public DatasetRating(Integer id) {
    this.id = id;
  }

  public DatasetRating(Integer id, int rating) {
    this.id = id;
    this.rating = rating;
  }

  public DatasetRating(int rating, Users users, Dataset datasetId) {
    this.rating = rating;
    this.users = users;
    this.datasetId = datasetId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public Date getDatePublished() {
    return datePublished;
  }

  public void setDatePublished(Date datePublished) {
    this.datePublished = datePublished;
  }

  public Users getUsers() {
    return users;
  }

  public void setUsers(Users users) {
    this.users = users;
  }

  @XmlTransient
  public Dataset getDatasetId() {
    return datasetId;
  }

  public void setDatasetId(Dataset datasetId) {
    this.datasetId = datasetId;
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
    if (!(object instanceof DatasetRating)) {
      return false;
    }
    DatasetRating other = (DatasetRating) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.entity.DatasetRating[ id=" + id + " ]";
  }

}
