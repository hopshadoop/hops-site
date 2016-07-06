/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.model;


import org.json.JSONObject;
/**
 *
 * @author jsvhqr
 */
public class RegisterJson {

    private final String searchEndpoint;

    private final String gvodEndpoint;

    private final String email;

    private final String cert;

    public RegisterJson(String registerJson) {
        
        JSONObject jsonObject = new JSONObject(registerJson);
        
        this.searchEndpoint = jsonObject.getString("searchEndpoint");
        this.gvodEndpoint = jsonObject.getString("gvodEndpoint");
        this.email = jsonObject.getString("email");
        this.cert = jsonObject.getString("cert");
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
