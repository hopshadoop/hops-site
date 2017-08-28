package io.hops.site.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

public class ClusterServiceDTO {

  @XmlRootElement
  public static class Register {

    private String delaTransferAddress;
    private String delaClusterAddress;
    private String email;

    public Register() {
    }

    public Register(String delaTransferAddress, String delaClusterAddress, String email) {
      this.delaTransferAddress = delaTransferAddress;
      this.delaClusterAddress = delaClusterAddress;
      this.email = email;
    }

    public String getDelaTransferAddress() {
      return delaTransferAddress;
    }

    public void setDelaTransferAddress(String delaTransferAddress) {
      this.delaTransferAddress = delaTransferAddress;
    }

    public String getDelaClusterAddress() {
      return delaClusterAddress;
    }

    public void setDelaClusterAddress(String delaClusterAddress) {
      this.delaClusterAddress = delaClusterAddress;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }
  }

  @XmlRootElement
  public static class HeavyPing {

    private List<String> upldDSIds;
    private List<String> dwnlDSIds;

    public HeavyPing() {
    }

    public HeavyPing(List<String> upldDSIds, List<String> dwnlDSIds) {
      this.upldDSIds = upldDSIds;
      this.dwnlDSIds = dwnlDSIds;
    }

    public List<String> getUpldDSIds() {
      return upldDSIds;
    }

    public void setUpldDSIds(List<String> upldDSIds) {
      this.upldDSIds = upldDSIds;
    }

    public List<String> getDwnlDSIds() {
      return dwnlDSIds;
    }

    public void setDwnlDSIds(List<String> dwnlDSIds) {
      this.dwnlDSIds = dwnlDSIds;
    }
  }
}
