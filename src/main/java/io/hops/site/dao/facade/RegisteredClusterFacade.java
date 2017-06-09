package io.hops.site.dao.facade;

import io.hops.site.dao.entity.RegisteredCluster;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class RegisteredClusterFacade extends AbstractFacade<RegisteredCluster> {

    @PersistenceContext(unitName = "hops-sitePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RegisteredClusterFacade() {
        super(RegisteredCluster.class);
    }

    public List<RegisteredCluster> findByEmail(String email) {
        return em.createNamedQuery("RegisteredCluster.findByEmail").setParameter("email", email).getResultList();
    }
    
}
