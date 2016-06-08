/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.rest;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

        search_endpoint = search_endpoint.replaceAll("'", "/");
        gvod_endpoint = gvod_endpoint.replaceAll("'", "/");

        if (!ClusterRegistered(email)) {

            if (isValid(cert)) {

                registerCluster(search_endpoint, email, cert, gvod_endpoint);
                List<RegisteredClusters> clusters = getAllRegisteredClusters();
                GenericEntity<List<RegisteredClusters>> to_return = new GenericEntity<List<RegisteredClusters>>(clusters) {
                };

                return Response.status(200).entity(to_return).build();
            } else {
                List<RegisteredClusters> clusters = new ArrayList<>();
                GenericEntity<List<RegisteredClusters>> to_return = new GenericEntity<List<RegisteredClusters>>(clusters) {
                };
                return Response.status(403).entity(to_return).build();
            }

        } else {
            List<RegisteredClusters> clusters = new ArrayList<>();
            GenericEntity<List<RegisteredClusters>> to_return = new GenericEntity<List<RegisteredClusters>>(clusters) {
            };
            return Response.status(403).entity(to_return).build();
        }

    }

    @GET
    @Path("/ping/{cluster_id}/{search_endpoint}/{email}/{cert}/{gvod_endpoint}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response Ping(@PathParam("cluster_id") String cluster_id, @PathParam("search_endpoint") String search_endpoint, @PathParam("email") String email, @PathParam("cert") String cert, @PathParam("gvod_endpoint") String gvod_endpoint) {

       
        if(!ClusterRegistered(email)){
            List<RegisteredClusters> clusters = new ArrayList<>();
                GenericEntity<List<RegisteredClusters>> to_return = new GenericEntity<List<RegisteredClusters>>(clusters) {
                };
                return Response.status(403).entity(to_return).build();
        }else{
            List<RegisteredClusters> clusters = getAllRegisteredClusters();
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

    private boolean isValid(String cert) {
        return true;
    }

    private boolean ClusterRegistered(String email) {

        boolean exists;
        RegisteredClusters rq = this.registeredClustersFacade.findByEmail(email);
        if (rq != null) {
            return true;
        } else {
            return false;
        }

    }

    private void registerCluster(String search_endpoint, String email, String cert, String gvod_endpoint) {

        String uniqueId = UUID.randomUUID().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        this.registeredClustersFacade.create(new RegisteredClusters(uniqueId, search_endpoint, email, cert, gvod_endpoint, 0, dateFormat.format(date), dateFormat.format(date)));

    }

    private List<RegisteredClusters> getAllRegisteredClusters() {

        return this.registeredClustersFacade.findAll();
    }

}
