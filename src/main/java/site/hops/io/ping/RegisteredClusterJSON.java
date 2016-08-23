/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.io.ping;

import javax.xml.bind.annotation.XmlRootElement;
import site.hops.io.register.AddressJSON;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class RegisteredClusterJSON {

       private String clusterId;
       
       private String searchEndpoint;
       
       private AddressJSON gvodEndpoint;
       
       private long heartbeatsMissed;
       
       private String dateRegistered;
       
       private String dateLastPing;

    public RegisteredClusterJSON(String clusterId, AddressJSON gvodEndpoint, long heartbeatsMissed, String dateRegistered, String dateLastPing, String searchEndpoint) {
        this.clusterId = clusterId;
        this.gvodEndpoint = gvodEndpoint;
        this.heartbeatsMissed = heartbeatsMissed;
        this.dateRegistered = dateRegistered;
        this.dateLastPing = dateLastPing;
        this.searchEndpoint = searchEndpoint;
    }
    
    
    public RegisteredClusterJSON() {
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public void setSearchEndpoint(String searchEndpoint) {
        this.searchEndpoint = searchEndpoint;
    }

    public void setGvodEndpoint(AddressJSON gvodEndpoint) {
        this.gvodEndpoint = gvodEndpoint;
    }

    public void setHeartbeatsMissed(long heartbeatsMissed) {
        this.heartbeatsMissed = heartbeatsMissed;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public void setDateLastPing(String dateLastPing) {
        this.dateLastPing = dateLastPing;
    }

    
    
    public String getSearchEndpoint() {
        return searchEndpoint;
    }
    
    
    
    public String getClusterId() {
        return clusterId;
    }

    public AddressJSON getGvodEndpoint() {
        return gvodEndpoint;
    }

    public long getHeartbeatsMissed() {
        return heartbeatsMissed;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public String getDateLastPing() {
        return dateLastPing;
    }
       
       
       
}
