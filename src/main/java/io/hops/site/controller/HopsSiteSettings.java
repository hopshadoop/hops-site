package io.hops.site.controller;

import io.hops.site.common.Ip;
import io.hops.site.dao.entity.HopsSiteVariables;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.javatuples.Pair;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class HopsSiteSettings {

  private static final Logger logger = Logger.getLogger(HopsSiteSettings.class.getName());

  @PersistenceContext(unitName = "hops-sitePU")
  private EntityManager em;

  private boolean cached = false;

  private void checkCache() {
    if (!cached) {
      populateVariables();
      cached = true;
    }
  }

  public HopsSiteVariables findById(String id) {
    try {
      return em.createNamedQuery("HopsSiteVariables.findById", HopsSiteVariables.class).setParameter("id", id).
        getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  private String setStringVar(String varName, String defaultValue) {
    HopsSiteVariables variable = findById(varName);
    if (variable != null && variable.getValue() != null && !variable.getValue().isEmpty()) {
      String value = variable.getValue();
      return value;
    }
    return defaultValue;
  }
  
  private long setLongVar(String varName, Long defaultValue) {
    HopsSiteVariables var = findById(varName);
    try {
      if (var != null && var.getValue() != null) {
        String val = var.getValue();
        if (val != null && val.isEmpty() == false) {
          return Long.parseLong(val);
        }
      }
    } catch (NumberFormatException ex) {
      logger.info("Error - not a long! " + varName + " should be an integer. Value was " + defaultValue);
    }
    return defaultValue;
  }
  
  private String setIpVar(String varName, String defaultValue) {
    HopsSiteVariables var = findById(varName);
    if (var != null && var.getValue() != null && Ip.validIp(var.getValue())) {
      String val = var.getValue();
      if (val != null && val.isEmpty() == false) {
        return val;
      }
    }
    return defaultValue;
  }

  private Integer setIntVar(String varName, Integer defaultValue) {
    HopsSiteVariables var = findById(varName);
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

  private void populateVariables() {
    populateHopsSiteCache();
    populateElasticCache();
  }
  //**************************************************VARIABLES*********************************************************
  public static final Level DELA_DEBUG = Level.INFO;
  private static final String VARIABLE_DELA_VERSION = "dela_version";
  private static final String VARIABLE_DELA_HEARTBEAT_INTERVAL = "dela_heartbeat_interval";
  private String DELA_VERSION = "0.1";
  private long DELA_HEARTBEAT_INTERVAL = 10*60*1000l; //same as cluster - 10mins default
  private long HEARTBEAT_CHECK_INTERVAL = 5 * DELA_HEARTBEAT_INTERVAL;
  private long DATASET_HEALTH_INTERVAL = 5 * HEARTBEAT_CHECK_INTERVAL;
  private int DATASET_HEALTH_OLDER_THAN = 5 * (int) (DATASET_HEALTH_INTERVAL / 1000);

  private void populateHopsSiteCache() {
    DELA_VERSION = setStringVar(VARIABLE_DELA_VERSION, DELA_VERSION);
    DELA_HEARTBEAT_INTERVAL = setLongVar(VARIABLE_DELA_HEARTBEAT_INTERVAL, DELA_HEARTBEAT_INTERVAL);
    HEARTBEAT_CHECK_INTERVAL = 5 * DELA_HEARTBEAT_INTERVAL;
    DATASET_HEALTH_INTERVAL = 5 * HEARTBEAT_CHECK_INTERVAL;
    DATASET_HEALTH_OLDER_THAN = 5 * (int) (DATASET_HEALTH_INTERVAL / 1000);
  }

  public String getDELA_VERSION() {
    checkCache();
    return DELA_VERSION;
  }

  public void setDELA_VERSION(String DELA_VERSION) {
    checkCache();
    this.DELA_VERSION = DELA_VERSION;
  }
  
  public long getDELA_HEARTBEAT_INTERVAL() {
    checkCache();
    return DELA_HEARTBEAT_INTERVAL;
  }

  public void setDELA_HEARTBEAT_INTERVAL(long DELA_HEARTBEAT_INTERVAL) {
    checkCache();
    this.DELA_HEARTBEAT_INTERVAL = DELA_HEARTBEAT_INTERVAL;
    this.HEARTBEAT_CHECK_INTERVAL = 5 * this.DELA_HEARTBEAT_INTERVAL;
    this.DATASET_HEALTH_INTERVAL = 5 * HEARTBEAT_CHECK_INTERVAL;
    this.DATASET_HEALTH_OLDER_THAN = 5 * (int) (DATASET_HEALTH_INTERVAL / 1000);
  }
  
  public long getHEARTBEAT_CHECK_INTERVAL() {
    checkCache();
    return HEARTBEAT_CHECK_INTERVAL;
  }
  
  public long getDATASET_HEALTH_INTERVAL() {
    checkCache();
    return DATASET_HEALTH_INTERVAL;
  }
  
  public int getDATASET_HEALTH_OLDER_THAN() {
    checkCache();
    return DATASET_HEALTH_OLDER_THAN;
  }
  
  //************************************************Elasticsearch*******************************************************
  public static final String DELA_DOC_INDEX = "hops-site";
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
  //************************************************DATASET STATUS******************************************************
  public static final int DATASET_STATUS_UPLOAD = 0;
  public static final int DATASET_STATUS_DOWNLOAD = 1;
  //*************************************************LIVE DATASET*******************************************************
  public final static int LIVE_DATASET_BOOTSTRAP_PEERS = 5;
  //*****************************************************LOG************************************************************
  public final static Level DEBUG = Level.INFO;
  //****************************************************Session*********************************************************
  public static final Pair<Integer, TimeUnit> SESSION_EXPIRATION_TIME = Pair.with(30, TimeUnit.MINUTES);
  public static final int SESSION_MAX_SIZE = 10000;
  //*****************************************************Util***********************************************************
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
