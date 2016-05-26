package site.hops.beans;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import site.hops.entities.RegisteredClusters;

/**
 *
 * @author jsvhqr
 */
@Stateless
public class HeartbeatBean {
    
    @EJB
    RegisteredClustersFacade registeredclustersFacade;

    @Schedule(minute = "*/5", hour="*", persistent = false)
    public void automaticTimeout() {
        for(RegisteredClusters cluster : registeredclustersFacade.findAll()){
            cluster.setHeartbeatsMissed(cluster.getHeartbeatsMissed() + 1);
        }
    }

}