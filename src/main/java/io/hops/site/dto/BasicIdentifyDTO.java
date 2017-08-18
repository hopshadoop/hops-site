package io.hops.site.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
@XmlRootElement
public class BasicIdentifyDTO {
  private String clusterId;
  private String datasetId;

  public BasicIdentifyDTO() {
  }

  public BasicIdentifyDTO(String clusterId, String datasetId) {
    this.clusterId = clusterId;
    this.datasetId = datasetId;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getDatasetId() {
    return datasetId;
  }

  public void setDatasetId(String datasetId) {
    this.datasetId = datasetId;
  }
}
