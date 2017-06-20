package io.hops.site.dto;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisteredClusterJSON {

  private String clusterId;

  private String searchEndpoint;

  private AddressJSON gvodEndpoint;

  private long heartbeatsMissed;

  private Date dateRegistered;

  private Date dateLastPing;

  public RegisteredClusterJSON(String clusterId, AddressJSON gvodEndpoint, long heartbeatsMissed, Date dateRegistered,
          Date dateLastPing, String searchEndpoint) {
    this.clusterId = clusterId;
    this.gvodEndpoint = gvodEndpoint;
    this.heartbeatsMissed = heartbeatsMissed;
    this.dateRegistered = dateRegistered;
    this.dateLastPing = dateLastPing;
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

  public void setGvodEndpoint(AddressJSON gvodEndpoint) {
    this.gvodEndpoint = gvodEndpoint;
  }

  public void setHeartbeatsMissed(long heartbeatsMissed) {
    this.heartbeatsMissed = heartbeatsMissed;
  }

  public void setDateRegistered(Date dateRegistered) {
    this.dateRegistered = dateRegistered;
  }

  public void setDateLastPing(Date dateLastPing) {
    this.dateLastPing = dateLastPing;
  }

  public String getSearchEndpoint() {
    return searchEndpoint;
  }

  public String getClusterId() {
    return clusterId;
  }

  public AddressJSON getGvodEndpoint() {
    return gvodEndpoint;
  }

  public long getHeartbeatsMissed() {
    return heartbeatsMissed;
  }

  public Date getDateRegistered() {
    return dateRegistered;
  }

  public Date getDateLastPing() {
    return dateLastPing;
  }

}
