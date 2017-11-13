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

import io.hops.site.dao.entity.Heartbeat;
import io.hops.site.dao.entity.LiveDataset;
import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dao.facade.DatasetHealthFacade;
import io.hops.site.dao.facade.HeartbeatFacade;
import io.hops.site.dao.facade.LiveDatasetFacade;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import io.hops.site.dto.ClusterAddressDTO;
import io.hops.site.dto.ClusterServiceDTO;
import io.hops.site.rest.ClusterService;
import io.hops.site.rest.exception.ThirdPartyException;
import io.hops.site.util.CertificateHelper;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.core.Response;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ClusterController {

  private final static Logger LOGGER = Logger.getLogger(ClusterService.class.getName());
  @EJB
  private HopsSiteSettings settings;
  @EJB
  private RegisteredClusterFacade clusterFacade;
  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private HeartbeatFacade heartbeatFacade;
  @EJB
  private LiveDatasetFacade liveDatasetFacade;
  @EJB
  private DatasetHealthFacade datasetHealthFacade;

  /**
   * Register a new cluster
   *
   * @param cert
   * @param orgName
   * @param msg
   * @return
   */
  public String registerCluster(X509Certificate cert, ClusterServiceDTO.Register msg) throws ThirdPartyException {
    String subject = CertificateHelper.getCertificateSubject(cert);
    byte[] certByte;
    try {
      certByte = cert.getEncoded();
    } catch (CertificateEncodingException ex) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(), "certificate issue",
        ThirdPartyException.Source.REMOTE_DELA, "bad request");
    }
    String orgName = CertificateHelper.getOrgName(cert);
    String email = CertificateHelper.getCertificateEmail(cert);
    LOGGER.log(Level.INFO, "registering subject:{0}, email:{1}", new Object[]{subject, email});
    Optional<RegisteredCluster> c = clusterFacade.findBySubject(subject);
    if (c.isPresent()) {
      RegisteredCluster cluster = c.get();
      cluster.setDelaEndpoint(msg.getDelaTransferAddress());
      cluster.setHttpEndpoint(msg.getDelaClusterAddress());
      cluster.setCert(certByte);
      cluster.setOrgName(orgName);
      cluster.setEmail(email.toLowerCase());
      cluster.setSubject(subject);
      clusterFacade.edit(cluster);
      Optional<Heartbeat> h = heartbeatFacade.findByClusterId(cluster.getId());
      if (h.isPresent()) {
        heartbeatFacade.remove(h.get());
      }
      return c.get().getPublicId();
    } else {
      String clusterPublicId = HopsSiteSettings.getClusterId();
      RegisteredCluster cluster = new RegisteredCluster(clusterPublicId, msg.getDelaTransferAddress(),
        msg.getDelaClusterAddress(), email.toLowerCase(), certByte, orgName, subject);
      clusterFacade.create(cluster);
      return clusterPublicId;
    }
  }

  public Action ping(String publicCId, ClusterServiceDTO.Ping msg) {
    Optional<RegisteredCluster> cluster = this.clusterFacade.findByPublicId(publicCId);
    if (cluster.isPresent()) {
      Optional<Heartbeat> h = heartbeatFacade.findByClusterId(cluster.get().getId());
      if (h.isPresent()) {
        if (pingSanityCheck(cluster.get().getId(), msg)) {
          ping(h.get());
          return Action.PING;
        }
      }
      return Action.HEAVY_PING;
    } else {
      return Action.REGISTER;
    }
  }

  private boolean pingSanityCheck(int clusterId, ClusterServiceDTO.Ping msg) {
    List<LiveDataset> liveDatasets = liveDatasetFacade.liveDatasets(clusterId);
    if (liveDatasets.size() != msg.getDwnlDSSize() + msg.getUpldDSSize()) {
      return false;
    }

    int dwnl = 0;
    int upld = 0;
    for (LiveDataset liveDataset : liveDatasets) {
      if (liveDataset.uploadStatus()) {
        upld++;
      } else if (liveDataset.downloadStatus()) {
        dwnl++;
      }
    }
    return dwnl == msg.getDwnlDSSize() && upld == msg.getUpldDSSize();
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
      Collection<Integer> downloadDatasets = datasetFacade.findIds(dwnlDSIds).values();
      Collection<Integer> uploadDatasets = datasetFacade.findIds(upldDSIds).values();
      liveDatasetFacade.downloadDatasets(cluster.getId(), downloadDatasets);
      liveDatasetFacade.uploadDatasets(cluster.getId(), uploadDatasets);
      datasetHealthFacade.downloadDatasets(downloadDatasets);
      datasetHealthFacade.uploadDatasets(uploadDatasets);
      return Action.HEAVY_PING;
    } else {
      return Action.REGISTER;

    }
  }

  public static enum Action {

    REGISTER,
    HEAVY_PING,
    PING;
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
  public Optional<RegisteredCluster> getClusterBySubject(String subject) {
    if (subject == null || subject.isEmpty()) {
      throw new IllegalArgumentException("Cluster subject not assigned.");
    }
    Optional<RegisteredCluster> cluster = clusterFacade.findBySubject(subject);
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
