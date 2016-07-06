/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import site.hops.beans.RegisteredClustersFacade;
import site.hops.entities.RegisteredClusters;

/**
 *
 * @author jsvhqr
 */
@Stateless
public class HelperFunctions {
    
    @EJB
    RegisteredClustersFacade registeredClustersFacade;
    
    public boolean isValid(String cert) {
        return true;
    }

    public boolean ClusterRegisteredWithEmail(String email) {
        RegisteredClusters rq = this.registeredClustersFacade.findByEmail(email);
        return rq != null;

    }

    public String registerCluster(String search_endpoint, String email, String cert, String gvod_endpoint) {

        String uniqueId = UUID.randomUUID().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        this.registeredClustersFacade.create(new RegisteredClusters(uniqueId, search_endpoint, email, cert, gvod_endpoint, 0, dateFormat.format(date), dateFormat.format(date)));
        
        return uniqueId;
    }

    public List<RegisteredClusters> getAllRegisteredClusters() {

        return this.registeredClustersFacade.findAll();
    }

    public boolean ClusterRegisteredWithId(String cluster_id) {
        
        RegisteredClusters rq = this.registeredClustersFacade.find(cluster_id);
        return rq != null;
        
    }
    
}
