package io.hops.site.dto.internal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ElasticDoc {

  public static final String DOC_TYPE = "dataset";
  public static final String NAME_FIELD = "name";
  public static final String DESCRIPTION_FIELD = "description";
  public static final String ID_FIELD = "id";

  private String id;
  private String name;
  private String description;

  public ElasticDoc() {
  }

  public ElasticDoc(String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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
}
