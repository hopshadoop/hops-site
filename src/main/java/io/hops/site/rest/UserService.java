/*
 * Copyright 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hops.site.rest;

import io.hops.site.rest.exception.AppException;
import io.hops.site.controller.UsersController;
import io.hops.site.dao.entity.Users;
import io.hops.site.dto.UserDTO;
import io.swagger.annotations.Api;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/user",
        description = "User service")
@TransactionAttribute(TransactionAttributeType.NEVER)
public class UserService {

  private final static Logger LOGGER = Logger.getLogger(UserService.class.getName());

  @EJB
  private UsersController usersController;
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/register/{publicCId}")
  public Response getUser(@PathParam("publicCId") String publicCId, UserDTO userDTO) {
    Optional<Users> user = usersController.findUserByEmailAndClusterId(userDTO.getEmail(), publicCId);
    if (!user.isPresent()) {
      usersController.addNewUser(publicCId, userDTO);
    } else {
      usersController.updateUser(publicCId, userDTO);
    }
    user = usersController.findUserByEmailAndClusterId(userDTO.getEmail(), publicCId);
    return Response.ok("" + user.get().getId()).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/get/{publicCId}")
  public Response getUser(@PathParam("publicCId") String publicCId, String userEmail) throws AppException {
    Optional<Users> user = usersController.findUserByEmailAndClusterId(userEmail, publicCId);
    if(!user.isPresent()) {
      return Response.ok(new UserDTO(user.get())).build();
    } else {
      throw new AppException(Response.Status.EXPECTATION_FAILED.getStatusCode(), "user not found");
    }
  }
  
  @DELETE
  @Path("/delete/{userId}")
  @RolesAllowed({"admin"})
  public Response deleteUser(@PathParam("userId") Integer userId) {
    usersController.removeUser(userId);
    LOGGER.log(Level.INFO, "Remove user with id: {0}", userId);
    return Response.ok("OK").build();
  }
}
