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

import io.hops.site.controller.UsersController;
import io.hops.site.dao.entity.Users;
import io.hops.site.old_dto.UserDTO;
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
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
  public Response getUser(UserDTO userDTO) {
    Optional<Users> user = usersController.findUserByEmailAndClusterId(userDTO.getEmail(), userDTO.getClusterId());
    if (!user.isPresent()) {
      usersController.addNewUser(userDTO);
      user = usersController.findUserByEmailAndClusterId(userDTO.getEmail(), userDTO.getClusterId());
    }
    return Response.ok().entity(user).build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateUser(UserDTO user) {
    usersController.updateUser(user);
    LOGGER.log(Level.INFO, "Update user: {0}", user.getFirstname());
    return Response.ok("OK").build();
  }

  @DELETE
  @Path("{userId}")
  @RolesAllowed({"admin"})
  public Response deleteUser(@PathParam("userId") Integer userId) {
    usersController.removeUser(userId);
    LOGGER.log(Level.INFO, "Remove user with id: {0}", userId);
    return Response.ok("OK").build();
  }

  @DELETE
  @RolesAllowed({"admin"})
  public Response deleteUserByEmail(UserDTO userDTO) {
    usersController.removeUser(userDTO.getEmail(), userDTO.getClusterId());
    LOGGER.log(Level.INFO, "Remove user by email: {0}", userDTO.getEmail());
    return Response.ok("OK").build();
  }

}
