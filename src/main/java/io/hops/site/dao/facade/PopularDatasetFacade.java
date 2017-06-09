package io.hops.site.dao.facade;

import io.hops.site.dao.entity.PopularDataset;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class PopularDatasetFacade extends AbstractFacade<PopularDataset> {

    @PersistenceContext(unitName = "hops-sitePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PopularDatasetFacade() {
        super(PopularDataset.class);
    }
    
}
