/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.io.popularDatasets;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import site.hops.io.identity.IdentificationJson;
/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class PopularDatasetJson {
    
    private String datasetId;
    
    private ManifestJson manifestJson;
    
    private int leeches;
    
    private int seeds;
    
    private List<String> gvodEndpoints;
    
    private IdentificationJson identification;

    public PopularDatasetJson() {
        
        
    }

    public PopularDatasetJson(ManifestJson manifestJson, String datasetId, int leeches, int seeds, List<String> partners) {
        this.manifestJson = manifestJson;
        this.datasetId = datasetId;
        this.leeches = leeches;
        this.seeds = seeds;
        this.gvodEndpoints = partners;
    }

    public IdentificationJson getIdentification() {
        return identification;
    }

    public void setIdentification(IdentificationJson identification) {
        this.identification = identification;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public int getLeeches() {
        return leeches;
    }

    public int getSeeds() {
        return seeds;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public void setLeeches(int leeches) {
        this.leeches = leeches;
    }

    public void setSeeds(int seeds) {
        this.seeds = seeds;
    }
    
    @XmlElement(name = "gvodEndpoints")
    public List<String> getGvodEndpoints() {
        return gvodEndpoints;
    }

    public void setGvodEndpoints(List<String> gvodEndpoints) {
        this.gvodEndpoints = gvodEndpoints;
    }

    public ManifestJson getManifestJson() {
        return manifestJson;
    }

    public void setManifestJson(ManifestJson manifestJson) {
        this.manifestJson = manifestJson;
    }
    
    
    
    
    
}