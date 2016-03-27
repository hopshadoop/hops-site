package site.hops.beans;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import site.hops.entities.Registeredclusters;

/**
 *
 * @author jsvhqr
 */
@Stateless
public class HeartbeatBean {
    
    @EJB
    RegisteredclustersFacade registeredclustersFacade;

    @Schedule(minute = "*/1", hour="*", persistent = false)
    public void automaticTimeout() {
        for(Registeredclusters cluster : registeredclustersFacade.findAll()){
            cluster.setHeartbeatsmissed(cluster.getHeartbeatsmissed() + 1);
        }
    }

}