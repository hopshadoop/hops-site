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

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class RegisterAndPingService {

  @EJB
  HelperFunctions helperFunctions;
  @EJB
  RegisteredClusterFacade registeredClustersFacade;

  private final ObjectMapper mapper = new ObjectMapper();

  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response Register(RegisterJSON registerJson) {

    if (!helperFunctions.ClusterRegisteredWithEmail(registerJson.getEmail())) {

      if (helperFunctions.isValid(registerJson.getCert())) {

        String registeredId = helperFunctions.registerCluster(registerJson.getSearchEndpoint(), registerJson.getEmail(),
                registerJson.getCert(), registerJson.getGVodEndpoint());

        if (registeredId != null) {

          return Response.status(200).entity(new RegisteredJSON(registeredId)).build();

        } else {

          return Response.status(403).entity(new FailJSON("invalid gvodEndpoint")).build();

        }

      } else {

        return Response.status(403).entity(new FailJSON("invalid cert")).build();
      }

    } else {

      return Response.status(403).entity(new FailJSON("already registered")).build();
    }

  }

  @PUT
  @Path("ping")
  @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  @Produces(MediaType.APPLICATION_JSON)
  public Response Ping(IdentificationJSON identification) {

    if (!helperFunctions.ClusterRegisteredWithId(identification.getClusterId())) {
      return Response.status(403).entity(new FailJSON("invalid id")).build();
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
          Logger.getLogger(RegisterAndPingService.class.getName()).log(Level.SEVERE, null, ex);
          return Response.status(403).entity(new FailJSON("parsing of gvodEndpoint failed")).build();
        }
      }
      return Response.status(200).entity(new PingedJSON(to_ret)).build();
    }

  }
}