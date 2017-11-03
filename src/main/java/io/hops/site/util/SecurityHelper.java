package io.hops.site.util;

import javax.ws.rs.core.SecurityContext;

public class SecurityHelper {
  public static String getClusterRole(SecurityContext sc) {
    if (sc.isUserInRole("clusters")) {
      return "clusters";
    } else if (sc.isUserInRole("admin")) {
      return "admin";
    } else {
      return "none";
    }
  }
}
