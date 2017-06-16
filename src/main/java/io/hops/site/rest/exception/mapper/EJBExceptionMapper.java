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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
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
    JsonResponse jsonResponse = new JsonResponse();
    LOGGER.log(Level.INFO, "EJB Exception Mapper: {0}", exception.getCause().getMessage());
    jsonResponse.setStatus(Response.Status.BAD_REQUEST.getReasonPhrase());
    jsonResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
    jsonResponse.setErrorMsg(exception.getCause().getMessage());
    return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
  }
  
}
