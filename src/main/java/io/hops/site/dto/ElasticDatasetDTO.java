package io.hops.site.dto;

import com.google.gson.Gson;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
@XmlRootElement
public class ElasticDatasetDTO {
  private String name;
  private String description;

  public ElasticDatasetDTO() {
  }

  public ElasticDatasetDTO(String name, String description) {
    this.name = name;
    this.description = description;
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
  
  public static ElasticDatasetDTO from(PublishDatasetDTO.Request dataset) {
    return new ElasticDatasetDTO(dataset.getName(), dataset.getDescription());
  }
  
  public String json() {
    return new Gson().toJson(this);
  }
}
