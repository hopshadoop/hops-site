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
package io.hops.site.controller;

import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dao.entity.Users;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import io.hops.site.dao.facade.UsersFacade;
import io.hops.site.dto.UserDTO;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class UsersController {

  private final static Logger LOGGER = Logger.getLogger(UsersController.class.getName());
  @EJB
  private UsersFacade userFacade;
  @EJB
  private RegisteredClusterFacade clusterFacade;

  /**
   * Creates new hops-site user
   *
   * @param user
   */
  public void addNewUser(UserDTO user) {
    userDTOSanityCheck(user);
    Optional<RegisteredCluster> cluster = clusterFacade.findByPublicId(user.getClusterId());
    if (!cluster.isPresent()) {
      throw new IllegalArgumentException("Cluster not found.");
    }
    Users newUser = new Users(user.getFirstname(), user.getLastname(), user.getEmail().toLowerCase(), cluster.get());
    userFacade.create(newUser);
    LOGGER.log(Level.INFO, "Adding new user: {0}.", user.getFirstname());
  }

  /**
   * Update user first and/or last name
   *
   * @param updateUser
   */
  public void updateUser(UserDTO updateUser) {
    userDTOSanityCheck(updateUser);
    Optional<Users> userAux = userFacade.findByEmailAndPublicClusterId(updateUser.getEmail(), updateUser.getClusterId());
    if (!userAux.isPresent()) {
      throw new IllegalArgumentException("User not found.");
    }
    Users user = userAux.get();
    user.setFirstname(updateUser.getFirstname());
    user.setLastname(updateUser.getLastname());
    userFacade.edit(user);
    LOGGER.log(Level.INFO, "Updating user: {0}.", user.getId());
  }

  private void userDTOSanityCheck(UserDTO user) {
    if (user == null) {
      throw new IllegalArgumentException("User is not assigned.");
    }
    if (user.getEmail() == null || user.getEmail().isEmpty()) {
      throw new IllegalArgumentException("User email not assigned.");
    }
    if (user.getClusterId() == null || user.getClusterId().isEmpty()) {
      throw new IllegalArgumentException("Cluster is not assigned.");
    }
    if ((user.getFirstname() == null || user.getFirstname().isEmpty()) && (user.getLastname() == null || user.
      getLastname().isEmpty())) {
      throw new IllegalArgumentException("User name is not assigned.");
    }
  }

  /**
   * Removes user with the given id
   *
   * @param userId
   */
  public void removeUser(Integer userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User id not assigned.");
    }
    Users managedUser = userFacade.find(userId);
    userFacade.remove(managedUser);
    LOGGER.log(Level.INFO, "Remove user: {0}.", userId);
  }

  /**
   * Remove user by email
   *
   * @param userEmail
   * @param clusterPublicId
   */
  public void removeUser(String userEmail, String clusterPublicId) {
    if (userEmail == null || clusterPublicId == null) {
      throw new IllegalArgumentException("User email or cluster id not assigned.");
    }
    Optional<Users> user = userFacade.findByEmailAndPublicClusterId(userEmail, clusterPublicId);
    if (user.isPresent()) {
      userFacade.remove(user.get());
    }
    LOGGER.log(Level.INFO, "Remove user: {0}.", userEmail);
  }

  /**
   *
   * @param email
   * @param clusterPublicId
   * @return
   */
  public Optional<Users> findUserByEmailAndClusterId(String email, String clusterPublicId) {
    if (email == null || clusterPublicId == null) {
      throw new IllegalArgumentException("User email or cluster id not assigned.");
    }
    Optional<Users> user = userFacade.findByEmailAndPublicClusterId(email.toLowerCase(), clusterPublicId);
    return user;
  }

}
