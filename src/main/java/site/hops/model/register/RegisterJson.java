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

}
