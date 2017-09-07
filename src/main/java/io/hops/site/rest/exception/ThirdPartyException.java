package io.hops.site.rest.exception;

public class ThirdPartyException extends AppException {

  private Source source;
  private String sourceDetails;
  
  public ThirdPartyException(int status, String msg, Source source, String sourceDetails) {
    super(status, msg);
    this.source = source;
    this.sourceDetails = sourceDetails;
  }
  
  public ThirdPartyException(int status, Error msg, Source source, String sourceDetails) {
    super(status, msg.toString());
    this.source = source;
    this.sourceDetails = sourceDetails;
  }

  public Source getSource() {
    return source;
  }

  public void setSource(Source source) {
    this.source = source;
  }

  public String getSourceDetails() {
    return sourceDetails;
  }

  public void setSourceDetails(String sourceDetails) {
    this.sourceDetails = sourceDetails;
  }
  
  public static enum Source {
    LOCAL, SETTINGS, MYSQL, HDFS, KAFKA, DELA, REMOTE_DELA, HOPS_SITE
  }
  
  public static ThirdPartyException from(AppException ex, Source source, String sourceDetails) {
    return new ThirdPartyException(ex.getStatus(), ex.getMessage(), source, sourceDetails);
  }
  
  public static enum Error {
    CERT_MISSING("certificate missing"),
    CERT_MISSMATCH("certificate missmatch"),
    EMAIL_MISSING("email missing"),
    CLUSTER_NOT_REGISTERED("cluster not registered"),
    CLUSTER_ID_MISSING("cluster id param missing"),
    IMPERSONATION("cluster id does not match certificate"),
    USER_NOT_REGISTERED("user not registered"),
    HEAVY_PING("heavy ping required"),
    DATASET_EXISTS("dataset exists"),
    DATASET_DOES_NOT_EXIST("dataset does not exist");
    
    private final String msg;
    
    Error(String msg) {
      this.msg = msg;
    }
    
    public boolean is(String msg) {
      return this.msg.equals(msg);
    }

    @Override
    public String toString() {
      return msg;
    }
  }
}
