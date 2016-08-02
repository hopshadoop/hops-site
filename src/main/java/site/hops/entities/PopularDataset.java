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
import javax.persistence.Lob;
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
@Table(name = "popular_dataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PopularDataset.findAll", query = "SELECT p FROM PopularDataset p"),
    @NamedQuery(name = "PopularDataset.findByDatasetId", query = "SELECT p FROM PopularDataset p WHERE p.datasetId = :datasetId"),
    @NamedQuery(name = "PopularDataset.findByLeeches", query = "SELECT p FROM PopularDataset p WHERE p.leeches = :leeches"),
    @NamedQuery(name = "PopularDataset.findBySeeds", query = "SELECT p FROM PopularDataset p WHERE p.seeds = :seeds")})
public class PopularDataset implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "dataset_id")
    private String datasetId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "manifest")
    private String manifest;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "partners")
    private String partners;
    @Basic(optional = false)
    @NotNull
    @Column(name = "leeches")
    private int leeches;
    @Basic(optional = false)
    @NotNull
    @Column(name = "seeds")
    private int seeds;

    public PopularDataset() {
    }

    public PopularDataset(String datasetId) {
        this.datasetId = datasetId;
    }

    public PopularDataset(String datasetId, String manifest, String partners, int leeches, int seeds) {
        this.datasetId = datasetId;
        this.manifest = manifest;
        this.partners = partners;
        this.leeches = leeches;
        this.seeds = seeds;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getManifest() {
        return manifest;
    }

    public void setManifest(String manifest) {
        this.manifest = manifest;
    }

    public String getPartners() {
        return partners;
    }

    public void setPartners(String partners) {
        this.partners = partners;
    }

    public int getLeeches() {
        return leeches;
    }

    public void setLeeches(int leeches) {
        this.leeches = leeches;
    }

    public int getSeeds() {
        return seeds;
    }

    public void setSeeds(int seeds) {
        this.seeds = seeds;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datasetId != null ? datasetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PopularDataset)) {
            return false;
        }
        PopularDataset other = (PopularDataset) object;
        if ((this.datasetId == null && other.datasetId != null) || (this.datasetId != null && !this.datasetId.equals(other.datasetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "site.hops.entities.PopularDataset[ datasetId=" + datasetId + " ]";
    }
    
}
