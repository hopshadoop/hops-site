/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import site.hops.entities.Partner;

/**
 *
 * @author jsvhqr
 */
@Stateless
public class PartnerFacade extends AbstractFacade<Partner> {

    @PersistenceContext(unitName = "site.hopshadoop.hops-site_hops-site_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PartnerFacade() {
        super(Partner.class);
    }
    
    public Partner getPartnerByEndpoint(String endpoint){
        
        return (Partner) em.createNamedQuery("Partner.findByGvodUdpEndpoint").getSingleResult();
    }
    
}