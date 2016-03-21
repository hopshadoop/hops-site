/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.bootstrapserver.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import se.kth.bootstrapserver.entities.RegisteredClusters;

/**
 *
 * @author jsvhqr
 */
@Stateless
public class RegisteredClustersFacade extends AbstractFacade<RegisteredClusters> {

    @PersistenceContext(unitName = "hops-sitePU")
    private EntityManager em;
    
    @Override
    public List<RegisteredClusters> findAll(){
        
        TypedQuery<RegisteredClusters> query = em.createNamedQuery("RegisteredClusters.findAll",RegisteredClusters.class);
        
        return query.getResultList();
        
    }
    
    public void registerCluster(String name, String restendpoint, String email, String cert, boolean udpsupport){
        
        RegisteredClusters rc = new RegisteredClusters(name,restendpoint,email,cert,udpsupport);
        
        em.persist(rc);
        
        
    }
    
    
    public Boolean clusterExists(String name){
        
        return em.find(RegisteredClusters.class, name) != null;
        
    }
    
    
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RegisteredClustersFacade() {
        super(RegisteredClusters.class);
    }
    
}
