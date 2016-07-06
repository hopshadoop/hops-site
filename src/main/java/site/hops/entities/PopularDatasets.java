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
@Table(name = "popular_datasets")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PopularDatasets.findAll", query = "SELECT p FROM PopularDatasets p"),
    @NamedQuery(name = "PopularDatasets.findByName", query = "SELECT p FROM PopularDatasets p WHERE p.name = :name"),
    @NamedQuery(name = "PopularDatasets.findByDatasetId", query = "SELECT p FROM PopularDatasets p WHERE p.datasetId = :datasetId"),
    @NamedQuery(name = "PopularDatasets.findByFiles", query = "SELECT p FROM PopularDatasets p WHERE p.files = :files"),
    @NamedQuery(name = "PopularDatasets.findByLeeches", query = "SELECT p FROM PopularDatasets p WHERE p.leeches = :leeches"),
    @NamedQuery(name = "PopularDatasets.findBySeeds", query = "SELECT p FROM PopularDatasets p WHERE p.seeds = :seeds"),
    @NamedQuery(name = "PopularDatasets.findByDatasetStructure", query = "SELECT p FROM PopularDatasets p WHERE p.datasetStructure = :datasetStructure")})
public class PopularDatasets implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "dataset_id")
    private String datasetId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "files")
    private int files;
    @Basic(optional = false)
    @NotNull
    @Column(name = "leeches")
    private int leeches;
    @Basic(optional = false)
    @NotNull
    @Column(name = "seeds")
    private int seeds;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "dataset_structure")
    private String datasetStructure;

    public PopularDatasets() {
    }

    public PopularDatasets(String name) {
        this.name = name;
    }

    public PopularDatasets(String name, String datasetId, int files, int leeches, int seeds, String datasetStructure) {
        this.name = name;
        this.datasetId = datasetId;
        this.files = files;
        this.leeches = leeches;
        this.seeds = seeds;
        this.datasetStructure = datasetStructure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public int getFiles() {
        return files;
    }

    public void setFiles(int files) {
        this.files = files;
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

    public String getDatasetStructure() {
        return datasetStructure;
    }

    public void setDatasetStructure(String datasetStructure) {
        this.datasetStructure = datasetStructure;
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
        if (!(object instanceof PopularDatasets)) {
            return false;
        }
        PopularDatasets other = (PopularDatasets) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "site.hops.entities.PopularDatasets[ name=" + name + " ]";
    }
    
}
