package io.hops.site.controller;

import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class MissedHeartBeatsUpdater {
    
    @EJB
    RegisteredClusterFacade registeredclustersFacade;

    @Schedule(minute = "*/5", hour="*", persistent = false)
    public void automaticTimeout() {
        for(RegisteredCluster cluster : registeredclustersFacade.findAll()){
            cluster.setHeartbeatsMissed(cluster.getHeartbeatsMissed() + 1);
        }
    }

}