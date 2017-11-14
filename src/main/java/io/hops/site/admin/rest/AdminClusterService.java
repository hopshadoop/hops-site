package io.hops.site.admin.rest;

import io.hops.site.controller.ClusterController;
import io.hops.site.dao.admin.entity.AdminRegisteredCluster;
import io.hops.site.rest.annotation.NoCache;
import io.hops.site.util.SecurityHelper;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/admin/cluster")
@Stateless
@RolesAllowed({"admin"})
@Api(value = "admin/cluster",
    description = "Admin Cluster service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@TransactionAttribute(TransactionAttributeType.NEVER)
public class AdminClusterService {

  private final static Logger LOG = Logger.getLogger(AdminClusterService.class.getName());
  @EJB
  private ClusterController clusterController;

  @GET
  @Path("all")
  @NoCache
  public Response getAllRegisterdClusters(@Context SecurityContext sc) {
    GenericEntity result = new GenericEntity<List<AdminRegisteredCluster>>(clusterController.getAllClustaers()) {
    };
    return Response.ok(result).build();
  }

  @GET
  @Path("org/{orgName}/{orgUnit}")
  @NoCache
  public Response getRegisterdCluster(@PathParam("orgName") String orgName, @PathParam("orgUnit") String orgUnit,
      @Context SecurityContext sc) {
    String role = SecurityHelper.getClusterRole(sc);
    Optional<AdminRegisteredCluster> cluster = clusterController.getClusterByOrgName(orgName, orgUnit);
    if (cluster.isPresent()) {
      return Response.ok().entity(cluster.get()).build();
    }
    return Response.ok().entity(null).build();
  }

  @DELETE
  @Path("{clusterId}")
  public Response removeRegisterdCluster(@PathParam("clusterId") String clusterId, @Context SecurityContext sc) {
    clusterController.removeClusterByPublicId(clusterId);
    return Response.status(Response.Status.OK).build();
  }
}
