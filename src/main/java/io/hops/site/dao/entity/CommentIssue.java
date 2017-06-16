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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "comment_issue",
        catalog = "hops_site",
        schema = "")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "CommentIssue.findAll",
          query = "SELECT c FROM CommentIssue c"),
  @NamedQuery(name = "CommentIssue.findById",
          query = "SELECT c FROM CommentIssue c WHERE c.id = :id"),
  @NamedQuery(name = "CommentIssue.findByType",
          query = "SELECT c FROM CommentIssue c WHERE c.type = :type"),
  @NamedQuery(name = "CommentIssue.findByDateReported",
          query
          = "SELECT c FROM CommentIssue c WHERE c.dateReported = :dateReported"),
  @NamedQuery(name = "CommentIssue.findByMsg",
          query = "SELECT c FROM CommentIssue c WHERE c.msg = :msg")})
public class CommentIssue implements Serializable {

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
  @Column(name = "type")
  private String type;
  @Column(name = "date_reported")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateReported;
  @Size(max = 2000)
  @Column(name = "msg")
  private String msg;
  @JoinColumn(name = "users",
          referencedColumnName = "id")
  @ManyToOne(optional = false)
  private Users users;
  @JoinColumn(name = "comment_id",
          referencedColumnName = "id")
  @ManyToOne(optional = false)
  private Comment commentId;

  public CommentIssue() {
  }

  public CommentIssue(String type, String msg, Users users, Comment commentId) {
    this.type = type;
    this.msg = msg;
    this.users = users;
    this.commentId = commentId;
  }

  public CommentIssue(Integer id) {
    this.id = id;
  }

  public CommentIssue(Integer id, String type) {
    this.id = id;
    this.type = type;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Date getDateReported() {
    return dateReported;
  }

  public void setDateReported(Date dateReported) {
    this.dateReported = dateReported;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Users getUsers() {
    return users;
  }

  public void setUsers(Users users) {
    this.users = users;
  }

  public Comment getCommentId() {
    return commentId;
  }

  public void setCommentId(Comment commentId) {
    this.commentId = commentId;
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
    if (!(object instanceof CommentIssue)) {
      return false;
    }
    CommentIssue other = (CommentIssue) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.entity.CommentIssue[ id=" + id + " ]";
  }
  
}
