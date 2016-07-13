/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import site.hops.beans.RegisteredClustersFacade;
import site.hops.entities.RegisteredClusters;
import site.hops.model.ping.FailurePingJson;
import site.hops.model.ping.PingJson;
import site.hops.model.ping.SuccessPingJson;
import site.hops.model.register.FailureRegisterJson;
import site.hops.model.register.RegisterJson;
import site.hops.model.register.SuccessRegisterJson;
import site.hops.tools.HelperFunctions;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class RegisterAndPingService {
    
    @EJB
    HelperFunctions helperFunctions;
    @EJB
    RegisteredClustersFacade registeredClustersFacade;

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Register(RegisterJson registerJson) {
        
        if (!helperFunctions.ClusterRegisteredWithEmail(registerJson.getEmail())) {

            if (helperFunctions.isValid(registerJson.getCert())) {

                String registeredId = helperFunctions.registerCluster(registerJson.getSearchEndpoint(), registerJson.getEmail(), registerJson.getCert(), registerJson.getGVodEndpoint());

                return Response.status(200).entity(new SuccessRegisterJson(registeredId)).build();
                
            } else {
                
                
                return Response.status(403).entity(new FailureRegisterJson("invalid cert")).build();
            }

        } else {
            
            return Response.status(403).entity(new FailureRegisterJson("already registered")).build();
        }

    }

    @GET
    @Path("ping")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response Ping(PingJson pingJson) {
       
        if(!helperFunctions.ClusterRegisteredWithId(pingJson.getClusterId())){
                return Response.status(403).entity(new FailurePingJson("invalid id")).build();
        }else{
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            RegisteredClusters rc = this.registeredClustersFacade.find(pingJson.getClusterId());
            rc.setHeartbeatsMissed(0);
            rc.setDateLastPing(dateFormat.format(date));
            registeredClustersFacade.edit(rc);
            List<RegisteredClusters> clusters = helperFunctions.getAllRegisteredClusters();
            return Response.status(200).entity(new SuccessPingJson(clusters)).build();
        }
        

    }
}
