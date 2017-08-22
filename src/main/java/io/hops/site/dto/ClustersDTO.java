package io.hops.site.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClustersDTO {
  private List<AddressJSON> clusters;

  public ClustersDTO() {
  }

  public ClustersDTO(List clusters) {
    this.clusters = clusters;
  }

  public List<AddressJSON> getClusters() {
    return clusters;
  }

  public void setClusters(List<AddressJSON> clusters) {
    this.clusters = clusters;
  }
}
