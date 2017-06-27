package io.hops.site.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IdentificationJSON {

  private String clusterId;

  public IdentificationJSON() {
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  @Override
  public String toString() {
    return "IdentificationJSON{" + "clusterId=" + clusterId + '}';
  }

}
