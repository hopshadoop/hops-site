/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.rest;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import site.hops.beans.RegisteredclustersFacade;
import site.hops.entities.Registeredclusters;

/**
 * Root resource (exposed at "myresource" path)
 */

@Path("myresource")
public class ClusterService {

    @EJB
    RegisteredclustersFacade registeredClustersFacade;
    
    
    
    /*@GET
    @Path("/ping/{name}/{restEndpoint}/{email}/{heartbeatmissed}/{dateregistered}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response Ping(@PathParam("name") String name, @PathParam("restEndpoint") String restEndpoint,@PathParam("email") String email, @PathParam("heartbeatMissed") Long heartbeatMissed, @PathParam("dateRegistered") String dateRegistered) {
        
        List<Registeredclusters> clusters;
        
        if(null != registeredClustersFacade.find(name)){
            
            clusters = registeredClustersFacade.findAll();
        }
        else{
            Registeredclusters rc = new Registeredclusters(name,restEndpoint,email,heartbeatMissed,dateRegistered);
            registeredClustersFacade.create(rc);
            clusters = registeredClustersFacade.findAll();
        }
        
        return Response.status(200).entity(clusters).build();
    }*/
    
    @GET
    @Path("/ping/{name}/{restEndpoint}/{email}/{cert}/{udpEndpoint}/{heartbeatMissed}/{dateRegistered}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response Ping(@PathParam("name") String name, @PathParam("restEndpoint") String restEndpoint,@PathParam("email") String email,@PathParam("cert") String cert,@PathParam("udpEndpoint") String udpEndpoint, @PathParam("heartbeatMissed") Long heartbeatMissed, @PathParam("dateRegistered") String dateRegistered) {
        
        List<Registeredclusters> clusters;
        
        if(null != registeredClustersFacade.find(name)){
            
            clusters = registeredClustersFacade.findAll();
        }
        else{
            Registeredclusters rc = new Registeredclusters(name,restEndpoint,email,heartbeatMissed,dateRegistered);
            rc.setCert(cert);
            rc.setUdpendpoint(udpEndpoint);
            registeredClustersFacade.create(rc);
            clusters = registeredClustersFacade.findAll();
        }
        GenericEntity<List<Registeredclusters>> to_return = new GenericEntity<List<Registeredclusters>>(clusters) {};
        
        return Response.status(200).entity(to_return).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
    
    
}
    
    
    

