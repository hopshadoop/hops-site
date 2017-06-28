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
  private RegisteredClusterFacade registeredClusterFacade;

  /**
   * Creates new hops-site user
   *
   * @param user
   */
  public void addNewUser(UserDTO user) {
    if (user == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
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
    RegisteredCluster registeredCluster = registeredClusterFacade.find(user.getClusterId());
    if (registeredCluster == null) {
      throw new IllegalArgumentException("Cluster not found.");
    }

    Users newUser = new Users(user.getFirstname(), user.getLastname(), user.getEmail().toLowerCase(), registeredCluster);
    userFacade.create(newUser);
    LOGGER.log(Level.INFO, "Adding new user: {0}.", user.getFirstname());
  }

  /**
   * Update user first and/or last name
   *
   * @param user
   */
  public void updateUser(UserDTO user) {
    if (user == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
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
    RegisteredCluster registeredCluster = registeredClusterFacade.find(user.getClusterId());
    if (registeredCluster == null) {
      throw new IllegalArgumentException("Cluster not found.");
    }
    Users managedUser = userFacade.findByEmailAndCluster(user.getEmail(), user.getClusterId());
    if (managedUser == null) {
      throw new IllegalArgumentException("User not found.");
    }

    managedUser.setFirstname(user.getFirstname());
    managedUser.setLastname(user.getLastname());
    userFacade.edit(managedUser);
    LOGGER.log(Level.INFO, "Updating user: {0}.", user.getFirstname());
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
   * @param clusterId
   */
  public void removeUser(String userEmail, String clusterId) {
    if (userEmail == null || clusterId == null) {
      throw new IllegalArgumentException("User email or cluster id not assigned.");
    }
    Users managedUser = userFacade.findByEmailAndCluster(userEmail, clusterId);
    userFacade.remove(managedUser);
    LOGGER.log(Level.INFO, "Remove user: {0}.", userEmail);
  }

  /**
   * 
   * @param email
   * @param clusterId
   * @return 
   */
  public Users findUserByEmailAndClusterId(String email, String clusterId) {
    if (email == null || clusterId == null) {
      throw new IllegalArgumentException("User email or cluster id not assigned.");
    }
    return userFacade.findByEmailAndCluster(email.toLowerCase(), clusterId);
  }

}
