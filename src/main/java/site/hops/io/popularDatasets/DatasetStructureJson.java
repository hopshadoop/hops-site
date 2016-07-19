/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.io.popularDatasets;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class DatasetStructureJson {
    
    private String name;
    private String description;
    private ManifestJson manifestJson;

    public DatasetStructureJson() {
    }

    public DatasetStructureJson(String name, String description, ManifestJson manifestJson) {
        this.name = name;
        this.description = description;
        this.manifestJson = manifestJson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ManifestJson getManifestJson() {
        return manifestJson;
    }

    public void setManifestJson(ManifestJson manifestJson) {
        this.manifestJson = manifestJson;
    }

    
    
    
    
    
            
    
}


