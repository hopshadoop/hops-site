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
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "users",
        catalog = "hops_site",
        schema = "")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Users.findAll",
          query = "SELECT u FROM Users u"),
  @NamedQuery(name = "Users.findById",
          query = "SELECT u FROM Users u WHERE u.id = :id"),
  @NamedQuery(name = "Users.findByFirstname",
          query = "SELECT u FROM Users u WHERE u.firstname = :firstname"),
  @NamedQuery(name = "Users.findByLastname",
          query = "SELECT u FROM Users u WHERE u.lastname = :lastname"),
  @NamedQuery(name = "Users.findByEmail",
          query = "SELECT u FROM Users u WHERE u.email = :email"),
  @NamedQuery(name = "Users.findByEmailAndClusterId",
          query = "SELECT u FROM Users u WHERE u.email = :email AND u.clusterId.clusterId = :clusterId"),
  @NamedQuery(name = "Users.findByStatus",
          query = "SELECT u FROM Users u WHERE u.status = :status")})
public class Users implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 45)
  @Column(name = "firstname")
  private String firstname;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 45)
  @Column(name = "lastname")
  private String lastname;
  @Pattern(regexp
          = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
          message = "Invalid email")
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 150)
  @Column(name = "email")
  private String email;
  @Column(name = "status")
  private Integer status;
  @OneToMany(cascade = CascadeType.ALL,
          mappedBy = "users")
  private Collection<DatasetIssue> datasetIssueCollection;
  @OneToMany(cascade = CascadeType.ALL,
          mappedBy = "users")
  private Collection<Comment> commentCollection;
  @OneToMany(cascade = CascadeType.ALL,
          mappedBy = "users")
  private Collection<CommentIssue> commentIssueCollection;
  @OneToMany(cascade = CascadeType.ALL,
          mappedBy = "users")
  private Collection<DatasetRating> datasetRatingCollection;
  @JoinColumn(name = "cluster_id",
          referencedColumnName = "cluster_id")
  @ManyToOne(optional = false)
  private RegisteredCluster clusterId;

  public Users() {
  }

  public Users(Integer id) {
    this.id = id;
  }

  public Users(Integer id, String firstname, String lastname, String email) {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
  }

  public Users(String firstname, String lastname, String email, RegisteredCluster clusterId) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.clusterId = clusterId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  @XmlTransient
  @JsonIgnore
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<DatasetIssue> getDatasetIssueCollection() {
    return datasetIssueCollection;
  }

  public void setDatasetIssueCollection(Collection<DatasetIssue> datasetIssueCollection) {
    this.datasetIssueCollection = datasetIssueCollection;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<Comment> getCommentCollection() {
    return commentCollection;
  }

  public void setCommentCollection(Collection<Comment> commentCollection) {
    this.commentCollection = commentCollection;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<CommentIssue> getCommentIssueCollection() {
    return commentIssueCollection;
  }

  public void setCommentIssueCollection(Collection<CommentIssue> commentIssueCollection) {
    this.commentIssueCollection = commentIssueCollection;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<DatasetRating> getDatasetRatingCollection() {
    return datasetRatingCollection;
  }

  public void setDatasetRatingCollection(Collection<DatasetRating> datasetRatingCollection) {
    this.datasetRatingCollection = datasetRatingCollection;
  }

  @XmlTransient
  @JsonIgnore
  public RegisteredCluster getClusterId() {
    return clusterId;
  }

  public void setClusterId(RegisteredCluster clusterId) {
    this.clusterId = clusterId;
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
    if (!(object instanceof Users)) {
      return false;
    }
    Users other = (Users) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.entity.Users[ id=" + id + " ]";
  }

}
