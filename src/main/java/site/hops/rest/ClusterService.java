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
    public Response Register(@PathParam("search_endpoint") String search_endpoint, @PathParam("email") String email, @PathParam("cert") String cert, @PathParam("gvod_endpoint") String gvod_endpoint) {

        List<RegisteredClusters> clusters;

        search_endpoint = search_endpoint.replaceAll("'", "/");
        gvod_endpoint = gvod_endpoint.replaceAll("'", "/");

        String uniqueId = UUID.randomUUID().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        RegisteredClusters registeredCluster = this.registeredClustersFacade.find(uniqueId);
        if (registeredCluster != null && registeredCluster.getEmail().equals(email) && registeredCluster.getCert().equals(cert)) {
            registeredCluster.setHeartbeatsMissed(0);
            registeredCluster.setDateLastPing(dateFormat.format(date));
            this.registeredClustersFacade.edit(registeredCluster);
            return Response.status(200).entity(uniqueId).build();
        } else if (isValid(cert)) {
            this.registeredClustersFacade.create(new RegisteredClusters(uniqueId, search_endpoint, email, cert, gvod_endpoint, 0, dateFormat.format(date), dateFormat.format(date)));
        } else {
            Response.status(403).entity(null).build();
        }

        return Response.status(200).entity(uniqueId).build();
    }

    @GET
    @Path("/ping/{cluster_id}/{search_endpoint}/{email}/{cert}/{gvod_endpoint}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response Ping(@PathParam("cluster_id") String cluster_id, @PathParam("search_endpoint") String search_endpoint, @PathParam("email") String email, @PathParam("cert") String cert, @PathParam("gvod_endpoint") String gvod_endpoint) {

        List<RegisteredClusters> clusters;

        RegisteredClusters registeredCluster = registeredClustersFacade.find(cluster_id);
        if (registeredCluster != null) {
            registeredCluster.setHeartbeatsMissed(0);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            registeredCluster.setDateLastPing(dateFormat.format(date));
            registeredClustersFacade.edit(registeredCluster);
            clusters = registeredClustersFacade.findAll();
            GenericEntity<List<RegisteredClusters>> to_return = new GenericEntity<List<RegisteredClusters>>(clusters) {
            };

            return Response.status(200).entity(to_return).build();
        }else{
            return Response.status(403).entity(null).build();
        }

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    private boolean isValid(String cert) {
        return true;
    }

}
