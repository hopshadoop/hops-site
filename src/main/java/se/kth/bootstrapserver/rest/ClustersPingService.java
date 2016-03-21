/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.bootstrapserver.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
/**
 *
 * @author jsvhqr
 */

@Path("/available_clusters")
@Produces(MediaType.APPLICATION_JSON)
public class ClustersPingService {
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response ping(@PathParam("name") String name,@PathParam("restendpoint") String rep, @PathParam("email") String mail, @PathParam("cert") String cert, @PathParam("udp_support") Boolean udps){
        
        
        
        
        
        return null;
    }
    
}
