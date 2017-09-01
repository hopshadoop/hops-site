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
import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.old_dto.JsonResponse;
import io.hops.site.util.CertificateHelper;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Optional;
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
import org.glassfish.jersey.server.ContainerRequest;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthFilter implements ContainerRequestFilter {

  private final static Logger LOGGER = Logger.getLogger(ContainerRequestFilter.class.getName());
  private Handler fh;

  @EJB
  private ClusterController clusterController;

  @Context
  private ResourceInfo resourceInfo;
  
  @PostConstruct
  private void init() {
    try {
      //path, size in bytes and, number of log files to use
      fh = new FileHandler("../logs/hops-site%g.log", 2000000, 50);
      LOGGER.addHandler(fh);
      SimpleFormatter formatter = new SimpleFormatter();  
      fh.setFormatter(formatter);
    } catch (IOException | SecurityException ex) {
      LOGGER.log(Level.SEVERE, null, ex.getMessage());
    }
    LOGGER.log(Level.INFO, "Hops-site auth filter log.");
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    X509Certificate[] certs = (X509Certificate[]) requestContext.getProperty("javax.servlet.request.X509Certificate");
    if (certs == null || certs.length < 1) {
      requestContext.abortWith(buildResponse("No cert found.", Response.Status.UNAUTHORIZED));
      return;
    }
    X509Certificate principalCert = certs[0];
    String clusterEmail = CertificateHelper.getCertificatePart(principalCert, "EMAILADDRESS");
    if (clusterEmail.isEmpty()) { //Common name not set
      requestContext.abortWith(buildResponse("Common name not set.", Response.Status.UNAUTHORIZED));
      return;
    }

    String path = requestContext.getUriInfo().getPath();
    Method method = resourceInfo.getResourceMethod();
    LOGGER.log(Level.INFO, "Path: {0}, method: {1}", new Object[]{path, method});

    Optional<RegisteredCluster> clusterFromCert = clusterController.getClusterByEmail(clusterEmail);
    if (!clusterFromCert.isPresent() && !allowedPath(path)) { //not yet registerd
      requestContext.abortWith(buildResponse("Cluster not registerd.", Response.Status.FORBIDDEN));
      return;
    }

    if (clusterFromCert.isPresent() && "cluster/register".equals(path)) { //already registered
      requestContext.abortWith(buildResponse("Cluster already registered.", Response.Status.FORBIDDEN));
      return;
    }

//    RegisteredCluster clusterInReq = getClusterFromReq(requestContext);
//    if (clusterInReq == null) { // not protected ex. getRole
//      return;
//    }

//    if (!matchCerts(clusterInReq.getCert(), principalCert)) { // req as different cluster
//      requestContext.abortWith(buildResponse("Cluster in request do not match certificat.", Response.Status.FORBIDDEN));
//    }
  }

  private boolean allowedPath(String path) {
    return "cluster/register".equals(path) ||
        "cluster/dela/version".equals(path);
  }
  private String getCertificateEmail(X509Certificate principalCert) {
    String tmpName, name = "";
    Principal principal = principalCert.getSubjectDN();
    // Extract the email
    String email = "EMAILADDRESS=";
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

  private RegisteredCluster getClusterFromReq(ContainerRequestContext requestContext) {
    ContainerRequest cr = (ContainerRequest) requestContext;
    if (cr.bufferEntity()) {
      GenericReqDTO reqDTO;
      try {
        reqDTO = cr.readEntity(GenericReqDTO.class);
      } catch (Exception nsme) {
        //nothing to do. It is not GenericRequestDTO, thus can not be checked.
        LOGGER.log(Level.INFO, "Not a generic request.");
        return null;
      }
      LOGGER.log(Level.INFO, "Generic request: {0}.", reqDTO.toString());
      String clusterPublicId;
      if (reqDTO.getUser() != null) {
        clusterPublicId = reqDTO.getUser().getClusterId();
      } else if (reqDTO.getClusterId() != null) {
        clusterPublicId = reqDTO.getClusterId();
      } else {
        throw new AccessControlException("update logic for GenericReqDTO");
      }
      Optional<RegisteredCluster> cluster = clusterController.getClusterByPublicId(clusterPublicId);
      if (!cluster.isPresent()) {
        throw new AccessControlException("Cluster not registered.");
      }
      return cluster.get();
    }
    return null;
  }

  private Response buildResponse(String message, Response.Status status) {
    JsonResponse json = new JsonResponse();
    json.setStatus(status.getReasonPhrase());
    json.setStatusCode(status.getStatusCode());
    json.setErrorMsg(message);
    return Response.status(status).entity(json).build();
  }

  private boolean matchCerts(byte[] cert, X509Certificate principalCert) {
    try {
      return Arrays.equals(principalCert.getEncoded(), cert);
    } catch (CertificateEncodingException ex) {
      LOGGER.log(Level.SEVERE, null, ex);
      return false;
    }
  }

}
