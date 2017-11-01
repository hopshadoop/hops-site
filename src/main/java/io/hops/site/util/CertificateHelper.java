package io.hops.site.util;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;

public class CertificateHelper {

  private final static Logger LOGGER = Logger.getLogger(CertificateHelper.class.getName());

  public static String getOrgName(X509Certificate cert) {
    String org = getCertificatePart(cert, "O");
    String orgUnit = getCertificatePart(cert, "OU");
    String orgName = org + "_" + orgUnit;
    return orgName;
  }

  public static String getUserEmail(ContainerRequestContext requestContext) {
    X509Certificate cert = ((X509Certificate[]) requestContext.getProperty("javax.servlet.request.X509Certificate"))[0];
    return getCertificateEmail(cert);
  }

  public static String getCertificateEmail(X509Certificate principalCert) {
    return getCertificatePart(principalCert, "EMAILADDRESS");
  }

  public static String getCertificateSubject(X509Certificate cert) {
    return cert.getSubjectDN().getName();
  }

  public static String getCertificatePart(X509Certificate cert, String partName) {
    String tmpName, name = "";
    String subject = getCertificateSubject(cert);
    String part = partName + "=";
    int start = subject.indexOf(part);
    if (start > -1) {
      tmpName = subject.substring(start + part.length());
      int end = tmpName.indexOf(",");
      if (end > 0) {
        name = tmpName.substring(0, end);
      } else {
        name = tmpName;
      }
    }
    return name.toLowerCase();
  }

  public static boolean matchCerts(byte[] cert, X509Certificate principalCert) {
    try {
      return Arrays.equals(principalCert.getEncoded(), cert);
    } catch (CertificateEncodingException ex) {
      LOGGER.log(Level.SEVERE, "hops_site:cert - {0}", ex);
      return false;
    }
  }
}
