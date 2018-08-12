/*
 * Copyright 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hops.site.rest.request.filter;

import io.hops.site.controller.ClusterController;
import io.hops.site.controller.HopsSiteSettings;
import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.old_dto.JsonResponse;
import io.hops.site.rest.exception.ThirdPartyException;
import io.hops.site.util.CertificateHelper;
import io.hops.site.util.SecurityHelper;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthFilter implements ContainerRequestFilter {

  private final static Logger LOGGER = Logger.getLogger(ContainerRequestFilter.class.getName());
  private Handler fh;

  @EJB
  private ClusterController clusterController;

  @Context
  private ResourceInfo resourceInfo;

  private static final Set<String> publicPaths = new HashSet<String>() {
    {
      add("private/cluster/register");
    }
  };

  public static final String SEARCH_PREFIX = "dataset/search";

  public static boolean isSearch(String path) {
    if (path.startsWith(SEARCH_PREFIX)) {
      return true;
    }
    return false;
  }

  private static final String CLUSTER_ID_PARAM = "publicCId";

  @PostConstruct
  private void init() {
    try {
      //path, size in bytes and, number of log files to use
      fh = new FileHandler("../logs/tracker_access%g.log", 2000000, 50);
      LOGGER.addHandler(fh);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);
    } catch (IOException | SecurityException ex) {
      LOGGER.log(Level.SEVERE, null, ex.getMessage());
    }
    LOGGER.log(Level.INFO, "hops_site:auth_filter log.");
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String path = requestContext.getUriInfo().getPath();
    Method method = resourceInfo.getResourceMethod();
    if (path.startsWith("public") || path.startsWith("/public") || publicPaths.contains(path)) {
      LOGGER.log(Level.INFO, "public path:{0} method:{1}", new Object[]{path, method});
      return;
    } else {
      LOGGER.log(Level.INFO, "private path:{0} method:{1}", new Object[]{path, method});
    }
    X509Certificate[] certs = (X509Certificate[]) requestContext.getProperty("javax.servlet.request.X509Certificate");
    if (certs == null || certs.length < 1) {
      requestContext.abortWith(fail(ThirdPartyException.Error.CERT_MISSING));
      return;
    }
    String clusterSubject = CertificateHelper.getCertificateSubject(certs[0]);
    if (clusterSubject == null || clusterSubject.isEmpty()) {
      requestContext.abortWith(fail(ThirdPartyException.Error.EMAIL_MISSING));
      return;
    }

    if (isSearch(path)) {
      return;
    }
    String publicCId = requestContext.getUriInfo().getPathParameters().getFirst(CLUSTER_ID_PARAM);
    if (publicCId == null) {
      return;
    }
    Optional<RegisteredCluster> cluster = clusterController.getClusterBySubject(clusterSubject);
    if (!cluster.isPresent()) {
      requestContext.abortWith(fail(ThirdPartyException.Error.CLUSTER_NOT_REGISTERED));
      return;
    }
    if (!CertificateHelper.matchCerts(cluster.get().getCert(), certs[0])) {
      requestContext.abortWith(fail(ThirdPartyException.Error.CERT_MISSMATCH));
      return;
    }
    if (!cluster.get().getPublicId().equals(publicCId)) {
      requestContext.abortWith(fail(ThirdPartyException.Error.IMPERSONATION));
      return;
    }
    String role = SecurityHelper.getClusterRole(requestContext.getSecurityContext());
    LOGGER.log(Level.INFO, "cluster:{0} authenticated as:{1}", new Object[]{publicCId, role});
  }

  private Response fail(ThirdPartyException.Error msg) {
    JsonResponse json = new JsonResponse();
    json.setStatus(Response.Status.UNAUTHORIZED.getReasonPhrase());
    json.setStatusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    json.setErrorMsg(msg.toString());
    return Response.status(Response.Status.UNAUTHORIZED).entity(json).build();
  }
}
