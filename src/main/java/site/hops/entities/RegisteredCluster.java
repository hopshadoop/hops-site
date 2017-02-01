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
@Table(name = "hops_site.registered_cluster")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegisteredCluster.findAll", query = "SELECT r FROM RegisteredCluster r"),
    @NamedQuery(name = "RegisteredCluster.findByClusterId", query = "SELECT r FROM RegisteredCluster r WHERE r.clusterId = :clusterId"),
    @NamedQuery(name = "RegisteredCluster.findBySearchEndpoint", query = "SELECT r FROM RegisteredCluster r WHERE r.searchEndpoint = :searchEndpoint"),
    @NamedQuery(name = "RegisteredCluster.findByEmail", query = "SELECT r FROM RegisteredCluster r WHERE r.email = :email"),
    @NamedQuery(name = "RegisteredCluster.findByCert", query = "SELECT r FROM RegisteredCluster r WHERE r.cert = :cert"),
    @NamedQuery(name = "RegisteredCluster.findByGvodEndpoint", query = "SELECT r FROM RegisteredCluster r WHERE r.gvodEndpoint = :gvodEndpoint"),
    @NamedQuery(name = "RegisteredCluster.findByHeartbeatsMissed", query = "SELECT r FROM RegisteredCluster r WHERE r.heartbeatsMissed = :heartbeatsMissed"),
    @NamedQuery(name = "RegisteredCluster.findByDateRegistered", query = "SELECT r FROM RegisteredCluster r WHERE r.dateRegistered = :dateRegistered"),
    @NamedQuery(name = "RegisteredCluster.findByDateLastPing", query = "SELECT r FROM RegisteredCluster r WHERE r.dateLastPing = :dateLastPing")})
public class RegisteredCluster implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "cluster_id")
    private String clusterId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "search_endpoint")
    private String searchEndpoint;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "cert")
    private String cert;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "gvod_endpoint")
    private String gvodEndpoint;
    @Basic(optional = false)
    @NotNull
    @Column(name = "heartbeats_missed")
    private long heartbeatsMissed;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "date_registered")
    private String dateRegistered;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "date_last_ping")
    private String dateLastPing;

    public RegisteredCluster() {
    }

    public RegisteredCluster(String clusterId) {
        this.clusterId = clusterId;
    }

    public RegisteredCluster(String clusterId, String searchEndpoint, String email, String cert, String gvodEndpoint, long heartbeatsMissed, String dateRegistered, String dateLastPing) {
        this.clusterId = clusterId;
        this.searchEndpoint = searchEndpoint;
        this.email = email;
        this.cert = cert;
        this.gvodEndpoint = gvodEndpoint;
        this.heartbeatsMissed = heartbeatsMissed;
        this.dateRegistered = dateRegistered;
        this.dateLastPing = dateLastPing;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getSearchEndpoint() {
        return searchEndpoint;
    }

    public void setSearchEndpoint(String searchEndpoint) {
        this.searchEndpoint = searchEndpoint;
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

    public String getGvodEndpoint() {
        return gvodEndpoint;
    }

    public void setGvodEndpoint(String gvodEndpoint) {
        this.gvodEndpoint = gvodEndpoint;
    }

    public long getHeartbeatsMissed() {
        return heartbeatsMissed;
    }

    public void setHeartbeatsMissed(long heartbeatsMissed) {
        this.heartbeatsMissed = heartbeatsMissed;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getDateLastPing() {
        return dateLastPing;
    }

    public void setDateLastPing(String dateLastPing) {
        this.dateLastPing = dateLastPing;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clusterId != null ? clusterId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegisteredCluster)) {
            return false;
        }
        RegisteredCluster other = (RegisteredCluster) object;
        if ((this.clusterId == null && other.clusterId != null) || (this.clusterId != null && !this.clusterId.equals(other.clusterId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "site.hops.entities.RegisteredCluster[ clusterId=" + clusterId + " ]";
    }
    
}
