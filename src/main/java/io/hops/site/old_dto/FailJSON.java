package io.hops.site.old_dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FailJSON {

  private String details;

  public FailJSON() {
  }

  public FailJSON(String details) {
    this.details = details;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

}
