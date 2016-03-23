/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.se.hops.site.rest;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import kth.se.hops.site.beans.RegisteredClustersFacade;
import kth.se.hops.site.entities.RegisteredClusters;

/**
 *
 * @author archer
 */

@Path("/")
public class SiteEndpoint {
    
    
    @EJB
    RegisteredClustersFacade registeredClustersFacade;
    
    
    @GET
    @Path("ping")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response Ping(@PathParam("name") String name, @PathParam("restEndpoint") String restEndpoint, @PathParam("email") String email, @PathParam("cert") String cert, @PathParam("udp_support") Boolean udp_support){
        
        List<RegisteredClusters> response = null;
        
        if(null != registeredClustersFacade.find(name)){
            response = registeredClustersFacade.findAll();
        }
        else
        {
            registeredClustersFacade.create(new RegisteredClusters(name,restEndpoint,email,cert,udp_support));
            response = registeredClustersFacade.findAll();
        }
        
        
        
        return Response.status(200).entity(response).build();
    
}
    
}
