package io.hops.site.common;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.javatuples.Pair;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 * from hopsworks - io.hops.hopsworks.common.util
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class Settings {

  private static final Logger logger = Logger.getLogger(Settings.class.getName());

  @PersistenceContext(unitName = "hops-sitePU")
  private EntityManager em;

  private boolean cached = false;

  private void populateCache() {
    if (!cached) {
      populateElasticCache();
    }
  }

  private void checkCache() {
    if (!cached) {
      populateCache();
      cached = true;
    }
  }

  public Variables findById(String id) {
    try {
      return em.createNamedQuery("Variables.findById", Variables.class).setParameter("id", id).
        getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  private String setIpVar(String varName, String defaultValue) {
    Variables var = findById(varName);
    if (var != null && var.getValue() != null && Ip.validIp(var.getValue())) {
      String val = var.getValue();
      if (val != null && val.isEmpty() == false) {
        return val;
      }
    }
    return defaultValue;
  }

  private Integer setIntVar(String varName, Integer defaultValue) {
    Variables var = findById(varName);
    try {
      if (var != null && var.getValue() != null) {
        String val = var.getValue();
        if (val != null && val.isEmpty() == false) {
          return Integer.parseInt(val);
        }
      }
    } catch (NumberFormatException ex) {
      logger.info("Error - not an integer! " + varName + " should be an integer. Value was " + defaultValue);
    }
    return defaultValue;
  }

  // Elasticsearch
  public static final String DELA_DOC_INDEX = "hops-site";
  public static final String DELA_DOC_TYPE = "dataset";
  public static final String DELA_DOC_NAME_FIELD = "name";
  public static final String DELA_DOC_DESCRIPTION_FIELD = "description";
  public static final String DELA_DOC_METADATA_FIELD = "xattr";
  public static final String DELA_DOC_METADATA_FIELDS = DELA_DOC_METADATA_FIELD + ".*";

  private static final String VARIABLE_ELASTIC_IP = "elastic_ip";
  private static final String VARIABLE_ELASTIC_PORT = "elastic_port";

  private String ELASTIC_IP = "127.0.0.1";
  private int ELASTIC_PORT = 9300;

  private void populateElasticCache() {
    ELASTIC_IP = setIpVar(VARIABLE_ELASTIC_IP, ELASTIC_IP);
    ELASTIC_PORT = setIntVar(VARIABLE_ELASTIC_PORT, ELASTIC_PORT);
  }

  public synchronized String getElasticIp() {
    checkCache();
    return ELASTIC_IP;
  }

  public synchronized int getElasticPort() {
    checkCache();
    return ELASTIC_PORT;
  }

  //Session
  public static final Pair<Integer, TimeUnit> SESSION_EXPIRATION_TIME = Pair.with(30, TimeUnit.MINUTES);
  public static final int SESSION_MAX_SIZE = 10000;

  //LIVE DATASET
  public final static int LIVE_DATASET_UPLOAD_STATUS = 0;
  public final static int LIVE_DATASET_DOWNLOAD_STATUS = 1;
  public final static int LIVE_DATASET_BOOTSTRAP_PEERS = 5;
  
  //Util
  public static Date getDateNow() {
    return Calendar.getInstance().getTime();
  }
  
  private static final Random rand = new Random();
  
  public static String getClusterId() {
    return "" + rand.nextInt();
  }
  
  public static String getDatasetPublicId() {
    return "" + rand.nextInt();
  }

  public static String getSessionId() {
    return "" + rand.nextInt();
  }
}
