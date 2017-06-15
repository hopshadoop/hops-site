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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
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
  public void addNewUser(Users user) {
    if (user == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (user.getEmail() == null || user.getEmail().isEmpty()) {
      throw new IllegalArgumentException("User email not assigned.");
    }
    if (user.getClusterId() == null || user.getClusterId().getClusterId() == null || user.getClusterId().getClusterId().
            isEmpty()) {
      throw new IllegalArgumentException("Cluster is not assigned.");
    }
    if ((user.getFirstname() == null || user.getFirstname().isEmpty()) && (user.getLastname() == null || user.
            getLastname().isEmpty())) {
      throw new IllegalArgumentException("User name is not assigned.");
    }
    RegisteredCluster registeredCluster = registeredClusterFacade.find(user.getClusterId().getClusterId());
    if (registeredCluster == null) {
      throw new IllegalArgumentException("Cluster not found.");
    }

    user.setClusterId(registeredCluster);
    userFacade.create(user);
    LOGGER.log(Level.INFO, "Adding new user: {0}.", user.getFirstname());
  }

  /**
   * Update user first and/or last name
   *
   * @param user
   */
  public void updateUser(Users user) {
    if (user == null) {
      throw new IllegalArgumentException("One or more arguments not assigned.");
    }
    if (user.getEmail() == null || user.getEmail().isEmpty()) {
      throw new IllegalArgumentException("User email not assigned.");
    }
    if (user.getClusterId() == null || user.getClusterId().getClusterId() == null || user.getClusterId().getClusterId().
            isEmpty()) {
      throw new IllegalArgumentException("Cluster is not assigned.");
    }
    if ((user.getFirstname() == null || user.getFirstname().isEmpty()) && (user.getLastname() == null || user.
            getLastname().isEmpty())) {
      throw new IllegalArgumentException("User name is not assigned.");
    }
    RegisteredCluster registeredCluster = registeredClusterFacade.find(user.getClusterId().getClusterId());
    if (registeredCluster == null) {
      throw new IllegalArgumentException("Cluster not found.");
    }
    Users managedUser = userFacade.findByEmail(user.getEmail());
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
   * @param userEmail 
   */
  public void removeUser(String userEmail) {
    if (userEmail == null) {
      throw new IllegalArgumentException("User email not assigned.");
    }
    Users managedUser = userFacade.findByEmail(userEmail);
    userFacade.remove(managedUser);
    LOGGER.log(Level.INFO, "Remove user: {0}.", userEmail);
  }

}
