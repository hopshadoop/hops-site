package io.hops.site.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterJSON {

  private String httpEndpoint;
  private String delaEndpoint;
  private String email;
  private String cert;
  private byte[] derCert;

  public RegisterJSON() {
  }

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

  public String getCert() {
    return cert;
  }

  public void setCert(String cert) {
    this.cert = cert;
  }

  public byte[] getDerCert() {
    return derCert;
  }

  public void setDerCert(byte[] derCert) {
    this.derCert = derCert;
  }

  @Override
  public String toString() {
    return "RegisterJSON{" + "delaEndpoint=" + httpEndpoint + ", delaEndpoint=" + delaEndpoint + ", email=" + email +
            ", cert=" + cert + '}';
  }


}
