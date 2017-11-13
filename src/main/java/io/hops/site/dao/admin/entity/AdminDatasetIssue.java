package io.hops.site.dao.admin.entity;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.concurrent.Immutable;
import javax.annotation.security.RolesAllowed;
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

@Entity(name = "AdminDatasetIssue")
@Immutable
@RolesAllowed({"admin"})
@Table(name = "dataset_issue")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AdminDatasetIssue.findAll",
      query = "SELECT d FROM AdminDatasetIssue d")
  ,
    @NamedQuery(name = "AdminDatasetIssue.findById",
      query = "SELECT d FROM AdminDatasetIssue d WHERE d.id = :id")
  ,
    @NamedQuery(name = "AdminDatasetIssue.findByType",
      query = "SELECT d FROM AdminDatasetIssue d WHERE d.type = :type")
  ,
    @NamedQuery(name = "AdminDatasetIssue.findByMsg",
      query = "SELECT d FROM AdminDatasetIssue d WHERE d.msg = :msg")
  ,
    @NamedQuery(name = "AdminDatasetIssue.findByDateReported",
      query
      = "SELECT d FROM AdminDatasetIssue d WHERE d.dateReported = :dateReported")})
public class AdminDatasetIssue implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
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
  private AdminUsers users;
  @JoinColumn(name = "dataset_id",
      referencedColumnName = "id")
  @ManyToOne(optional = false)
  private AdminDataset datasetId;

  public AdminDatasetIssue() {
  }

  public AdminDatasetIssue(Integer id) {
    this.id = id;
  }

  public AdminDatasetIssue(Integer id, String type) {
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

  public AdminUsers getUsers() {
    return users;
  }

  public void setUsers(AdminUsers users) {
    this.users = users;
  }

  public AdminDataset getDatasetId() {
    return datasetId;
  }

  public void setDatasetId(AdminDataset datasetId) {
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
    if (!(object instanceof AdminDatasetIssue)) {
      return false;
    }
    AdminDatasetIssue other = (AdminDatasetIssue) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.admin.entity.DatasetIssue[ id=" + id + " ]";
  }
  
}
