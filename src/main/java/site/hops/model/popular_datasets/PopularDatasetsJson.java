/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.model.popular_datasets;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class PopularDatasetsJson {
    
    private String clusterId;
    
    private String datasetName;
    
    private String structure;
    
    private String datasetId;
    
    private int files;
    
    private int leeches;
    
    private int seeds;

    public PopularDatasetsJson() {
        
        
    }

    public PopularDatasetsJson(String clusterId, String datasetName, String structure, String datasetId, int files, int leeches, int seeds) {
        this.clusterId = clusterId;
        this.datasetName = datasetName;
        this.structure = structure;
        this.datasetId = datasetId;
        this.files = files;
        this.leeches = leeches;
        this.seeds = seeds;
    }

    public String getClusterId() {
        return clusterId;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public String getStructure() {
        return structure;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public int getFiles() {
        return files;
    }

    public int getLeeches() {
        return leeches;
    }

    public int getSeeds() {
        return seeds;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public void setFiles(int files) {
        this.files = files;
    }

    public void setLeeches(int leeches) {
        this.leeches = leeches;
    }

    public void setSeeds(int seeds) {
        this.seeds = seeds;
    }
    
    
    
    
    
}
