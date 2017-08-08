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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import io.hops.site.dto.AddressJSON;
import io.hops.site.dto.IdentificationJSON;
import io.hops.site.dto.RegisterJSON;
import io.hops.site.dto.RegisteredClusterJSON;
import io.hops.site.rest.ClusterService;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ClusterController {

  private final static Logger LOGGER = Logger.getLogger(ClusterService.class.getName());
  @EJB
  private HelperFunctions helperFunctions;
  @EJB
  private RegisteredClusterFacade registeredClustersFacade;

  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * Register a new cluster
   *
   * @param registerJson
   * @return
   */
  public String registerCluster(RegisterJSON registerJson) {
    if (helperFunctions.ClusterRegisteredWithEmail(registerJson.getEmail())) {
      LOGGER.log(Level.INFO, "Already registered.");
      throw new IllegalArgumentException("Already registered.");
    }
    if (!helperFunctions.isValid(registerJson.getCert())) {
      LOGGER.log(Level.INFO, "Invalid cert.");
      throw new AccessControlException("Invalid cert.");
    }
    String registeredId = helperFunctions.registerCluster(registerJson.getSearchEndpoint(), registerJson.getEmail().
            toLowerCase(), registerJson.getDerCert(), registerJson.getGvodEndpoint());
    if (registeredId == null) {
      LOGGER.log(Level.INFO, "Invalid gvodEndpoint.");
      throw new IllegalArgumentException("Invalid gvodEndpoint.");
    }
    return registeredId;
  }

  /**
   * Register heartbeat from cluster
   *
   * @param identification
   * @return list of RegisteredClusterJSON
   */
  public List<RegisteredClusterJSON> registerPing(IdentificationJSON identification) {
    if (!helperFunctions.ClusterRegisteredWithId(identification.getClusterId())) {
      throw new IllegalArgumentException("Invalid id.");
    }
    RegisteredCluster registeredCluster = this.registeredClustersFacade.find(identification.getClusterId());
    registeredCluster.setHeartbeatsMissed(0);
    registeredCluster.setDateLastPing(new Date());
    registeredClustersFacade.edit(registeredCluster);
    return getAll();
  }

  /**
   * Gets all registered clusters
   *
   * @return list of RegisteredClusterJSON
   */
  public List<RegisteredClusterJSON> getAll() {
    List<RegisteredCluster> registeredClusters = helperFunctions.getAllRegisteredClusters();
    List<RegisteredClusterJSON> to_ret = new ArrayList<>();
    for (RegisteredCluster r : registeredClusters) {
      try {
        to_ret.add(new RegisteredClusterJSON(r.getClusterId(), mapper.
                readValue(r.getGvodEndpoint(), AddressJSON.class), r.getHeartbeatsMissed(), r.getDateRegistered(), r.
                getDateLastPing(), r.getSearchEndpoint()));
      } catch (IOException ex) {
        LOGGER.log(Level.SEVERE, null, ex);
        throw new IllegalArgumentException("Parsing of gvodEndpoint failed.");
      }
    }
    return to_ret;
  }

  /**
   *
   * @param clusterEmail
   * @return
   */
  public RegisteredCluster getClusterByEmail(String clusterEmail) {
    if (clusterEmail == null || clusterEmail.isEmpty()) {
      throw new IllegalArgumentException("Cluster email not assigned.");
    }
    return registeredClustersFacade.findByEmail(clusterEmail.toLowerCase());
  }

  /**
   *
   * @param clusterId
   * @return
   */
  public RegisteredCluster getClusterById(String clusterId) {
    if (clusterId == null || clusterId.isEmpty()) {
      throw new IllegalArgumentException("Cluster id not assigned.");
    }
    return registeredClustersFacade.find(clusterId);
  }

  /**
   *
   * @param clusterId
   */
  public void removeCluster(String clusterId) {
    if (clusterId == null || clusterId.isEmpty()) {
      throw new IllegalArgumentException("Cluster id not assigned.");
    }
    RegisteredCluster cluster = registeredClustersFacade.find(clusterId);
    registeredClustersFacade.remove(cluster);
  }
}
