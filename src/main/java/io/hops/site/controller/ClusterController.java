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
import io.hops.site.common.Settings;
import io.hops.site.dao.entity.Heartbeat;
import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dao.facade.HeartbeatFacade;
import io.hops.site.dao.facade.LiveDatasetFacade;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import io.hops.site.dto.RegisterJSON;
import io.hops.site.dto.RegisteredClusterJSON;
import io.hops.site.rest.ClusterService;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
  private Settings settings;
  @EJB
  private RegisteredClusterFacade clusterFacade;
  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private HeartbeatFacade heartbeatFacade;
  @EJB
  private LiveDatasetFacade liveDatasetFacade;
  @EJB
  private HelperFunctions helperFunctions;


  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * Register a new cluster
   *
   * @param msg
   * @return
   */
  public String registerCluster(RegisterJSON msg) {
    Optional<RegisteredCluster> c = clusterFacade.findByEmail(msg.getEmail());
    if(c.isPresent()) {
      LOGGER.log(Level.INFO, "Already registered.");
      throw new IllegalArgumentException("Already registered.");
    }
    if (!helperFunctions.isValid(msg.getCert())) {
      LOGGER.log(Level.INFO, "Invalid cert.");
      throw new AccessControlException("Invalid cert.");
    }
    String clusterPublicId = settings.getClusterId();
    RegisteredCluster cluster = new RegisteredCluster(clusterPublicId, msg.getHttpEndpoint(), msg.getEmail().toLowerCase(), 
      msg.getDerCert(), msg.getDelaEndpoint());
    clusterFacade.create(cluster);
    return clusterPublicId;
  }
 

  public Action ping(String clusterPublicId) {
    Optional<RegisteredCluster> cluster = this.clusterFacade.findByPublicId(clusterPublicId);
    if (cluster.isPresent()) {
      Optional<Heartbeat> h = heartbeatFacade.findByClusterId(cluster.get().getId());
      if (h.isPresent()) {
        Heartbeat heartbeat = h.get();
        heartbeat.setLastPinged(settings.getDateNow());
        heartbeatFacade.edit(heartbeat);
        return Action.PING;
      } else {
        return Action.HEAVY_PING;
      }
    } else {
      throw new IllegalStateException("Authentication Filter - operations performed from unregistered cluster");
    }
  }

  public Action heavyPing(String clusterPublicId, List<String> upldDSPublicIds, List<String> dwnlDSPublicIds) {
    Optional<RegisteredCluster> cluster = this.clusterFacade.findByPublicId(clusterPublicId);
    if (cluster.isPresent()) {
      Heartbeat heartbeat = new Heartbeat(cluster.get().getId(), settings.getDateNow());
      heartbeatFacade.create(heartbeat);
      liveDatasetFacade.downloadDatasets(cluster.get().getId(), datasetFacade.findIds(upldDSPublicIds));
      liveDatasetFacade.uploadDatasets(cluster.get().getId(), datasetFacade.findIds(dwnlDSPublicIds));
      return Action.HEAVY_PING;
    } else {
      throw new IllegalStateException("Authentication Filter - operations performed from unregistered cluster");
    }
  }

  public static enum Action {
    HEAVY_PING,
    PING,
  }

  /**
   * Gets all registered clusters
   *
   * @return list of RegisteredClusterJSON
   */
  public List<RegisteredClusterJSON> getAll() {
    List<RegisteredCluster> registeredClusters = clusterFacade.findAll();
    List<RegisteredClusterJSON> to_ret = new ArrayList<>();
    for (RegisteredCluster r : registeredClusters) {
      to_ret.add(new RegisteredClusterJSON(r.getPublicId(), r.getDelaEndpoint(), r.getDateRegistered(),
        r.getHttpEndpoint()));
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
    Optional<RegisteredCluster> cluster = clusterFacade.findByEmail(clusterEmail.toLowerCase());
    if (!cluster.isPresent()) {
      throw new IllegalArgumentException("Cluster email not assigned.");
    }
    return cluster.get();
  }

  /**
   *
   * @param clusterId
   * @return
   */
  public Optional<RegisteredCluster> getClusterByPublicId(String clusterId) {
    if (clusterId == null || clusterId.isEmpty()) {
      throw new IllegalArgumentException("Cluster id not assigned.");
    }
    Optional<RegisteredCluster> cluster = clusterFacade.findByPublicId(clusterId);
    return cluster;
  }

  /**
   *
   * @param clusterId
   */
  public void removeClusterByPublicId(String clusterId) {
    if (clusterId == null || clusterId.isEmpty()) {
      throw new IllegalArgumentException("Cluster id not assigned.");
    }
    Optional<RegisteredCluster> cluster = clusterFacade.findByPublicId(clusterId);
    if (cluster.isPresent()) {
      clusterFacade.remove(cluster.get());
    }
  }
}
