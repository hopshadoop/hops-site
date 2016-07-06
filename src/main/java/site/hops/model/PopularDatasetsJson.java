/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.model;

import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONObject;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class PopularDatasetsJson {
    
    private final String datasetName;
    
    private final String structure;
    
    private final String datasetId;
    
    private final int files;
    
    private final int leeches;
    
    private final int seeds;

    public PopularDatasetsJson(String json) {
        
        JSONObject jsonObject = new JSONObject(json);
        
        datasetName = jsonObject.getString("datsetName");
        structure = jsonObject.getString("datasetStructure");
        datasetId = jsonObject.getString("datasetId");
        files = jsonObject.getJSONArray("childrenFiles").length();
        seeds = Integer.parseInt(jsonObject.getString("seeds"));
        leeches = Integer.parseInt(jsonObject.getString("leeches"));
        
        
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
    
    
    
    
    
}
