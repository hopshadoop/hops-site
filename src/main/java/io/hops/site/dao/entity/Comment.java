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
@Table(name = "comment",
        catalog = "hops_site",
        schema = "")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Comment.findAll",
          query = "SELECT c FROM Comment c"),
  @NamedQuery(name = "Comment.findById",
          query = "SELECT c FROM Comment c WHERE c.id = :id"),
  @NamedQuery(name = "Comment.findByContent",
          query = "SELECT c FROM Comment c WHERE c.content = :content"),
  @NamedQuery(name = "Comment.findByDatePublished",
          query
          = "SELECT c FROM Comment c WHERE c.datePublished = :datePublished")})
public class Comment implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 2000)
  @Column(name = "content")
  private String content;
  @Basic(optional = false)
  @NotNull
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
  @OneToMany(cascade = CascadeType.ALL,
          mappedBy = "commentId")
  private Collection<CommentIssue> commentIssueCollection;

  public Comment() {
  }

  public Comment(Integer id) {
    this.id = id;
  }

  public Comment(Integer id, String content, Date datePublished) {
    this.id = id;
    this.content = content;
    this.datePublished = datePublished;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
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

  public Dataset getDatasetId() {
    return datasetId;
  }

  public void setDatasetId(Dataset datasetId) {
    this.datasetId = datasetId;
  }

  @XmlTransient
  public Collection<CommentIssue> getCommentIssueCollection() {
    return commentIssueCollection;
  }

  public void setCommentIssueCollection(Collection<CommentIssue> commentIssueCollection) {
    this.commentIssueCollection = commentIssueCollection;
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
    if (!(object instanceof Comment)) {
      return false;
    }
    Comment other = (Comment) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.Comment[ id=" + id + " ]";
  }
  
}
