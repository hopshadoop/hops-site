package io.hops.site.dao.admin.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.concurrent.Immutable;
import javax.annotation.security.RolesAllowed;


@Entity(name = "AdminComment")
@Immutable
@RolesAllowed({"admin"})
@Table(name = "comment")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AdminComment.findAll",
      query = "SELECT c FROM AdminComment c")
  ,
    @NamedQuery(name = "AdminComment.findById",
      query = "SELECT c FROM AdminComment c WHERE c.id = :id")
  ,
    @NamedQuery(name = "AdminComment.findByContent",
      query = "SELECT c FROM AdminComment c WHERE c.content = :content")
  ,
    @NamedQuery(name = "AdminComment.findByDatePublished",
      query
      = "SELECT c FROM AdminComment c WHERE c.datePublished = :datePublished")})
public class AdminComment implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
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
  private AdminUsers users;
  @JoinColumn(name = "dataset_id",
      referencedColumnName = "id")
  @ManyToOne(optional = false)
  private AdminDataset datasetId;
  @OneToMany(cascade = CascadeType.ALL,
      mappedBy = "commentId")
  private Collection<AdminCommentIssue> commentIssueCollection;

  public AdminComment() {
  }

  public AdminComment(Integer id) {
    this.id = id;
  }

  public AdminComment(Integer id, String content, Date datePublished) {
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

  @XmlTransient
  @JsonIgnore
  public Collection<AdminCommentIssue> getCommentIssueCollection() {
    return commentIssueCollection;
  }

  public void setCommentIssueCollection(Collection<AdminCommentIssue> commentIssueCollection) {
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
    if (!(object instanceof AdminComment)) {
      return false;
    }
    AdminComment other = (AdminComment) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.admin.entity.Comment[ id=" + id + " ]";
  }
  
}
