package io.hops.site.util;

public class DatasetHelper {

  public static String getPublicDatasetId(String projectName, String datasetName, int version) {
    return projectName + "_" + datasetName + "_" + version;
  }
}
