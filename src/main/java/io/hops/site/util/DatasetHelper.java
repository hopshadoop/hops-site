package io.hops.site.util;

import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.rest.exception.ThirdPartyException;
import java.util.Optional;
import javax.ws.rs.core.Response;

public class DatasetHelper {

  public static String getPublicDatasetId(String datasetName, int version) {
    return datasetName + "_" + version;
  }

  public static Dataset getDataset(DatasetFacade datasetFacade, String publicDId) throws ThirdPartyException {
    Optional<Dataset> dataset = datasetFacade.findByPublicId(publicDId);
    if (!dataset.isPresent()) {
      throw new ThirdPartyException(Response.Status.BAD_REQUEST.getStatusCode(),
        ThirdPartyException.Error.DATASET_DOES_NOT_EXIST, ThirdPartyException.Source.REMOTE_DELA, "dataset not found");
    }
    return dataset.get();
  }
}
