/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.entities;

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

/**
 *
 * @author jsvhqr
 */
@Entity
@Table(name = "partner")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Partner.findAll", query = "SELECT p FROM Partner p"),
    @NamedQuery(name = "Partner.findByPartnerId", query = "SELECT p FROM Partner p WHERE p.partnerId = :partnerId"),
    @NamedQuery(name = "Partner.findByGvodUdpEndpoint", query = "SELECT p FROM Partner p WHERE p.gvodUdpEndpoint = :gvodUdpEndpoint")})
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "partner_id")
    private Integer partnerId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "gvod_udp_endpoint")
    private String gvodUdpEndpoint;
    @ManyToMany(mappedBy = "partnerCollection")
    private Collection<PopularDataset> popularDatasetCollection;

    public Partner() {
    }

    public Partner(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Partner(Integer partnerId, String gvodUdpEndpoint) {
        this.partnerId = partnerId;
        this.gvodUdpEndpoint = gvodUdpEndpoint;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getGvodUdpEndpoint() {
        return gvodUdpEndpoint;
    }

    public void setGvodUdpEndpoint(String gvodUdpEndpoint) {
        this.gvodUdpEndpoint = gvodUdpEndpoint;
    }

    @XmlTransient
    public Collection<PopularDataset> getPopularDatasetCollection() {
        return popularDatasetCollection;
    }

    public void setPopularDatasetCollection(Collection<PopularDataset> popularDatasetCollection) {
        this.popularDatasetCollection = popularDatasetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerId != null ? partnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.partnerId == null && other.partnerId != null) || (this.partnerId != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "site.hops.entities.Partner[ partnerId=" + partnerId + " ]";
    }
    
}
