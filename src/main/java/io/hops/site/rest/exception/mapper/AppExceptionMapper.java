package io.hops.site.rest.exception.mapper;

import io.hops.site.old_dto.JsonResponse;
import io.hops.site.rest.exception.AppException;
import io.hops.site.rest.exception.ThirdPartyException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppExceptionMapper implements ExceptionMapper<Exception> {

  private final static Logger LOG = Logger.getLogger(AppExceptionMapper.class.getName());

  @Override
  public Response toResponse(Exception ex) {
    if (ex instanceof ThirdPartyException) {
      return handleThirdPartyException((ThirdPartyException)ex);
    } else if(ex instanceof AppException){
      return handleAppException((AppException)ex);
    } else {
      ex.printStackTrace();
      LOG.log(Level.WARNING, "Exception: {0}", ex.getMessage());
      return Response.status(Response.Status.EXPECTATION_FAILED).build();
    }
  }
  
  private Response handleThirdPartyException(ThirdPartyException tpe) {
    LOG.log(Level.WARNING, "Source:<{0}:{1}>ThirdPartyException: {2}",
      new Object[]{tpe.getSource(), tpe.getSourceDetails(), tpe.getMessage()});
    JsonResponse jsonResponse = new JsonResponse();
    jsonResponse.setStatus(Response.Status.EXPECTATION_FAILED.getReasonPhrase());
    jsonResponse.setStatusCode(Response.Status.EXPECTATION_FAILED.getStatusCode());
//    jsonResponse.setErrorMsg(tpe.getSource() + ":" + tpe.getSourceDetails() + ":" + tpe.getMessage());
    jsonResponse.setErrorMsg(tpe.getMessage());
    return Response.status(Response.Status.EXPECTATION_FAILED).entity(jsonResponse).build();
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
