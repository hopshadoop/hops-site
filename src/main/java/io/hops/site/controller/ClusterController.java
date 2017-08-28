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
import io.hops.site.dto.ClusterAddressDTO;
import io.hops.site.dto.ClusterServiceDTO;
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
  public String registerCluster(byte[] cert, ClusterServiceDTO.Register msg) {
    Optional<RegisteredCluster> c = clusterFacade.findByEmail(msg.getEmail());
    if (c.isPresent()) {
      return c.get().getPublicId();
    }
    String clusterPublicId = settings.getClusterId();
    RegisteredCluster cluster = new RegisteredCluster(clusterPublicId, msg.getDelaTransferAddress(), 
      msg.getDelaClusterAddress(), msg.getEmail().toLowerCase(), cert);
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
      return Action.REGISTER;
    }
  }

  private void ping(Heartbeat heartbeat) {
    heartbeat.setLastPinged(settings.getDateNow());
    heartbeatFacade.edit(heartbeat);
  }

  public Action heavyPing(String publicCId, List<String> upldDSIds, List<String> dwnlDSIds) {
    Optional<RegisteredCluster> c = this.clusterFacade.findByPublicId(publicCId);
    if (c.isPresent()) {
      RegisteredCluster cluster = c.get();
      Optional<Heartbeat> h = heartbeatFacade.findByClusterId(cluster.getId());
      if (h.isPresent()) {
        ping(h.get());
      } else {
        heartbeatFacade.create(new Heartbeat(cluster.getId(), settings.getDateNow()));
      }
      liveDatasetFacade.downloadDatasets(cluster.getId(), datasetFacade.findIds(upldDSIds));
      liveDatasetFacade.uploadDatasets(cluster.getId(), datasetFacade.findIds(dwnlDSIds));
      return Action.HEAVY_PING;
    } else {
      return Action.REGISTER;
    }
  }

  public static enum Action {
    REGISTER("Cluster not registered."),
    HEAVY_PING("Heavy ping required"),
    PING("Ping");
    
    private String msg;

    Action(String msg) {
      this.msg = msg;
    }
    @Override
    public String toString() {
      return msg;
    }
  }

  /**
   * Gets all registered clusters
   *
   * @return list of RegisteredClusterJSON
   */
  public List<ClusterAddressDTO> getAll() {
    List<RegisteredCluster> registeredClusters = clusterFacade.findAll();
    List<ClusterAddressDTO> to_ret = new ArrayList<>();
    for (RegisteredCluster r : registeredClusters) {
      to_ret.add(new ClusterAddressDTO(r.getPublicId(), r.getDelaEndpoint(), r.getHttpEndpoint()));
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
