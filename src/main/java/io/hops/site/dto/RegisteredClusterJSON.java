package io.hops.site.dto;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisteredClusterJSON {

  private String clusterId;
  private String searchEndpoint;
  private String delaEndpoint;
  private Date dateRegistered;

  public RegisteredClusterJSON(String clusterId, String delaEndpoint, Date dateRegistered,
    String searchEndpoint) {
    this.clusterId = clusterId;
    this.delaEndpoint = delaEndpoint;
    this.dateRegistered = dateRegistered;
    this.searchEndpoint = searchEndpoint;
  }

  public RegisteredClusterJSON() {
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public void setSearchEndpoint(String searchEndpoint) {
    this.searchEndpoint = searchEndpoint;
  }

  public void setDelaEndpoint(String delaEndpoint) {
    this.delaEndpoint = delaEndpoint;
  }

  public void setDateRegistered(Date dateRegistered) {
    this.dateRegistered = dateRegistered;
  }

  public String getSearchEndpoint() {
    return searchEndpoint;
  }

  public String getClusterId() {
    return clusterId;
  }

  public String getDelaEndpoint() {
    return delaEndpoint;
  }

  public Date getDateRegistered() {
    return dateRegistered;
  }
}
