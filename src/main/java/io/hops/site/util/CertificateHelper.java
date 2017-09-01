package io.hops.site.util;

import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CertificateHelper {

  private final static Logger LOGGER = Logger.getLogger(CertificateHelper.class.getName());

  public static String getCertificatePart(X509Certificate principalCert, String partName) {
    String tmpName, name = "";
    Principal principal = principalCert.getSubjectDN();
    // Extract the email
    String email = partName + "=";
    int start = principal.getName().indexOf(email);
    if (start > -1) {
      tmpName = principal.getName().substring(start + email.length());
      int end = tmpName.indexOf(",");
      if (end > 0) {
        name = tmpName.substring(0, end);
      } else {
        name = tmpName;
      }
      LOGGER.log(Level.INFO, "Request from principal: {0}", principal.getName());
      LOGGER.log(Level.INFO, "Request with email: {0}", name.toLowerCase());
    }
    return name.toLowerCase();
  }
}
