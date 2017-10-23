package io.hops.site.util;

import io.hops.site.dao.entity.RegisteredCluster;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import io.hops.site.rest.exception.ThirdPartyException;
import java.util.Optional;
import javax.ws.rs.core.Response;

public class ClusterHelper {
  public static RegisteredCluster getCluster(RegisteredClusterFacade clusterFacade, String publicCId) 
    throws ThirdPartyException {
    Optional<RegisteredCluster> cluster = clusterFacade.findByPublicId(publicCId);
    if (!cluster.isPresent()) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(), 
        ThirdPartyException.Error.CLUSTER_NOT_REGISTERED, ThirdPartyException.Source.REMOTE_DELA, "access control");
    }
    return cluster.get();
  }
}
