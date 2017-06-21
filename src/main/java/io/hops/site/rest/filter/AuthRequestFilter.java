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
package io.hops.site.rest.filter;

import io.hops.site.dto.GenericRequestDTO;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.cert.X509Certificate;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.server.ContainerRequest;

@Provider
public class AuthRequestFilter implements ContainerRequestFilter {

  private final static Logger LOGGER = Logger.getLogger(ContainerRequestFilter.class.getName());
  @Context
  private ResourceInfo resourceInfo;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    X509Certificate[] chain = (X509Certificate[]) requestContext.getProperty("javax.servlet.request.X509Certificate");
    if (chain != null && chain.length > 0) {
      String subject = chain[0].getSubjectDN().getName();
      LOGGER.log(Level.SEVERE, "Subject: {0}", subject);
//      CertificateSecurityContext securityContext = new CertificateSecurityContext(subject, chain);
//      requestContext.setSecurityContext(securityContext);
    }
    
    String path = requestContext.getUriInfo().getPath();
    Method method = resourceInfo.getResourceMethod();
    LOGGER.log(Level.INFO, "Path: {0}, method: {1}", new Object[]{path, method});
    ContainerRequest cr = (ContainerRequest) requestContext;
    if (cr.bufferEntity()) {
      GenericRequestDTO reqDTO = null;
      try {
        reqDTO = cr.readEntity(GenericRequestDTO.class);
      } catch (Exception nsme) {
        //nothing to do. It is not GenericRequestDTO, thus can not be checked.
      }
      if (reqDTO == null) {
        LOGGER.log(Level.INFO, "No Cluster Id");
      } else {
        LOGGER.log(Level.INFO, "Cluster Id: {0}", reqDTO.getClusterId());
        //check if cluster id matches Certificate.
      }
    }
  }

}
