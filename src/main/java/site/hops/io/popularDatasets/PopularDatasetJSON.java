/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.io.popularDatasets;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import site.hops.io.identity.IdentificationJSON;
import site.hops.io.register.AddressJSON;
/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class PopularDatasetJSON {
    
    private String datasetId;
    
    private ManifestJSON manifestJson;
    
    private int leeches;
    
    private int seeds;
    
    private List<AddressJSON> gvodEndpoints;
    
    private IdentificationJSON identification;

    public PopularDatasetJSON() {
        
        
    }

    public PopularDatasetJSON(ManifestJSON manifestJson, String datasetId, int leeches, int seeds, List<AddressJSON> partners) {
        this.manifestJson = manifestJson;
        this.datasetId = datasetId;
        this.leeches = leeches;
        this.seeds = seeds;
        this.gvodEndpoints = partners;
    }

    public IdentificationJSON getIdentification() {
        return identification;
    }

    public void setIdentification(IdentificationJSON identification) {
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
    public List<AddressJSON> getGvodEndpoints() {
        return gvodEndpoints;
    }

    public void setGvodEndpoints(List<AddressJSON> gvodEndpoints) {
        this.gvodEndpoints = gvodEndpoints;
    }

    public ManifestJSON getManifestJson() {
        return manifestJson;
    }

    public void setManifestJson(ManifestJSON manifestJson) {
        this.manifestJson = manifestJson;
    }
    
    
    
    
    
}
