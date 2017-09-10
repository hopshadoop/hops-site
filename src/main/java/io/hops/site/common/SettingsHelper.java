package io.hops.site.common;

import io.hops.site.dao.entity.Users;
import io.hops.site.dao.facade.UsersFacade;
import io.hops.site.rest.exception.ThirdPartyException;
import java.util.Optional;
import javax.ws.rs.core.Response;

public class SettingsHelper {

  public static Users getUser(UsersFacade userFacade, String publicCId, String userEmail) throws ThirdPartyException {
    Optional<Users> user = userFacade.findByEmailAndPublicClusterId(userEmail, publicCId);
    if (!user.isPresent()) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(),
        ThirdPartyException.Error.USER_NOT_REGISTERED, ThirdPartyException.Source.REMOTE_DELA, "bad request");
    }
    return user.get();
  }
}
