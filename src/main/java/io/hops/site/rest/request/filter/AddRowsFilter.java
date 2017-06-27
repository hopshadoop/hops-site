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
package io.hops.site.rest.request.filter;

import io.hops.site.controller.DatasetController;
import io.hops.site.controller.UsersController;
import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.entity.Users;
import io.hops.site.dto.DatasetDTO;
import io.hops.site.dto.UserDTO;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.server.ContainerRequest;

@Provider
@Priority(Priorities.USER)
public class AddRowsFilter implements ContainerRequestFilter {

  private final static Logger LOGGER = Logger.getLogger(AddRowsFilter.class.getName());

  @EJB
  private DatasetController datasetController;
  @EJB
  private UsersController usersController;
  
  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    ContainerRequest cr = (ContainerRequest) requestContext;
    if (cr.bufferEntity()) {
      GenericReqUserDatasetDTO reqDTO = null;
      try {
        reqDTO = cr.readEntity(GenericReqUserDatasetDTO.class);
      } catch (Exception nsme) {
        //nothing to do. It is not GenericRequestDTO, thus can not be checked.
      }
      if (reqDTO == null) {
        LOGGER.log(Level.INFO, "Not a generic request.");
        return;
      }

      if (reqDTO.getDataset() != null) {
        addDatasetIfNotExist(reqDTO.getDataset());
      }

      if (reqDTO.getUser() != null) {
        addUserIfNotExist(reqDTO.getUser());
      }
           
    }
  }

  private void addDatasetIfNotExist(DatasetDTO datasetDTO) {
    if (datasetDTO.getPublicId() == null) {
      return;
    }
    Dataset ds = datasetController.findDatasetByPublicId(datasetDTO.getPublicId());
    if (ds != null) {
      return;
    }
    datasetController.addDataset(datasetDTO);
    LOGGER.log(Level.INFO, "Add new dataset with public id: {0}", datasetDTO.getPublicId());
  }

  private void addUserIfNotExist(UserDTO userDTO) {
    if (userDTO.getEmail() == null) {
      return;
    }
    Users user = usersController.findUserByEmail(userDTO.getEmail());
    if (user != null) {
      return;
    }
    usersController.addNewUser(userDTO);
    LOGGER.log(Level.INFO, "Add new user for cluster: {0}", userDTO.getClusterId());
  }

}
