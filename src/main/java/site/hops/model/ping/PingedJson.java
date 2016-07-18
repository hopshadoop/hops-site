/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.model.ping;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import site.hops.entities.RegisteredCluster;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class PingedJson {
    
    
    private List<RegisteredCluster> clusters;
    public PingedJson(){
        
    }

    public PingedJson(List<RegisteredCluster> clusters) {
        this.clusters = clusters;
    }
    
    public List<RegisteredCluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<RegisteredCluster> clusters) {
        this.clusters = clusters;
    }
    
    
    
}
