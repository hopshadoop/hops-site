package io.hops.site.dao.admin.entity;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import javax.persistence.FetchType;

@Entity(name = "AdminDataset")
@Immutable
@RolesAllowed({"admin"})
@Table(name = "dataset")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AdminDataset.findAll",
      query = "SELECT d FROM AdminDataset d")
  ,
    @NamedQuery(name = "AdminDataset.findById",
      query = "SELECT d FROM AdminDataset d WHERE d.id = :id")
  ,
    @NamedQuery(name = "AdminDataset.findByPublicId",
      query = "SELECT d FROM AdminDataset d WHERE d.publicId = :publicId")
  ,
    @NamedQuery(name = "AdminDataset.findByName",
      query = "SELECT d FROM AdminDataset d WHERE d.name = :name")
  ,
    @NamedQuery(name = "AdminDataset.findByVersion",
      query = "SELECT d FROM AdminDataset d WHERE d.version = :version")
  ,
    @NamedQuery(name = "AdminDataset.findByDescription",
      query = "SELECT d FROM AdminDataset d WHERE d.description = :description")
  ,
    @NamedQuery(name = "AdminDataset.findByPublishedOn",
      query = "SELECT d FROM AdminDataset d WHERE d.publishedOn = :publishedOn")
  ,
    @NamedQuery(name = "AdminDataset.findByReadmePath",
      query = "SELECT d FROM AdminDataset d WHERE d.readmePath = :readmePath")
  ,
    @NamedQuery(name = "AdminDataset.findByStatus",
      query = "SELECT d FROM AdminDataset d WHERE d.status = :status")
  ,
    @NamedQuery(name = "AdminDataset.findBySize",
      query = "SELECT d FROM AdminDataset d WHERE d.size = :size")
  ,
    @NamedQuery(name = "AdminDataset.findByRating",
      query = "SELECT d FROM AdminDataset d WHERE d.rating = :rating")})
public class AdminDataset implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
      max = 1000)
  @Column(name = "public_id")
  private String publicId;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
      max = 255)
  @Column(name = "name")
  private String name;
  @Basic(optional = false)
  @NotNull
  @Column(name = "version")
  private int version;
  @Size(max = 2000)
  @Column(name = "description")
  private String description;
  @Basic(optional = false)
  @NotNull
  @Column(name = "published_on")
  @Temporal(TemporalType.TIMESTAMP)
  private Date publishedOn;
  @Size(max = 150)
  @Column(name = "readme_path")
  private String readmePath;
  @Column(name = "status")
  private Integer status;
  @Column(name = "size")
  private BigInteger size;
  @Column(name = "rating")
  private Integer rating;
  @JoinTable(name = "dataset_category",
      joinColumns = {
        @JoinColumn(name = "dataset_id",
            referencedColumnName = "id")},
      inverseJoinColumns
      = {
        @JoinColumn(name = "category_id",
            referencedColumnName = "id")})
  @ManyToMany
  private Collection<AdminCategory> categoryCollection;
  @OneToMany(cascade = CascadeType.ALL,
      mappedBy = "datasetId")
  private Collection<AdminDatasetIssue> datasetIssueCollection;
  @OneToMany(cascade = CascadeType.ALL,
      mappedBy = "dataset")
  private Collection<AdminLiveDataset> liveDatasetCollection;
  @OneToMany(cascade = CascadeType.ALL,
      mappedBy = "datasetId")
  private Collection<AdminComment> commentCollection;
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
      mappedBy = "dataset")
  private Collection<AdminDatasetHealth> datasetHealthCollection;
  @JoinColumn(name = "owner_user_id",
      referencedColumnName = "id")
  @ManyToOne(optional = false)
  private AdminUsers ownerUserId;
  @OneToMany(cascade = CascadeType.ALL,
      mappedBy = "datasetId")
  private Collection<AdminDatasetRating> datasetRatingCollection;

  public AdminDataset() {
  }

  public AdminDataset(Integer id) {
    this.id = id;
  }

  public AdminDataset(Integer id, String publicId, String name, int version, Date publishedOn) {
    this.id = id;
    this.publicId = publicId;
    this.name = name;
    this.version = version;
    this.publishedOn = publishedOn;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getPublicId() {
    return publicId;
  }

  public void setPublicId(String publicId) {
    this.publicId = publicId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getPublishedOn() {
    return publishedOn;
  }

  public void setPublishedOn(Date publishedOn) {
    this.publishedOn = publishedOn;
  }

  public String getReadmePath() {
    return readmePath;
  }

  public void setReadmePath(String readmePath) {
    this.readmePath = readmePath;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public BigInteger getSize() {
    return size;
  }

  public void setSize(BigInteger size) {
    this.size = size;
  }

  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<AdminCategory> getCategoryCollection() {
    return categoryCollection;
  }

  public void setCategoryCollection(Collection<AdminCategory> categoryCollection) {
    this.categoryCollection = categoryCollection;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<AdminDatasetIssue> getDatasetIssueCollection() {
    return datasetIssueCollection;
  }

  public void setDatasetIssueCollection(Collection<AdminDatasetIssue> datasetIssueCollection) {
    this.datasetIssueCollection = datasetIssueCollection;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<AdminLiveDataset> getLiveDatasetCollection() {
    return liveDatasetCollection;
  }

  public void setLiveDatasetCollection(Collection<AdminLiveDataset> liveDatasetCollection) {
    this.liveDatasetCollection = liveDatasetCollection;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<AdminComment> getCommentCollection() {
    return commentCollection;
  }

  public void setCommentCollection(Collection<AdminComment> commentCollection) {
    this.commentCollection = commentCollection;
  }

  public Collection<AdminDatasetHealth> getDatasetHealthCollection() {
    return datasetHealthCollection;
  }

  public void setDatasetHealthCollection(Collection<AdminDatasetHealth> datasetHealthCollection) {
    this.datasetHealthCollection = datasetHealthCollection;
  }

  public AdminUsers getOwnerUserId() {
    return ownerUserId;
  }

  public void setOwnerUserId(AdminUsers ownerUserId) {
    this.ownerUserId = ownerUserId;
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
    if (!(object instanceof AdminDataset)) {
      return false;
    }
    AdminDataset other = (AdminDataset) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.admin.entity.Dataset[ id=" + id + " ]";
  }

}
