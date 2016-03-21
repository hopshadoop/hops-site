/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.bootstrapserver.rest;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import se.kth.bootstrapserver.beans.NoCacheResponse;
import se.kth.bootstrapserver.beans.RegisteredClustersFacade;
import se.kth.bootstrapserver.entities.RegisteredClusters;

/**
 *
 * @author jsvhqr
 */
@Path("/available_clusters")
@Produces(MediaType.APPLICATION_JSON)
public class ClustersPingService {

    @EJB
    RegisteredClustersFacade registerdClustersFacade;

    @EJB
    NoCacheResponse noCacheResponse;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response ping(@PathParam("name") String name, @PathParam("restendpoint") String rep, @PathParam("email") String mail, @PathParam("cert") String cert, @PathParam("udp_support") Boolean udps) {

        List<RegisteredClusters> clusters;

        if (registerdClustersFacade.clusterExists(name)) {

            clusters = registerdClustersFacade.findAll();

        } else {
            registerdClustersFacade.registerCluster(name, rep, mail, cert, udps);
            clusters = registerdClustersFacade.findAll();

        }

        return noCacheResponse.getNoCacheResponseBuilder(Response.Status.OK).entity(
                clusters).build();
    }

}
