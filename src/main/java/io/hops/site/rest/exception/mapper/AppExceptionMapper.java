package io.hops.site.rest.exception.mapper;

import io.hops.site.common.AppException;
import io.hops.site.old_dto.JsonResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {

  private final static Logger LOG = Logger.getLogger(AppExceptionMapper.class.getName());

  @Override
  public Response toResponse(AppException ex) {
    return handleAppException(ex);
  }

   private Response handleAppException(AppException ae) {
    LOG.log(Level.WARNING, "AppExceptionMapper: {0}", ae.getClass());
    JsonResponse json = new JsonResponse();
    json.setStatus(Response.Status.EXPECTATION_FAILED.getReasonPhrase());
    json.setStatusCode(Response.Status.EXPECTATION_FAILED.getStatusCode());
    json.setErrorMsg(ae.getMessage());
    return Response.status(Response.Status.EXPECTATION_FAILED).entity(json).build();
  }
}
