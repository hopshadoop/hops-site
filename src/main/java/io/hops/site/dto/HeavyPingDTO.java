package io.hops.site.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
@XmlRootElement
public class HeavyPingDTO {
  private String clusterId;
  private List<String> upldDSIds;
  private List<String> dwnlDSIds;

  public HeavyPingDTO() {
  }

  public HeavyPingDTO(String clusterId, List<String> upldDSIds, List<String> dwnlDSIds) {
    this.clusterId = clusterId;
    this.upldDSIds = upldDSIds;
    this.dwnlDSIds = dwnlDSIds;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public List<String> getUpldDSIds() {
    return upldDSIds;
  }

  public void setUpldDSIds(List<String> upldDSIds) {
    this.upldDSIds = upldDSIds;
  }

  public List<String> getDwnlDSIds() {
    return dwnlDSIds;
  }

  public void setDwnlDSIds(List<String> dwnlDSIds) {
    this.dwnlDSIds = dwnlDSIds;
  }
}
