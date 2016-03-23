/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.se.hops.site.rest.service;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import kth.se.hops.site.beans.RegisteredClustersFacade;
import kth.se.hops.site.entities.RegisteredClusters;

/**
 *
 * @author archer
 * 
 */



@Stateless
@Path("kth.se.hops.site.entities.registeredclusters")
public class RegisteredClustersFacadeREST  {

    @EJB
    RegisteredClustersFacade registeredClustersFacade;
    
    @Path("/ping")
    @GET
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

    
    
}
