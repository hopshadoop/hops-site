package io.hops.site.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisteredJSON {

  private String clusterId;

  public RegisteredJSON() {
  }

  public RegisteredJSON(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

}
