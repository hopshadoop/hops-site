package io.hops.site.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterJSON {

  private String searchEndpoint;
  private AddressJSON gvodEndpoint;
  private String email;
  private String cert;

  public RegisterJSON() {
  }

  public String getSearchEndpoint() {
    return searchEndpoint;
  }

  public void setSearchEndpoint(String searchEndpoint) {
    this.searchEndpoint = searchEndpoint;
  }

  public AddressJSON getGvodEndpoint() {
    return gvodEndpoint;
  }

  public void setGvodEndpoint(AddressJSON gvodEndpoint) {
    this.gvodEndpoint = gvodEndpoint;
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


}
