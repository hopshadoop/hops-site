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
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import site.hops.beans.RegisteredClusterFacade;
import site.hops.entities.RegisteredCluster;
import site.hops.io.failure.FailJson;
import site.hops.io.identity.Identification;
import site.hops.io.ping.PingedJson;
import site.hops.io.register.RegisterJson;
import site.hops.io.register.RegisteredJson;
import site.hops.tools.HelperFunctions;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class RegisterAndPingService {
    
    @EJB
    HelperFunctions helperFunctions;
    @EJB
    RegisteredClusterFacade registeredClustersFacade;

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Register(RegisterJson registerJson) {
        
        if (!helperFunctions.ClusterRegisteredWithEmail(registerJson.getEmail())) {

            if (helperFunctions.isValid(registerJson.getCert())) {

                String registeredId = helperFunctions.registerCluster(registerJson.getSearchEndpoint(), registerJson.getEmail(), registerJson.getCert(), registerJson.getGVodEndpoint());

                return Response.status(200).entity(new RegisteredJson(registeredId)).build();
                
            } else {
                
                
                return Response.status(403).entity(new FailJson("invalid cert")).build();
            }

        } else {
            
            return Response.status(403).entity(new FailJson("already registered")).build();
        }

    }

    @PUT
    @Path("ping")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response Ping(Identification identification) {
       
        if(!helperFunctions.ClusterRegisteredWithId(identification.getClusterId())){
                return Response.status(403).entity(new FailJson("invalid id")).build();
        }else{
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            RegisteredCluster registeredCluster = this.registeredClustersFacade.find(identification.getClusterId());
            registeredCluster.setHeartbeatsMissed(0);
            registeredCluster.setDateLastPing(dateFormat.format(date));
            registeredClustersFacade.edit(registeredCluster);
            List<RegisteredCluster> registeredClusters = helperFunctions.getAllRegisteredClusters();
            return Response.status(200).entity(new PingedJson(registeredClusters)).build();
        }
        

    }
}
