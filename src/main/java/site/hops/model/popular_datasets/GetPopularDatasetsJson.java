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
public class GetPopularDatasetsJson {
    
    
    private String clusterId;
    
    public GetPopularDatasetsJson(){
        
    }

    public GetPopularDatasetsJson(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterId() {
        return clusterId;
    }
    
    
}
