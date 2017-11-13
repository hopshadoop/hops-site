package io.hops.site.dao.admin.entity;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.concurrent.Immutable;
import javax.annotation.security.RolesAllowed;


@Entity(name = "AdminUsers")
@Immutable
@RolesAllowed({"admin"})
@Table(name = "users")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AdminUsers.findAll",
      query = "SELECT u FROM AdminUsers u")
  ,
    @NamedQuery(name = "AdminUsers.findById",
      query = "SELECT u FROM AdminUsers u WHERE u.id = :id")
  ,
    @NamedQuery(name = "AdminUsers.findByFirstname",
      query = "SELECT u FROM AdminUsers u WHERE u.firstname = :firstname")
  ,
    @NamedQuery(name = "AdminUsers.findByLastname",
      query = "SELECT u FROM AdminUsers u WHERE u.lastname = :lastname")
  ,
    @NamedQuery(name = "AdminUsers.findByEmail",
      query = "SELECT u FROM AdminUsers u WHERE u.email = :email")
  ,
    @NamedQuery(name = "AdminUsers.findByStatus",
      query = "SELECT u FROM AdminUsers u WHERE u.status = :status")})
public class AdminUsers implements Serializable {

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
  @Column(name = "firstname")
  private String firstname;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
      max = 45)
  @Column(name = "lastname")
  private String lastname;
  // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
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
  private Collection<AdminDatasetIssue> datasetIssueCollection;
  @JoinColumn(name = "cluster_id",
      referencedColumnName = "id")
  @ManyToOne(optional = false)
  private AdminRegisteredCluster clusterId;
  @OneToMany(cascade = CascadeType.ALL,
      mappedBy = "users")
  private Collection<AdminComment> commentCollection;
  @OneToMany(cascade = CascadeType.ALL,
      mappedBy = "users")
  private Collection<AdminCommentIssue> commentIssueCollection;
  @OneToMany(cascade = CascadeType.ALL,
      mappedBy = "ownerUserId")
  private Collection<AdminDataset> datasetCollection;
  @OneToMany(cascade = CascadeType.ALL,
      mappedBy = "users")
  private Collection<AdminDatasetRating> datasetRatingCollection;

  public AdminUsers() {
  }

  public AdminUsers(Integer id) {
    this.id = id;
  }

  public AdminUsers(Integer id, String firstname, String lastname, String email) {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
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
  public Collection<AdminDatasetIssue> getDatasetIssueCollection() {
    return datasetIssueCollection;
  }

  public void setDatasetIssueCollection(Collection<AdminDatasetIssue> datasetIssueCollection) {
    this.datasetIssueCollection = datasetIssueCollection;
  }

  public AdminRegisteredCluster getClusterId() {
    return clusterId;
  }

  public void setClusterId(AdminRegisteredCluster clusterId) {
    this.clusterId = clusterId;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<AdminComment> getCommentCollection() {
    return commentCollection;
  }

  public void setCommentCollection(Collection<AdminComment> commentCollection) {
    this.commentCollection = commentCollection;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<AdminCommentIssue> getCommentIssueCollection() {
    return commentIssueCollection;
  }

  public void setCommentIssueCollection(Collection<AdminCommentIssue> commentIssueCollection) {
    this.commentIssueCollection = commentIssueCollection;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<AdminDataset> getDatasetCollection() {
    return datasetCollection;
  }

  public void setDatasetCollection(Collection<AdminDataset> datasetCollection) {
    this.datasetCollection = datasetCollection;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<AdminDatasetRating> getDatasetRatingCollection() {
    return datasetRatingCollection;
  }

  public void setDatasetRatingCollection(Collection<AdminDatasetRating> datasetRatingCollection) {
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
    if (!(object instanceof AdminUsers)) {
      return false;
    }
    AdminUsers other = (AdminUsers) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.admin.entity.Users[ id=" + id + " ]";
  }
  
}
