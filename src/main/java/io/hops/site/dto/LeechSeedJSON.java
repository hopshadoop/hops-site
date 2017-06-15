package io.hops.site.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LeechSeedJSON {
    
    
    private IdentificationJSON identity;
    private String gvodUdpEndpoint;
    private String datasetId;

    public LeechSeedJSON() {
    }

    public LeechSeedJSON(IdentificationJSON identity, String gvod_udp_endpoint) {
        this.identity = identity;
        this.gvodUdpEndpoint = gvod_udp_endpoint;
    }

    public IdentificationJSON getIdentity() {
        return identity;
    }

    public void setIdentity(IdentificationJSON identity) {
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
