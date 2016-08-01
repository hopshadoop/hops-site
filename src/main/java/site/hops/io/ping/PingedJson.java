/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.io.ping;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class PingedJson {
    
    
    private List<RegisteredClusterJson> clusters;
    public PingedJson(){
        
    }

    public PingedJson(List<RegisteredClusterJson> clusters) {
        this.clusters = clusters;
    }
    
    public List<RegisteredClusterJson> getClusters() {
        return clusters;
    }

    public void setClusters(List<RegisteredClusterJson> clusters) {
        this.clusters = clusters;
    }
    
    
    
}
