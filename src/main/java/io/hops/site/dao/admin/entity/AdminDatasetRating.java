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
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "AdminDatasetRating")
@Immutable
@RolesAllowed({"admin"})
@Table(name = "dataset_rating")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AdminDatasetRating.findAll",
      query = "SELECT d FROM AdminDatasetRating d")
  ,
    @NamedQuery(name = "AdminDatasetRating.findById",
      query = "SELECT d FROM AdminDatasetRating d WHERE d.id = :id")
  ,
    @NamedQuery(name = "AdminDatasetRating.findByRating",
      query = "SELECT d FROM AdminDatasetRating d WHERE d.rating = :rating")
  ,
    @NamedQuery(name = "AdminDatasetRating.findByDatePublished",
      query
      = "SELECT d FROM AdminDatasetRating d WHERE d.datePublished = :datePublished")})
public class AdminDatasetRating implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Column(name = "rating")
  private int rating;
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

  public AdminDatasetRating() {
  }

  public AdminDatasetRating(Integer id) {
    this.id = id;
  }

  public AdminDatasetRating(Integer id, int rating, Date datePublished) {
    this.id = id;
    this.rating = rating;
    this.datePublished = datePublished;
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
    if (!(object instanceof AdminDatasetRating)) {
      return false;
    }
    AdminDatasetRating other = (AdminDatasetRating) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.admin.entity.DatasetRating[ id=" + id + " ]";
  }
  
}
