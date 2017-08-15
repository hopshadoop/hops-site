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
import io.hops.site.dto.JsonResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
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
    String clusterEmail = getCertificateCommonName(principalCert);
    if (clusterEmail.isEmpty()) { //Common name not set
      requestContext.abortWith(buildResponse("Common name not set.", Response.Status.UNAUTHORIZED));
      return;
    }

    String path = requestContext.getUriInfo().getPath();
    Method method = resourceInfo.getResourceMethod();
    LOGGER.log(Level.INFO, "Path: {0}, method: {1}", new Object[]{path, method});

    RegisteredCluster clusterFromCert = clusterController.getClusterByEmail(clusterEmail);
    if (clusterFromCert == null && !"cluster/register".equals(path)) { //not yet registerd
      requestContext.abortWith(buildResponse("Cluster not registerd.", Response.Status.FORBIDDEN));
      return;
    }

    if (clusterFromCert != null && "cluster/register".equals(path)) { //already registered
      requestContext.abortWith(buildResponse("Cluster already registered.", Response.Status.FORBIDDEN));
      return;
    }

    RegisteredCluster clusterInReq = getClusterFromReq(requestContext);
    if (clusterInReq == null) { // not protected ex. getRole
      return;
    }

    if (!matchCerts(clusterInReq.getCert(), principalCert)) { // req as different cluster
      requestContext.abortWith(buildResponse("Cluster in request do not match certificat.", Response.Status.FORBIDDEN));
    }
  }

  private String getCertificateCommonName(X509Certificate principalCert) {
    String tmpName, name = "";
    Principal principal = principalCert.getSubjectDN();
    // Extract the common name (CN)
    int start = principal.getName().indexOf("CN");
    if (start > -1) {
      tmpName = principal.getName().substring(start + 3);
      int end = tmpName.indexOf(",");
      if (end > 0) {
        name = tmpName.substring(0, end);
      } else {
        name = tmpName;
      }
      LOGGER.log(Level.INFO, "Request from principal: {0}", principal.getName());
    }
    return name.toLowerCase();
  }

  private RegisteredCluster getClusterFromReq(ContainerRequestContext requestContext) {
    RegisteredCluster registeredCluster;

    ContainerRequest cr = (ContainerRequest) requestContext;
    if (cr.bufferEntity()) {
      GenericReqDTO reqDTO = null;
      try {
        reqDTO = cr.readEntity(GenericReqDTO.class);
      } catch (Exception nsme) {
        //nothing to do. It is not GenericRequestDTO, thus can not be checked.
      }
      if (reqDTO == null) {
        LOGGER.log(Level.INFO, "Not a generic request.");
        return null;
      }
      LOGGER.log(Level.INFO, "Generic request: {0}.", reqDTO.toString());
      if (reqDTO.getUser() != null) {
        registeredCluster = clusterController.getClusterById(reqDTO.getUser().getClusterId());
        if (registeredCluster == null) {
          throw new AccessControlException("Cluster not registered.");
        }
        return registeredCluster;
      }

      if (reqDTO.getClusterId() != null) {
        registeredCluster = clusterController.getClusterById(reqDTO.getClusterId());
        if (registeredCluster == null) {
          throw new AccessControlException("Cluster not yet registered.");
        }
        return registeredCluster;
      }
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
