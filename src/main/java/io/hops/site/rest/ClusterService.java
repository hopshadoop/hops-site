package io.hops.site.rest;

import io.hops.site.controller.ClusterController;
import io.hops.site.controller.HopsSiteSettings;
import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dto.ClusterAddressDTO;
import io.hops.site.dto.ClusterServiceDTO;
import io.hops.site.rest.annotation.NoCache;
import io.hops.site.rest.exception.ThirdPartyException;
import io.hops.site.util.SecurityHelper;
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
  public Response register(@Context HttpServletRequest req, ClusterServiceDTO.Register msg)
    throws CertificateEncodingException, ThirdPartyException {
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:cluster register");
    X509Certificate[] certs = (X509Certificate[]) req.getAttribute("javax.servlet.request.X509Certificate");
    String publicCId = clusterController.registerCluster(certs[0], msg);
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:cluster register done:{0}", publicCId);
    return Response.ok(publicCId).build();
  }

  @PUT
  @NoCache
  @Path("heavyPing/{publicCId}")
  public Response heavyPing(@PathParam("publicCId") String publicCId, ClusterServiceDTO.HeavyPing msg) 
    throws ThirdPartyException {
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:cluster heavyPing {0} <{1}, {2}>",
      new Object[]{publicCId, msg.getUpldDSIds().size(), msg.getDwnlDSIds().size()});
    ClusterController.Action action = clusterController.heavyPing(publicCId, msg.getUpldDSIds(),
      msg.getDwnlDSIds());
    switch (action) {
      case HEAVY_PING:
        LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:cluster heavyPing done {0}", publicCId);
        return Response.ok("ok").build();
      default:
        throw new ThirdPartyException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
          ThirdPartyException.Error.CLUSTER_NOT_REGISTERED, ThirdPartyException.Source.REMOTE_DELA, "protocol");
    }
  }

  @PUT
  @NoCache
  @Path("ping/{publicCId}")
  public Response ping(@PathParam("publicCId") String publicCId, ClusterServiceDTO.Ping msg) 
    throws ThirdPartyException {
    LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:cluster ping {0}", publicCId);
    ClusterController.Action action = clusterController.ping(publicCId, msg);
    switch (action) {
      case PING:
        LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:cluster ping done {0}", publicCId);
        return Response.ok("ok").build();
      default:
        throw new ThirdPartyException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
          ThirdPartyException.Error.HEAVY_PING, ThirdPartyException.Source.REMOTE_DELA, "protocol");
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
  @Path("all")
  @NoCache
  @RolesAllowed({"admin"})
  public Response getAllRegisterdClusters(@Context SecurityContext sc) {
    GenericEntity result = new GenericEntity<List<RegisteredCluster>>(clusterController.getAllClustaers()) {
    };
    return Response.ok(result).build();
  }

  @GET
  @NoCache
  @Path("role")
  public Response getClusterRole(@Context SecurityContext sc) {
    LOG.log(Level.INFO, "Cluster: {0}", sc.getUserPrincipal().getName());
    String role = SecurityHelper.getClusterRole(sc);
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
