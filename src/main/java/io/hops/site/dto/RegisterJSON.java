package io.hops.site.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterJSON {

    private String searchEndpoint;

    private AddressJSON gvodEndpoint;

    private String email;

    private String cert;
    
    public RegisterJSON(){
        
    }

    public String getSearchEndpoint() {
        return searchEndpoint;
    }

    public AddressJSON getGVodEndpoint() {
        return gvodEndpoint;
    }

    public String getEmail() {
        return email;
    }

    public String getCert() {
        return cert;
    }

    public AddressJSON getGvodEndpoint() {
        return gvodEndpoint;
    }

    public void setSearchEndpoint(String searchEndpoint) {
        this.searchEndpoint = searchEndpoint;
    }

    public void setGvodEndpoint(AddressJSON gvodEndpoint) {
        this.gvodEndpoint = gvodEndpoint;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }
    
    
    
}
