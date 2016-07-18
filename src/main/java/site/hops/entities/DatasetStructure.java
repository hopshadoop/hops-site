/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "dataset_structure")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatasetStructure.findAll", query = "SELECT d FROM DatasetStructure d"),
    @NamedQuery(name = "DatasetStructure.findByDatasetName", query = "SELECT d FROM DatasetStructure d WHERE d.datasetName = :datasetName"),
    @NamedQuery(name = "DatasetStructure.findByDatasetDescription", query = "SELECT d FROM DatasetStructure d WHERE d.datasetDescription = :datasetDescription"),
    @NamedQuery(name = "DatasetStructure.findByDatasetId", query = "SELECT d FROM DatasetStructure d WHERE d.datasetId = :datasetId")})
public class DatasetStructure implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "dataset_name")
    private String datasetName;
    @Size(max = 1000)
    @Column(name = "dataset_description")
    private String datasetDescription;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "dataset_id")
    private String datasetId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datasetName")
    private Collection<File> fileCollection;
    @JoinColumn(name = "dataset_id", referencedColumnName = "dataset_id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private PopularDataset popularDataset;

    public DatasetStructure() {
    }

    public DatasetStructure(String datasetId) {
        this.datasetId = datasetId;
    }

    public DatasetStructure(String datasetId, String datasetName) {
        this.datasetId = datasetId;
        this.datasetName = datasetName;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getDatasetDescription() {
        return datasetDescription;
    }

    public void setDatasetDescription(String datasetDescription) {
        this.datasetDescription = datasetDescription;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    @XmlTransient
    public Collection<File> getFileCollection() {
        return fileCollection;
    }

    public void setFileCollection(Collection<File> fileCollection) {
        this.fileCollection = fileCollection;
    }

    public PopularDataset getPopularDataset() {
        return popularDataset;
    }

    public void setPopularDataset(PopularDataset popularDataset) {
        this.popularDataset = popularDataset;
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
        if (!(object instanceof DatasetStructure)) {
            return false;
        }
        DatasetStructure other = (DatasetStructure) object;
        if ((this.datasetId == null && other.datasetId != null) || (this.datasetId != null && !this.datasetId.equals(other.datasetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "site.hops.entities.DatasetStructure[ datasetId=" + datasetId + " ]";
    }
    
}
