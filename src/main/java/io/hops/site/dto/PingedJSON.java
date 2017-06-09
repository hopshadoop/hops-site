/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.site.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class PingedJSON {
    
    
    private List<RegisteredClusterJSON> clusters;
    public PingedJSON(){
        
    }

    public PingedJSON(List<RegisteredClusterJSON> clusters) {
        this.clusters = clusters;
    }
    
    public List<RegisteredClusterJSON> getClusters() {
        return clusters;
    }

    public void setClusters(List<RegisteredClusterJSON> clusters) {
        this.clusters = clusters;
    }
    
    
    
}
