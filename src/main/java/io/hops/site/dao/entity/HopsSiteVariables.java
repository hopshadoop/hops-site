package io.hops.site.dao.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 * from hopsworks - io.hops.hopsworks.common.dao.util
 */
@Entity
@Table(name = "variables",
  catalog = "hops_site",
  schema = "")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "HopsSiteVariables.findAll", query = "SELECT v FROM HopsSiteVariables v"),
  @NamedQuery(name = "HopsSiteVariables.findById", query = "SELECT v FROM HopsSiteVariables v WHERE v.id = :id")})
public class HopsSiteVariables implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "id")
  private String id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "value")
  private String value;

  public HopsSiteVariables() {
  }

  public HopsSiteVariables(String id, String value) {
    this.id = id;
    this.value = value;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
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
    if (!(object instanceof HopsSiteVariables)) {
      return false;
    }
    HopsSiteVariables other = (HopsSiteVariables) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "io.hops.site.dao.entity.HopsSiteVariables[ id=" + id + " ]";
  }
}