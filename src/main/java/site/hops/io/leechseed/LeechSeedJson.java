/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.io.leechseed;

import javax.xml.bind.annotation.XmlRootElement;
import site.hops.io.identity.IdentificationJson;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class LeechSeedJson {
    
    
    private IdentificationJson identity;
    private String gvodUdpEndpoint;
    private String datasetId;

    public LeechSeedJson() {
    }

    public LeechSeedJson(IdentificationJson identity, String gvod_udp_endpoint) {
        this.identity = identity;
        this.gvodUdpEndpoint = gvod_udp_endpoint;
    }

    public IdentificationJson getIdentity() {
        return identity;
    }

    public void setIdentity(IdentificationJson identity) {
        this.identity = identity;
    }

    public String getGvodUdpEndpoint() {
        return gvodUdpEndpoint;
    }

    public void setGvodUdpEndpoint(String gvodUdpEndpoint) {
        this.gvodUdpEndpoint = gvodUdpEndpoint;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }
    
    
    
    
    
    
    
}
