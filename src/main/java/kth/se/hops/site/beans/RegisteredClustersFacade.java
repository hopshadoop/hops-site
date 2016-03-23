/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.se.hops.site.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kth.se.hops.site.entities.RegisteredClusters;

/**
 *
 * @author archer
 */
@Stateless
public class RegisteredClustersFacade extends AbstractFacade<RegisteredClusters> {

    @PersistenceContext(unitName = "se.kth_hops-site_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RegisteredClustersFacade() {
        super(RegisteredClusters.class);
    }
    
}
