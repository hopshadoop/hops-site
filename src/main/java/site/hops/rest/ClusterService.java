/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.rest;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
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
    @Path("/register/{search_endpoint}/{email}/{cert}/{gvod_endpoint}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response Register(@PathParam("search") String search_endpoint, @PathParam("email") String email, @PathParam("cert") String cert, @PathParam("gvod_endpoint") String gvod_endpoint) {

        List<RegisteredClusters> clusters;

        search_endpoint = search_endpoint.replaceAll("'", "/");
        gvod_endpoint = gvod_endpoint.replaceAll("'", "/");

        String uniqueID = UUID.randomUUID().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        if (this.registeredClustersFacade.find(uniqueID) != null) {
            this.registeredClustersFacade.create(new RegisteredClusters(uniqueID, search_endpoint, email, cert, gvod_endpoint, 0, dateFormat.format(date), dateFormat.format(date)));
        }

        return Response.status(200).entity(uniqueID).build();
    }

    @GET
    @Path("/ping/{cluster_id}/{search_endpoint}/{email}/{cert}/{gvod_endpoint}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response Ping(@PathParam("cluster_id") String cluster_id, @PathParam("search_endpoint") String search_endpoint, @PathParam("email") String email, @PathParam("cert") String cert, @PathParam("gvod_endpoint") String gvod_endpoint) {

        List<RegisteredClusters> clusters;

        search_endpoint = search_endpoint.replaceAll("'", "/");
        gvod_endpoint = gvod_endpoint.replaceAll("'", "/");

        RegisteredClusters clusterExist = registeredClustersFacade.find(cluster_id);
        clusterExist.setHeartbeatsMissed(0);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        clusterExist.setDateLastPing(dateFormat.format(date));
        registeredClustersFacade.edit(clusterExist);
        clusters = registeredClustersFacade.findAll();
        GenericEntity<List<RegisteredClusters>> to_return = new GenericEntity<List<RegisteredClusters>>(clusters) {
        };

        return Response.status(200).entity(to_return).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

}
