package io.hops.site.rest;

import io.hops.site.controller.ClusterController;
import io.hops.site.controller.HopsSiteSettings;
import io.hops.site.rest.annotation.NoCache;
import io.swagger.annotations.Api;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("public/cluster")
@Stateless
@Api(value = "public/cluster",
  description = "Cluster Register And Ping service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@TransactionAttribute(TransactionAttributeType.NEVER)
public class PublicClusterService {

  private final static Logger LOG = Logger.getLogger(PublicClusterService.class.getName());
  @EJB
  private ClusterController clusterController;
  @EJB
  private HopsSiteSettings hsettings;
  
  @GET
  @NoCache
  @Path("dela/version")
  public Response getVersion() {
    LOG.log(Level.FINE, "dela version request");
    String version = hsettings.getDELA_VERSION();
    return Response.ok(version).build();
  }
}
