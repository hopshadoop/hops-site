/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.entities;

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
 * @author jsvhqr
 */
@Entity
@Table(name = "registeredclusters")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Registeredclusters.findAll", query = "SELECT r FROM Registeredclusters r"),
    @NamedQuery(name = "Registeredclusters.findByName", query = "SELECT r FROM Registeredclusters r WHERE r.name = :name"),
    @NamedQuery(name = "Registeredclusters.findByRestendpoint", query = "SELECT r FROM Registeredclusters r WHERE r.restendpoint = :restendpoint"),
    @NamedQuery(name = "Registeredclusters.findByEmail", query = "SELECT r FROM Registeredclusters r WHERE r.email = :email"),
    @NamedQuery(name = "Registeredclusters.findByCert", query = "SELECT r FROM Registeredclusters r WHERE r.cert = :cert"),
    @NamedQuery(name = "Registeredclusters.findByUdpendpoint", query = "SELECT r FROM Registeredclusters r WHERE r.udpendpoint = :udpendpoint"),
    @NamedQuery(name = "Registeredclusters.findByHeartbeatsmissed", query = "SELECT r FROM Registeredclusters r WHERE r.heartbeatsmissed = :heartbeatsmissed"),
    @NamedQuery(name = "Registeredclusters.findByDateregistered", query = "SELECT r FROM Registeredclusters r WHERE r.dateregistered = :dateregistered")})
public class Registeredclusters implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "restendpoint")
    private String restendpoint;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;
    @Size(max = 1000)
    @Column(name = "cert")
    private String cert;
    @Size(max = 300)
    @Column(name = "udpendpoint")
    private String udpendpoint;
    @Basic(optional = false)
    @NotNull
    @Column(name = "heartbeatsmissed")
    private long heartbeatsmissed;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "dateregistered")
    private String dateregistered;

    public Registeredclusters() {
    }

    public Registeredclusters(String name) {
        this.name = name;
    }

    public Registeredclusters(String name, String restendpoint, String email, long heartbeatsmissed, String dateregistered) {
        this.name = name;
        this.restendpoint = restendpoint;
        this.email = email;
        this.heartbeatsmissed = heartbeatsmissed;
        this.dateregistered = dateregistered;
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

    public String getUdpendpoint() {
        return udpendpoint;
    }

    public void setUdpendpoint(String udpendpoint) {
        this.udpendpoint = udpendpoint;
    }

    public long getHeartbeatsmissed() {
        return heartbeatsmissed;
    }

    public void setHeartbeatsmissed(long heartbeatsmissed) {
        this.heartbeatsmissed = heartbeatsmissed;
    }

    public String getDateregistered() {
        return dateregistered;
    }

    public void setDateregistered(String dateregistered) {
        this.dateregistered = dateregistered;
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
        if (!(object instanceof Registeredclusters)) {
            return false;
        }
        Registeredclusters other = (Registeredclusters) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "site.hops.entities.Registeredclusters[ name=" + name + " ]";
    }
    
}
