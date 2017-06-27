package io.hops.site.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PingResponse {

  private List<RegisteredClusterJSON> clusters;

  public PingResponse() {

  }

  public PingResponse(List<RegisteredClusterJSON> clusters) {
    this.clusters = clusters;
  }

  public List<RegisteredClusterJSON> getClusters() {
    return clusters;
  }

  public void setClusters(List<RegisteredClusterJSON> clusters) {
    this.clusters = clusters;
  }

}
