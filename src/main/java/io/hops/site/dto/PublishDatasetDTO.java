package io.hops.site.dto;

import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class PublishDatasetDTO {
  
  @XmlRootElement
  public static class Request {

    private String clusterId;
    private String name;
    private String description;
    private Collection<String> categoryCollection;

    public Request() {
    }

    public String getClusterId() {
      return clusterId;
    }

    public void setClusterId(String clusterId) {
      this.clusterId = clusterId;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public Collection<String> getCategoryCollection() {
      return categoryCollection;
    }

    public void setCategoryCollection(Collection<String> categoryCollection) {
      this.categoryCollection = categoryCollection;
    }
  }

  @XmlRootElement
  public static class Response {

    private String datasetPublicId;

    public Response() {
      this.datasetPublicId = datasetPublicId;
    }

    public Response(String datasetPublicId) {
      this.datasetPublicId = datasetPublicId;
    }

    public String getDatasetPublicId() {
      return datasetPublicId;
    }

    public void setDatasetPublicId(String datasetPublicId) {
      this.datasetPublicId = datasetPublicId;
    }
  }
}
