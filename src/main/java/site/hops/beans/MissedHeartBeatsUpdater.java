package site.hops.beans;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import site.hops.entities.RegisteredCluster;

/**
 *
 * @author jsvhqr
 */
@Stateless
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