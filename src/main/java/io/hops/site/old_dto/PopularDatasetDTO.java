package io.hops.site.old_dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PopularDatasetDTO {

  private String datasetId;
  private ManifestJSON manifestJson;
  private int leeches;
  private int seeds;
  private String clusterId;

  public PopularDatasetDTO() {
  }

  public PopularDatasetDTO(ManifestJSON manifestJson, String datasetId, int leeches, int seeds) {
    this.manifestJson = manifestJson;
    this.datasetId = datasetId;
    this.leeches = leeches;
    this.seeds = seeds;
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

  public int getLeeches() {
    return leeches;
  }

  public int getSeeds() {
    return seeds;
  }

  public void setDatasetId(String datasetId) {
    this.datasetId = datasetId;
  }

  public void setLeeches(int leeches) {
    this.leeches = leeches;
  }

  public void setSeeds(int seeds) {
    this.seeds = seeds;
  }

  public ManifestJSON getManifestJson() {
    return manifestJson;
  }

  public void setManifestJson(ManifestJSON manifestJson) {
    this.manifestJson = manifestJson;
  }

}
