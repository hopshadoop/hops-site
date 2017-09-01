package io.hops.site.rest;

import io.hops.site.controller.ClusterController;
import io.hops.site.controller.HopsSiteSettings;
import io.hops.site.dto.ClusterAddressDTO;
import io.hops.site.dto.ClusterServiceDTO;
import io.hops.site.rest.annotation.NoCache;
import io.hops.site.util.CertificateHelper;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
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

  private final static Logger LOG = Logger.getLogger(ClusterService.class.getName());
  @EJB
  private ClusterController clusterController;
  @EJB
  private HopsSiteSettings hsettings;

  //*****************************************************VERIFIED*******************************************************
  @GET
  @NoCache
  @Path("dela/version")
  public Response getVersion() {
    LOG.log(Level.FINE, "dela version request");
    String version = hsettings.getDELA_VERSION();
    return Response.ok(version).build();
  }

  @PUT
  @NoCache
  @Path("register")
  public Response register(@Context HttpServletRequest req, ClusterServiceDTO.Register msg) throws
    CertificateEncodingException {
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:cluster register");
    X509Certificate[] certs = (X509Certificate[]) req.getAttribute("javax.servlet.request.X509Certificate");
    X509Certificate clientCert = certs[0];
    //TODO:check if cert email == registerJson.getEmail()
    String orgName = CertificateHelper.getCertificatePart(clientCert, "CN");
    String publicCId = clusterController.registerCluster(clientCert.getEncoded(), orgName, msg);
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:cluster register done:{0}", publicCId);
    return Response.ok(publicCId).build();
  }

  @PUT
  @NoCache
  @Path("heavyPing/{publicCId}")
  public Response heavyPing(@PathParam("publicCId") String publicCId, ClusterServiceDTO.HeavyPing ping) {
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:cluster heavyPing {0}", publicCId);
    ClusterController.Action action = clusterController.heavyPing(publicCId, ping.getUpldDSIds(),
      ping.getDwnlDSIds());
    switch (action) {
      case HEAVY_PING:
        LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:cluster heavyPing done {0}", publicCId);
        return Response.ok("ok").build();
      default:
        throw new IllegalStateException(action.toString());
    }
  }

  @PUT
  @NoCache
  @Path("ping/{publicCId}")
  public Response ping(@PathParam("publicCId") String publicCId) {
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:cluster ping {0}", publicCId);
    ClusterController.Action action = clusterController.ping(publicCId);
    switch (action) {
      case PING:
        LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:cluster ping done {0}", publicCId);
        return Response.ok("ok").build();
      default:
        throw new IllegalStateException(action.toString());
    }
  }

  //********************************************************************************************************************
  @GET
  @NoCache
  public Response getRegisterd(@Context SecurityContext sc) {
    GenericEntity result = new GenericEntity<List<ClusterAddressDTO>>(clusterController.getAll()) {
    };
    return Response.ok(result).build();
  }

  @GET
  @NoCache
  @Path("role")
  public Response getClusterRole(@Context SecurityContext sc) {
    LOG.log(Level.INFO, "Cluster: {0}", sc.getUserPrincipal().getName());
    String role;
    if (sc.isUserInRole("clusters")) {
      role = "clusters";
    } else if (sc.isUserInRole("admin")) {
      role = "admin";
    } else {
      role = "none";
    }
    LOG.log(Level.INFO, "Cluster Role: {0}", role);
    return Response.ok(role).build();
  }

  @DELETE
  @Path("{clusterId}")
  @RolesAllowed({"admin"})
  public Response removeRegisterdCluster(@PathParam("clusterId") String clusterId) {
    clusterController.removeClusterByPublicId(clusterId);
    LOG.log(Level.INFO, "Registered cluster with id: {0} removed.", clusterId);
    return Response.status(Response.Status.OK).build();
  }

}
