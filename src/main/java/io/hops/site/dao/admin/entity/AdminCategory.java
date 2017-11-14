package io.hops.site.dao.admin.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.concurrent.Immutable;
import javax.annotation.security.RolesAllowed;

@Entity(name = "AdminCategory")
@Immutable
@RolesAllowed({"admin"})
@Table(name = "category")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AdminCategory.findAll",
      query = "SELECT c FROM AdminCategory c")
  ,
    @NamedQuery(name = "AdminCategory.findById",
      query = "SELECT c FROM AdminCategory c WHERE c.id = :id")
  ,
    @NamedQuery(name = "AdminCategory.findByName",
      query = "SELECT c FROM AdminCategory c WHERE c.name = :name")})
public class AdminCategory implements Serializable {

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
  @Column(name = "name")
  private String name;
  @ManyToMany(mappedBy = "categoryCollection")
  private Collection<AdminDataset> datasetCollection;

  public AdminCategory() {
  }

  public AdminCategory(Integer id) {
    this.id = id;
  }

  public AdminCategory(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlTransient
  @JsonIgnore
  public Collection<AdminDataset> getDatasetCollection() {
    return datasetCollection;
  }

  public void setDatasetCollection(Collection<AdminDataset> datasetCollection) {
    this.datasetCollection = datasetCollection;
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
    if (!(object instanceof AdminCategory)) {
      return false;
    }
    AdminCategory other = (AdminCategory) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "AdminCategory{" + "id=" + id + ", name=" + name + '}';
  }
  
}