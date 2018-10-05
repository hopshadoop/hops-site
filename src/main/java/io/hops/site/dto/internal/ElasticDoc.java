package io.hops.site.dto.internal;

import java.io.IOException;
import javax.xml.bind.annotation.XmlRootElement;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

@XmlRootElement
public class ElasticDoc {

  public static final String DOC_TYPE = "dataset";
  public static final String NAME_FIELD = "name";
  public static final String VERSION_FIELD = "dsv";
  public static final String DESCRIPTION_FIELD = "description";
  public static final String ID_FIELD = "id";

  private String id;
  private String name;
  private int dsv;
  private String description;

  public ElasticDoc() {
  }

  public ElasticDoc(String id, String name, int dsv, String description) {
    this.id = id;
    this.name = name;
    this.dsv = dsv;
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

  public int getDsv() {
    return dsv;
  }

  public void setDsv(int dsv) {
    this.dsv = dsv;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
  
  public XContentBuilder elasticSerialize() throws IOException {
    return XContentFactory.jsonBuilder().startObject()
      .field(ID_FIELD, id)
      .field(NAME_FIELD, name)
      .field(VERSION_FIELD, dsv)
      .field(DESCRIPTION_FIELD, description)
      .endObject();
  }
}
