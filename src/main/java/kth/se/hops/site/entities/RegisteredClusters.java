/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.se.hops.site.entities;

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
 *
 * @author archer
 */
@Entity
@Table(name = "registered_clusters")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegisteredClusters.findAll", query = "SELECT r FROM RegisteredClusters r"),
    @NamedQuery(name = "RegisteredClusters.findByName", query = "SELECT r FROM RegisteredClusters r WHERE r.name = :name"),
    @NamedQuery(name = "RegisteredClusters.findByRestendpoint", query = "SELECT r FROM RegisteredClusters r WHERE r.restendpoint = :restendpoint"),
    @NamedQuery(name = "RegisteredClusters.findByEmail", query = "SELECT r FROM RegisteredClusters r WHERE r.email = :email"),
    @NamedQuery(name = "RegisteredClusters.findByCert", query = "SELECT r FROM RegisteredClusters r WHERE r.cert = :cert"),
    @NamedQuery(name = "RegisteredClusters.findByUdpSupport", query = "SELECT r FROM RegisteredClusters r WHERE r.udpSupport = :udpSupport")})
public class RegisteredClusters implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "restendpoint")
    private String restendpoint;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "cert")
    private String cert;
    @Basic(optional = false)
    @NotNull
    @Column(name = "udp_support")
    private boolean udpSupport;

    public RegisteredClusters() {
    }

    public RegisteredClusters(String name) {
        this.name = name;
    }

    public RegisteredClusters(String name, String restendpoint, String email, String cert, boolean udpSupport) {
        this.name = name;
        this.restendpoint = restendpoint;
        this.email = email;
        this.cert = cert;
        this.udpSupport = udpSupport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestendpoint() {
        return restendpoint;
    }

    public void setRestendpoint(String restendpoint) {
        this.restendpoint = restendpoint;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public boolean getUdpSupport() {
        return udpSupport;
    }

    public void setUdpSupport(boolean udpSupport) {
        this.udpSupport = udpSupport;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegisteredClusters)) {
            return false;
        }
        RegisteredClusters other = (RegisteredClusters) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kth.se.hops.site.entities.RegisteredClusters[ name=" + name + " ]";
    }
    
}
