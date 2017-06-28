package io.hops.site.rest;

import io.hops.site.controller.ClusterController;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.hops.site.dto.IdentificationJSON;
import io.hops.site.dto.PingResponse;
import io.hops.site.dto.RegisteredClusterJSON;
import io.hops.site.dto.RegisterJSON;
import io.hops.site.dto.RegisteredJSON;
import io.hops.site.rest.annotation.NoCache;
import io.swagger.annotations.Api;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@Path("cluster")
@Stateless
@Api(value = "/cluster",
        description = "Cluster Register And Ping service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ClusterService {

  private final static Logger LOGGER = Logger.getLogger(ClusterService.class.getName());
  @EJB
  private ClusterController clusterController;

  @GET
  @NoCache
  public Response getRegisterd(@Context SecurityContext sc) {
    List<RegisteredClusterJSON> to_ret = clusterController.getAll();
    return Response.status(Response.Status.OK).entity(new PingResponse(to_ret)).build();
  }
  
  @GET
  @NoCache
  @Path("role")
  public Response getClusterRole(@Context SecurityContext sc) {
    LOGGER.log(Level.INFO, "Cluster: {0}", sc.getUserPrincipal().getName());
    String role;
    if (sc.isUserInRole("clusters")) {
      role = "clusters";
    } else if (sc.isUserInRole("admin")){
      role = "admin";
    } else {
      role = "none";
    }
    LOGGER.log(Level.INFO, "Cluster Role: {0}", role);
    return Response.ok(role).build();
  }
  
  @POST
  @NoCache
  @Path("register")
  public Response register(RegisterJSON registerJson) {
    String registeredId = clusterController.registerCluster(registerJson);
    LOGGER.log(Level.INFO, "Registering new cluster.");
    return Response.status(Response.Status.OK).entity(new RegisteredJSON(registeredId)).build();
  }

  @PUT
  @NoCache
  @Path("ping")
  public Response ping(IdentificationJSON identification) {
    List<RegisteredClusterJSON> to_ret = clusterController.registerPing(identification);
    LOGGER.log(Level.INFO, "Registering ping from cluster id: {0}.", identification.getClusterId());
    return Response.status(Response.Status.OK).entity(new PingResponse(to_ret)).build();
  }
  
  @DELETE
  @Path("{clusterId}")
  @RolesAllowed({"admin"})
  public Response removeRegisterdCluster(@PathParam("clusterId") String clusterId) {
    clusterController.removeCluster(clusterId);
    LOGGER.log(Level.INFO, "Registered cluster with id: {0} removed.", clusterId);
    return Response.status(Response.Status.OK).build();
  }
}
