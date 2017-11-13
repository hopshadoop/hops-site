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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

@Entity(name = "AdminRegisteredCluster")
@Immutable
@RolesAllowed({"admin"})
@Table(name = "registered_cluster")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AdminRegisteredCluster.findAll",
      query = "SELECT r FROM AdminRegisteredCluster r")
  ,
    @NamedQuery(name = "AdminRegisteredCluster.findById",
      query = "SELECT r FROM AdminRegisteredCluster r WHERE r.id = :id")
  ,
    @NamedQuery(name = "AdminRegisteredCluster.findByPublicId",
      query
      = "SELECT r FROM AdminRegisteredCluster r WHERE r.publicId = :publicId")
  ,
    @NamedQuery(name = "AdminRegisteredCluster.findByOrgName",
      query
      = "SELECT r FROM AdminRegisteredCluster r WHERE r.orgName = :orgName")
  ,
    @NamedQuery(name = "AdminRegisteredCluster.findByEmail",
      query
      = "SELECT r FROM AdminRegisteredCluster r WHERE r.email = :email")
  ,
    @NamedQuery(name = "AdminRegisteredCluster.findBySubject",
      query
      = "SELECT r FROM AdminRegisteredCluster r WHERE r.subject = :subject")
  ,
    @NamedQuery(name = "AdminRegisteredCluster.findByDelaEndpoint",
      query
      = "SELECT r FROM AdminRegisteredCluster r WHERE r.delaEndpoint = :delaEndpoint")
  ,
    @NamedQuery(name = "AdminRegisteredCluster.findByHttpEndpoint",
      query
      = "SELECT r FROM AdminRegisteredCluster r WHERE r.httpEndpoint = :httpEndpoint")
  ,
    @NamedQuery(name = "AdminRegisteredCluster.findByDateRegistered",
      query
      = "SELECT r FROM AdminRegisteredCluster r WHERE r.dateRegistered = :dateRegistered")})
public class AdminRegisteredCluster implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
      max = 200)
  @Column(name = "public_id")
  private String publicId;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
      max = 100)
  @Column(name = "org_name")
  private String orgName;
  // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
      max = 100)
  @Column(name = "email")
  private String email;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
      max = 1000)
  @Column(name = "subject")
  private String subject;
  @Basic(optional = false)
  @NotNull
  @Lob
  @Column(name = "cert")
  private byte[] cert;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
      max = 100)
  @Column(name = "dela_endpoint")
  private String delaEndpoint;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1,
      max = 100)
  @Column(name = "http_endpoint")
  private String httpEndpoint;
  @Basic(optional = false)
  @NotNull
  @Column(name = "date_registered")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateRegistered;
  @OneToMany(cascade = CascadeType.ALL,
      mappedBy = "clusterId")
  private Collection<AdminUsers> usersCollection;
  @OneToOne(cascade = CascadeType.ALL,
      mappedBy = "registeredCluster")
  private AdminClusterHeartbeat clusterHeartbeat;

  public AdminRegisteredCluster() {
  }

  public AdminRegisteredCluster(Integer id) {
    this.id = id;
  }

  public AdminRegisteredCluster(Integer id, String publicId, String orgName, String email, String subject, byte[] cert,
      String delaEndpoint, String httpEndpoint, Date dateRegistered) {
    this.id = id;
    this.publicId = publicId;
    this.orgName = orgName;
    this.email = email;
    this.subject = subject;
    this.cert = cert;
    this.delaEndpoint = delaEndpoint;
    this.httpEndpoint = httpEndpoint;
    this.dateRegistered = dateRegistered;
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

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public byte[] getCert() {
    return cert;
  }

  public void setCert(byte[] cert) {
    this.cert = cert;
  }

  public String getDelaEndpoint() {
    return delaEndpoint;
  }

  public void setDelaEndpoint(String delaEndpoint) {
    this.delaEndpoint = delaEndpoint;
  }

  public String getHttpEndpoint() {
    return httpEndpoint;
  }

  public void setHttpEndpoint(String httpEndpoint) {
    this.httpEndpoint = httpEndpoint;
  }

  public Date getDateRegistered() {
    return dateRegistered;
  }

  public void setDateRegistered(Date dateRegistered) {
    this.dateRegistered = dateRegistered;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<AdminUsers> getUsersCollection() {
    return usersCollection;
  }

  public void setUsersCollection(Collection<AdminUsers> usersCollection) {
    this.usersCollection = usersCollection;
  }

  public AdminClusterHeartbeat getClusterHeartbeat() {
    return clusterHeartbeat;
  }

  public void setClusterHeartbeat(AdminClusterHeartbeat clusterHeartbeat) {
    this.clusterHeartbeat = clusterHeartbeat;
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
    if (!(object instanceof AdminRegisteredCluster)) {
      return false;
    }
    AdminRegisteredCluster other = (AdminRegisteredCluster) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.admin.entity.RegisteredCluster[ id=" + id + " ]";
  }

}
