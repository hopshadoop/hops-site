package io.hops.site.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import io.hops.site.dto.FailJSON;
import io.hops.site.dto.IdentificationJSON;
import io.hops.site.dto.PingedJSON;
import io.hops.site.dto.RegisteredClusterJSON;
import io.hops.site.dto.AddressJSON;
import io.hops.site.dto.RegisterJSON;
import io.hops.site.dto.RegisteredJSON;
import io.hops.site.controller.HelperFunctions;
import io.swagger.annotations.Api;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Path("cluster")
@Stateless
@Api(value = "/cluster", description = "Cluster Register And Ping service")
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ClusterService {

  private final static Logger LOGGER = Logger.getLogger(ClusterService.class.getName());
  @EJB
  private HelperFunctions helperFunctions;
  @EJB
  private RegisteredClusterFacade registeredClustersFacade;

  private final ObjectMapper mapper = new ObjectMapper();

  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response Register(RegisterJSON registerJson) {
    if (!helperFunctions.ClusterRegisteredWithEmail(registerJson.getEmail())) {
      if (helperFunctions.isValid(registerJson.getCert())) {
        String registeredId = helperFunctions.registerCluster(registerJson.getSearchEndpoint(), registerJson.getEmail(),
                registerJson.getCert(), registerJson.getGvodEndpoint());
        if (registeredId != null) {
          return Response.status(Response.Status.OK).entity(new RegisteredJSON(registeredId)).build();
        } else {
          LOGGER.log(Level.INFO, "Invalid gvodEndpoint.");
          return Response.status(Response.Status.FORBIDDEN).entity(new FailJSON("Invalid gvodEndpoint.")).build();
        }
      } else {
        LOGGER.log(Level.INFO, "Invalid cert.");
        return Response.status(Response.Status.FORBIDDEN).entity(new FailJSON("Invalid cert.")).build();
      }
    } else {
      LOGGER.log(Level.INFO, "Already registered.");
      return Response.status(Response.Status.FORBIDDEN).entity(new FailJSON("Already registered.")).build();
    }
  }

  @PUT
  @Path("ping")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response Ping(IdentificationJSON identification) {
    if (!helperFunctions.ClusterRegisteredWithId(identification.getClusterId())) {
      return Response.status(Response.Status.FORBIDDEN).entity(new FailJSON("invalid id")).build();
    } else {
      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      Date date = new Date();
      RegisteredCluster registeredCluster = this.registeredClustersFacade.find(identification.getClusterId());
      registeredCluster.setHeartbeatsMissed(0);
      registeredCluster.setDateLastPing(dateFormat.format(date));
      registeredClustersFacade.edit(registeredCluster);
      List<RegisteredCluster> registeredClusters = helperFunctions.getAllRegisteredClusters();
      List<RegisteredClusterJSON> to_ret = new ArrayList<>();
      for (RegisteredCluster r : registeredClusters) {
        try {
          to_ret.add(new RegisteredClusterJSON(r.getClusterId(), mapper.
                  readValue(r.getGvodEndpoint(), AddressJSON.class), r.getHeartbeatsMissed(), r.getDateRegistered(), r.
                  getDateLastPing(), r.getSearchEndpoint()));
        } catch (IOException ex) {
          LOGGER.log(Level.SEVERE, null, ex);
          return Response.status(Response.Status.FORBIDDEN).entity(new FailJSON("parsing of gvodEndpoint failed")).
                  build();
        }
      }
      return Response.status(Response.Status.OK).entity(new PingedJSON(to_ret)).build();
    }
  }
}
