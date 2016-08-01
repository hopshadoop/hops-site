/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.io.register;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class AddressJSON {
    
    private String ip;
    private int port;
    private int id;

    public AddressJSON() {
    }
    
    public AddressJSON(String ip, int port, int id) {
        this.ip = ip;
        this.port = port;
        this.id = id;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getId() {
        return id;
    }
    
    
    
    
}
