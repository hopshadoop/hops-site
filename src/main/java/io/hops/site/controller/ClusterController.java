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

import io.hops.site.common.Settings;
import io.hops.site.dao.entity.Heartbeat;
import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dao.facade.HeartbeatFacade;
import io.hops.site.dao.facade.LiveDatasetFacade;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import io.hops.site.dto.AddressJSON;
import io.hops.site.dto.RegisterDTO;
import io.hops.site.rest.ClusterService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

  /**
   * Register a new cluster
   *
   * @param msg
   * @return
   */
  public String registerCluster(byte[] cert, RegisterDTO.Req msg) {
    Optional<RegisteredCluster> c = clusterFacade.findByEmail(msg.getEmail());
    if (c.isPresent()) {
      return c.get().getPublicId();
    }
    String clusterPublicId = settings.getClusterId();
    RegisteredCluster cluster = new RegisteredCluster(clusterPublicId, msg.getHttpEndpoint(),
      msg.getEmail().toLowerCase(), cert, msg.getDelaEndpoint());
    clusterFacade.create(cluster);
    return clusterPublicId;
  }

  public Action ping(String clusterPublicId) {
    Optional<RegisteredCluster> cluster = this.clusterFacade.findByPublicId(clusterPublicId);
    if (cluster.isPresent()) {
      Optional<Heartbeat> h = heartbeatFacade.findByClusterId(cluster.get().getId());
      if (h.isPresent()) {
        ping(h.get());
        return Action.PING;
      } else {
        return Action.HEAVY_PING;
      }
    } else {
      throw new IllegalStateException("Authentication Filter - operations performed from unregistered cluster");
    }
  }

  private void ping(Heartbeat heartbeat) {
    heartbeat.setLastPinged(settings.getDateNow());
    heartbeatFacade.edit(heartbeat);
  }

  public Action heavyPing(String clusterPublicId, List<String> upldDSPublicIds, List<String> dwnlDSPublicIds) {
    Optional<RegisteredCluster> c = this.clusterFacade.findByPublicId(clusterPublicId);
    if (c.isPresent()) {
      RegisteredCluster cluster = c.get();
      Optional<Heartbeat> h = heartbeatFacade.findByClusterId(cluster.getId());
      if (h.isPresent()) {
        ping(h.get());
      } else {
        heartbeatFacade.create(new Heartbeat(cluster.getId(), settings.getDateNow()));
      }
      liveDatasetFacade.downloadDatasets(cluster.getId(), datasetFacade.findIds(upldDSPublicIds));
      liveDatasetFacade.uploadDatasets(cluster.getId(), datasetFacade.findIds(dwnlDSPublicIds));
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
  public List<AddressJSON> getAll() {
    List<RegisteredCluster> registeredClusters = clusterFacade.findAll();
    List<AddressJSON> to_ret = new ArrayList<>();
    for (RegisteredCluster r : registeredClusters) {
      to_ret.add(new AddressJSON(r.getPublicId(), r.getDelaEndpoint(), r.getHttpEndpoint()));
    }
    return to_ret;
  }

  /**
   *
   * @param clusterEmail
   * @return
   */
  public Optional<RegisteredCluster> getClusterByEmail(String clusterEmail) {
    if (clusterEmail == null || clusterEmail.isEmpty()) {
      throw new IllegalArgumentException("Cluster email not assigned.");
    }
    Optional<RegisteredCluster> cluster = clusterFacade.findByEmail(clusterEmail.toLowerCase());
    return cluster;
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
