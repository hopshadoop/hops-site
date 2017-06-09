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
@Table(name = "dataset_issue",
        catalog = "hops_site",
        schema = "")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "DatasetIssue.findAll",
          query = "SELECT d FROM DatasetIssue d"),
  @NamedQuery(name = "DatasetIssue.findById",
          query = "SELECT d FROM DatasetIssue d WHERE d.id = :id"),
  @NamedQuery(name = "DatasetIssue.findByType",
          query = "SELECT d FROM DatasetIssue d WHERE d.type = :type"),
  @NamedQuery(name = "DatasetIssue.findByMsg",
          query = "SELECT d FROM DatasetIssue d WHERE d.msg = :msg"),
  @NamedQuery(name = "DatasetIssue.findByDateReported",
          query
          = "SELECT d FROM DatasetIssue d WHERE d.dateReported = :dateReported")})
public class DatasetIssue implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
          max = 45)
  @Column(name = "type")
  private String type;
  @Size(max = 2000)
  @Column(name = "msg")
  private String msg;
  @Column(name = "date_reported")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateReported;
  @JoinColumn(name = "users",
          referencedColumnName = "id")
  @ManyToOne(optional = false)
  private Users users;
  @JoinColumn(name = "dataset_id",
          referencedColumnName = "Id")
  @ManyToOne(optional = false)
  private Dataset datasetId;

  public DatasetIssue() {
  }

  public DatasetIssue(Integer id) {
    this.id = id;
  }

  public DatasetIssue(Integer id, String type) {
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

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Date getDateReported() {
    return dateReported;
  }

  public void setDateReported(Date dateReported) {
    this.dateReported = dateReported;
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

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof DatasetIssue)) {
      return false;
    }
    DatasetIssue other = (DatasetIssue) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.DatasetIssue[ id=" + id + " ]";
  }

}
