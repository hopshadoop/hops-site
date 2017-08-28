package io.hops.site.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClusterAddressDTO {
  private String clusterId;
  private String delaTransferAddress;
  private String delaClusterAddress;

  public ClusterAddressDTO() {
  }

  public ClusterAddressDTO(String clusterId, String delaTransferAddress, String delaClusterAddress) {
    this.clusterId = clusterId;
    this.delaTransferAddress = delaTransferAddress;
    this.delaClusterAddress = delaClusterAddress;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getDelaTransferAddress() {
    return delaTransferAddress;
  }

  public void setDelaTransferAddress(String delaTransferAddress) {
    this.delaTransferAddress = delaTransferAddress;
  }

  public String getDelaClusterAddress() {
    return delaClusterAddress;
  }

  public void setDelaClusterAddress(String delaClusterAddress) {
    this.delaClusterAddress = delaClusterAddress;
  }
}