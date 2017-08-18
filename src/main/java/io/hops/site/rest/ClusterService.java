package io.hops.site.rest;

import io.hops.site.controller.ClusterController;
import io.hops.site.dto.HeavyPingDTO;
import io.hops.site.dto.IdentificationJSON;
import io.hops.site.dto.PingResponse;
import io.hops.site.dto.RegisterJSON;
import io.hops.site.dto.RegisteredClusterJSON;
import io.hops.site.dto.RegisteredJSON;
import io.hops.site.rest.annotation.NoCache;
import io.swagger.annotations.Api;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    } else if (sc.isUserInRole("admin")) {
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
  public Response register(RegisterJSON registerJson, @Context HttpServletRequest req) throws
    CertificateEncodingException {
    X509Certificate[] certs = (X509Certificate[]) req.getAttribute("javax.servlet.request.X509Certificate");
    X509Certificate clientCert = certs[0];
    registerJson.setDerCert(clientCert.getEncoded());
    //TODO:check if cert email == registerJson.getEmail()
    String registeredId = clusterController.registerCluster(registerJson);
    LOGGER.log(Level.INFO, "Registering new cluster.");
    return Response.status(Response.Status.OK).entity(new RegisteredJSON(registeredId)).build();
  }

  @PUT
  @NoCache
  @Path("heavyPing")
  public Response heavyPing(HeavyPingDTO ping) {
    ClusterController.Action action = clusterController.heavyPing(ping.getClusterId(), ping.getUpldDSIds(), ping.
      getDwnlDSIds());
    switch (action) {
      case HEAVY_PING:
        LOGGER.log(Level.INFO, "Registering heavy ping from cluster id: {0}.", ping.getClusterId());
        return Response.ok().build();
      default:
        throw new IllegalStateException("hops-site logic exception");
    }
  }

  @PUT
  @NoCache
  @Path("ping")
  public Response ping(IdentificationJSON ping) {
    ClusterController.Action action = clusterController.ping(ping.getClusterId());
    switch (action) {
      case HEAVY_PING:
        throw new IllegalStateException(action.toString());
      case PING:
        LOGGER.log(Level.INFO, "Registering ping from cluster id: {0}.", ping.getClusterId());
        return Response.ok().build();
      default:
        throw new IllegalStateException("hops-site logic exception");
    }
  }

  @DELETE
  @Path("{clusterId}")
  @RolesAllowed({"admin"})
  public Response removeRegisterdCluster(@PathParam("clusterId") String clusterId) {
    clusterController.removeClusterByPublicId(clusterId);
    LOGGER.log(Level.INFO, "Registered cluster with id: {0} removed.", clusterId);
    return Response.status(Response.Status.OK).build();
  }
}
