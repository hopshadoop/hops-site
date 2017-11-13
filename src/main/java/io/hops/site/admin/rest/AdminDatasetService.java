package io.hops.site.admin.rest;

import io.hops.site.dao.admin.entity.AdminDataset;
import io.hops.site.dao.admin.entity.AdminRegisteredCluster;
import io.hops.site.dao.admin.facade.AdminDatasetFacade;
import io.hops.site.rest.annotation.NoCache;
import io.hops.site.util.SecurityHelper;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/admin/dataset")
@Stateless
@RolesAllowed({"admin"})
@Api(value = "admin/dataset",
    description = "Admin Dataset service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@TransactionAttribute(TransactionAttributeType.NEVER)
public class AdminDatasetService {

  private final static Logger LOG = Logger.getLogger(AdminDatasetService.class.getName());

  @EJB
  private AdminDatasetFacade datasetFacade;

  @GET
  @NoCache
  public Response getAll(@Context SecurityContext sc) {
    String role = SecurityHelper.getClusterRole(sc);
    List<AdminDataset> datasets = datasetFacade.findAll();
    GenericEntity result = new GenericEntity<List<AdminDataset>>(datasets) {
    };
    return Response.ok(result).build();
  }
}
