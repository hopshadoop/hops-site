/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import site.hops.beans.PopularDatasetFacade;
import site.hops.beans.RegisteredClusterFacade;
import site.hops.entities.PopularDataset;
import site.hops.entities.RegisteredCluster;

/**
 *
 * @author jsvhqr
 */
@Stateless
public class HelperFunctions {
    
    @EJB
    RegisteredClusterFacade registeredClusterFacade;
    @EJB
    PopularDatasetFacade popularDatasetFacade;
    
    public boolean isValid(String cert) {
        return true;
    }

    public boolean ClusterRegisteredWithEmail(String email) {
        RegisteredCluster registeredCluster = this.registeredClusterFacade.findByEmail(email);
        return registeredCluster != null;

    }

    public String registerCluster(String search_endpoint, String email, String cert, String gvod_endpoint) {

        String uniqueId = UUID.randomUUID().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        this.registeredClusterFacade.create(new RegisteredCluster(uniqueId, search_endpoint, email, cert, gvod_endpoint, 0, dateFormat.format(date), dateFormat.format(date)));
        
        return uniqueId;
    }

    public List<RegisteredCluster> getAllRegisteredClusters() {

        return this.registeredClusterFacade.findAll();
    }
    
    public List<PopularDataset> getTopTenDatasets(){
        
        List<PopularDataset> to_ret = new LinkedList<>(popularDatasetFacade.findAll());
        
        Collections.sort(to_ret, new PopularDataset());
        
       if(to_ret.size() <= 10){
           return to_ret;
       }else{
           List<PopularDataset> top_ten = new LinkedList<>();
           for(int i=0; i<10; i++){
               top_ten.add(to_ret.get(i));
           }
           return top_ten;
       }
    }

    public boolean ClusterRegisteredWithId(String cluster_id) {
        
        RegisteredCluster registeredCluster = this.registeredClusterFacade.find(cluster_id);
        return registeredCluster != null;
        
    }
    
}
