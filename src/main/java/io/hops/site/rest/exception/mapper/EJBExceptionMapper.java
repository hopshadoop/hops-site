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
package io.hops.site.rest.exception.mapper;

import io.hops.site.dto.JsonResponse;
import java.security.AccessControlException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EJBExceptionMapper implements ExceptionMapper<EJBException> {

  private final static Logger LOGGER = Logger.getLogger(EJBExceptionMapper.class.getName());

  @Override
  @Produces(MediaType.APPLICATION_JSON)
  public Response toResponse(EJBException exception) {
    if (exception.getCause() instanceof IllegalArgumentException) {
      return handleIllegalArgumentException((IllegalArgumentException) exception.getCause());
    } else if (exception.getCause() instanceof AccessControlException) {
      return handleAccessControlException((AccessControlException) exception.getCause());
    } else if (exception.getCause() instanceof ConstraintViolationException) {
      return handleConstraintViolation((ConstraintViolationException) exception.getCause());
    }
    
    LOGGER.log(Level.INFO, "EJBException: {0}", exception.getCause().getMessage());
    JsonResponse jsonResponse = new JsonResponse();
    jsonResponse.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
    jsonResponse.setStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    jsonResponse.setErrorMsg(exception.getCause().getMessage());
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse).build();
  }

  private Response handleConstraintViolation(ConstraintViolationException cve) {
    LOGGER.log(Level.INFO, "ConstraintViolationException: {0}", cve.getMessage());
    JsonResponse jsonResponse = new JsonResponse();
    jsonResponse.setStatus(Response.Status.BAD_REQUEST.getReasonPhrase());
    jsonResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
    StringBuilder sb = new StringBuilder();
    Set<ConstraintViolation<?>> cvs = cve.getConstraintViolations();
    for (ConstraintViolation<?> cv : cvs) {
      LOGGER.log(Level.INFO, "Violation: {0}", cv.getMessage());
      sb.append(cv.getMessage()).append("\n");
    }
    if (sb.toString().isEmpty()) {
      jsonResponse.setErrorMsg(cve.getMessage());
    } else {
      jsonResponse.setErrorMsg(sb.toString());
    }

    return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
  }

  private Response handleAccessControlException(AccessControlException ace) {
    LOGGER.log(Level.INFO, "AccessControlException: {0}", ace.getMessage());
    JsonResponse jsonResponse = new JsonResponse();
    jsonResponse.setStatus(Response.Status.FORBIDDEN.getReasonPhrase());
    jsonResponse.setStatusCode(Response.Status.FORBIDDEN.getStatusCode());
    jsonResponse.setErrorMsg(ace.getMessage());
    return Response.status(Response.Status.FORBIDDEN).entity(jsonResponse).build();
  }

  private Response handleIllegalArgumentException(IllegalArgumentException iae) {
    LOGGER.log(Level.INFO, "IllegalArgumentException: {0}", iae.getMessage());
    JsonResponse jsonResponse = new JsonResponse();
    jsonResponse.setStatus(Response.Status.BAD_REQUEST.getReasonPhrase());
    jsonResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
    jsonResponse.setErrorMsg(iae.getMessage());
    return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
  }

}
