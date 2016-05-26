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
    @NamedQuery(name = "PopularDatasets.findByDatasetId", query = "SELECT p FROM PopularDatasets p WHERE p.datasetId = :datasetId"),
    @NamedQuery(name = "PopularDatasets.findByComments", query = "SELECT p FROM PopularDatasets p WHERE p.comments = :comments"),
    @NamedQuery(name = "PopularDatasets.findByDownloads", query = "SELECT p FROM PopularDatasets p WHERE p.downloads = :downloads"),
    @NamedQuery(name = "PopularDatasets.findByUpload", query = "SELECT p FROM PopularDatasets p WHERE p.upload = :upload")})
public class PopularDatasets implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "dataset_id")
    private String datasetId;
    @Size(max = 1000)
    @Column(name = "comments")
    private String comments;
    @Basic(optional = false)
    @NotNull
    @Column(name = "downloads")
    private int downloads;
    @Basic(optional = false)
    @NotNull
    @Column(name = "upload")
    private int upload;

    public PopularDatasets() {
    }

    public PopularDatasets(String datasetId) {
        this.datasetId = datasetId;
    }

    public PopularDatasets(String datasetId, int downloads, int upload) {
        this.datasetId = datasetId;
        this.downloads = downloads;
        this.upload = upload;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public int getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        this.upload = upload;
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
        if (!(object instanceof PopularDatasets)) {
            return false;
        }
        PopularDatasets other = (PopularDatasets) object;
        if ((this.datasetId == null && other.datasetId != null) || (this.datasetId != null && !this.datasetId.equals(other.datasetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "site.hops.entities.PopularDatasets[ datasetId=" + datasetId + " ]";
    }
    
}
