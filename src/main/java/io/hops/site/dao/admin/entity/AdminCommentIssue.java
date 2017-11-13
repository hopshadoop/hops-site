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

@Entity(name = "AdminCommentIssue")
@Immutable
@RolesAllowed({"admin"})
@Table(name = "comment_issue")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AdminCommentIssue.findAll",
      query = "SELECT c FROM CommentIssue c")
  ,
    @NamedQuery(name = "AdminCommentIssue.findById",
      query = "SELECT c FROM CommentIssue c WHERE c.id = :id")
  ,
    @NamedQuery(name = "AdminCommentIssue.findByType",
      query = "SELECT c FROM CommentIssue c WHERE c.type = :type")
  ,
    @NamedQuery(name = "AdminCommentIssue.findByDateReported",
      query
      = "SELECT c FROM AdminCommentIssue c WHERE c.dateReported = :dateReported")
  ,
    @NamedQuery(name = "AdminCommentIssue.findByMsg",
      query = "SELECT c FROM AdminCommentIssue c WHERE c.msg = :msg")})
public class AdminCommentIssue implements Serializable {

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
  @Basic(optional = false)
  @NotNull
  @Column(name = "date_reported")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateReported;
  @Size(max = 2000)
  @Column(name = "msg")
  private String msg;
  @JoinColumn(name = "users",
      referencedColumnName = "id")
  @ManyToOne(optional = false)
  private AdminUsers users;
  @JoinColumn(name = "comment_id",
      referencedColumnName = "id")
  @ManyToOne(optional = false)
  private AdminComment commentId;

  public AdminCommentIssue() {
  }

  public AdminCommentIssue(Integer id) {
    this.id = id;
  }

  public AdminCommentIssue(Integer id, String type, Date dateReported) {
    this.id = id;
    this.type = type;
    this.dateReported = dateReported;
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

  public AdminUsers getUsers() {
    return users;
  }

  public void setUsers(AdminUsers users) {
    this.users = users;
  }

  public AdminComment getCommentId() {
    return commentId;
  }

  public void setCommentId(AdminComment commentId) {
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
    if (!(object instanceof AdminCommentIssue)) {
      return false;
    }
    AdminCommentIssue other = (AdminCommentIssue) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.admin.entity.CommentIssue[ id=" + id + " ]";
  }
  
}
