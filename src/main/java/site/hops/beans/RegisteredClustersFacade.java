/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import site.hops.entities.RegisteredClusters;

/**
 *
 * @author jsvhqr
 */
@Stateless
public class RegisteredClustersFacade extends AbstractFacade<RegisteredClusters> {

    @PersistenceContext(unitName = "site.hopshadoop.hops-site_hops-site_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RegisteredClustersFacade() {
        super(RegisteredClusters.class);
    }
    
}