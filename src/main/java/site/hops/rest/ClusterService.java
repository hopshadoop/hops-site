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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import site.hops.beans.RegisteredClustersFacade;
import site.hops.entities.RegisteredClusters;

/**
 * Root resource (exposed at "myresource" path)
 */

@Path("myresource")
public class ClusterService {

    @EJB
    RegisteredClustersFacade registeredClustersFacade;
    
    
    @GET
    @Path("/ping/{name}/{restEndpoint}/{email}/{cert}/{udpsupport}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response Ping(@PathParam("name") String name, @PathParam("restEndpoint") String restEndpoint,@PathParam("email") String email,@PathParam("cert") String cert,@PathParam("udpSupport") Boolean udpSupport) {
        
        List<RegisteredClusters> clusters;
        
        if(null != registeredClustersFacade.find(name)){
            
            clusters = registeredClustersFacade.findAll();
        }
        else{
            registeredClustersFacade.create(new RegisteredClusters(name,restEndpoint,email,cert,udpSupport) );
            clusters = registeredClustersFacade.findAll();
        }
        
        return Response.status(200).entity(clusters).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
    
    
}
    
    
    

