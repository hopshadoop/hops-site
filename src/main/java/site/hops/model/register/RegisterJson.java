/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.model.register;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class RegisterJson {

    private String searchEndpoint;

    private String gvodEndpoint;

    private String email;

    private String cert;
    
    public RegisterJson(){
        
    }

    public String getSearchEndpoint() {
        return searchEndpoint;
    }

    public String getGVodEndpoint() {
        return gvodEndpoint;
    }

    public String getEmail() {
        return email;
    }

    public String getCert() {
        return cert;
    }

    public String getGvodEndpoint() {
        return gvodEndpoint;
    }

    public void setSearchEndpoint(String searchEndpoint) {
        this.searchEndpoint = searchEndpoint;
    }

    public void setGvodEndpoint(String gvodEndpoint) {
        this.gvodEndpoint = gvodEndpoint;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }
    
    
    
}
