/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.model.ping;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import site.hops.entities.RegisteredClusters;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class SuccessPingJson {
    
    
    private List<RegisteredClusters> clusters;
    public SuccessPingJson(){
        
    }

    public SuccessPingJson(List<RegisteredClusters> clusters) {
        this.clusters = clusters;
    }
    
    @XmlElement(name = "clusters")
    public List<RegisteredClusters> getClusters() {
        return clusters;
    }
    
    
    
}
