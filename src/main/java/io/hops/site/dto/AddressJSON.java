package io.hops.site.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AddressJSON {

  private String clusterId;
  private String delaAddress;
  private String httpAddress;

  public AddressJSON() {
  }

  public AddressJSON(String clusterId, String delaAddress, String httpAddress) {
    this.delaAddress = delaAddress;
    this.httpAddress = httpAddress;
    this.clusterId = clusterId;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }
  
  public String getDelaAddress() {
    return delaAddress;
  }

  public void setDelaAddress(String delaAddress) {
    this.delaAddress = delaAddress;
  }

  public String getHttpAddress() {
    return httpAddress;
  }

  public void setHttpAddress(String httpAddress) {
    this.httpAddress = httpAddress;
  }
}
