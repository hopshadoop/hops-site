package io.hops.site.dto;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterDTO {

  public static class Req {

    private String httpEndpoint;
    private String delaEndpoint;
    private String email;

    public String getHttpEndpoint() {
      return httpEndpoint;
    }

    public void setHttpEndpoint(String httpEndpoint) {
      this.httpEndpoint = httpEndpoint;
    }

    public String getDelaEndpoint() {
      return delaEndpoint;
    }

    public void setDelaEndpoint(String delaEndpoint) {
      this.delaEndpoint = delaEndpoint;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    @Override
    public String toString() {
      return "RegisterJSON{" + "delaEndpoint=" + httpEndpoint + ", delaEndpoint=" + delaEndpoint + ", email=" + email
        + '}';
    }
  }

  public static class Resp {
    private String clusterId;
    private Date dateRegistered;

    public Resp() {
    }

    public Resp(String clusterId, Date dateRegistered) {
      this.clusterId = clusterId;
      this.dateRegistered = dateRegistered;
    }

    public String getClusterId() {
      return clusterId;
    }

    public void setClusterId(String clusterId) {
      this.clusterId = clusterId;
    }

    public Date getDateRegistered() {
      return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
      this.dateRegistered = dateRegistered;
    }
  }
}
