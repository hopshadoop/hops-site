package io.hops.site.util;

public class DatasetHelper {

  public static String getPublicDatasetId(String datasetName, int version) {
    return datasetName + "_" + version;
  }
}
