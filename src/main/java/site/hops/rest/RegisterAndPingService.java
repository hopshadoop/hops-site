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
import site.hops.model.RegisterJson;
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
    public Response Register(String json) {
        
        RegisterJson registerJson = new RegisterJson(json);
        
        if (!helperFunctions.ClusterRegisteredWithEmail(registerJson.getEmail())) {

            if (helperFunctions.isValid(registerJson.getCert())) {

                String registeredId = helperFunctions.registerCluster(registerJson.getSearchEndpoint(), registerJson.getEmail(), registerJson.getCert(), registerJson.getGVodEndpoint());

                return Response.status(200).entity(registeredId).build();
            } else {
                return Response.status(403).entity(null).build();
            }

        } else {
            return Response.status(403).entity(null).build();
        }

    }

    @GET
    @Path("/ping/{cluster_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response Ping(@PathParam("cluster_id") String cluster_id) {

       
        if(!helperFunctions.ClusterRegisteredWithId(cluster_id)){
                return Response.status(403).entity(null).build();
        }else{
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            RegisteredClusters rc = this.registeredClustersFacade.find(cluster_id);
            rc.setHeartbeatsMissed(0);
            rc.setDateLastPing(dateFormat.format(date));
            registeredClustersFacade.edit(rc);
            List<RegisteredClusters> clusters = helperFunctions.getAllRegisteredClusters();
                GenericEntity<List<RegisteredClusters>> to_return = new GenericEntity<List<RegisteredClusters>>(clusters) {
                };
                return Response.status(200).entity(to_return).build();
        }
        

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

}
