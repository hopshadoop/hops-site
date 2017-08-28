package io.hops.site.controller;

import io.hops.site.dao.entity.HopsSiteVariables;
import java.util.logging.Logger;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

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

  //**************************************************VARIABLES*********************************************************
  private static final String VARIABLE_DELA_VERSION = "dela_version";
  private String DELA_VERSION = "0.1"; 
  private void populateVariables() {
    DELA_VERSION = setStringVar(VARIABLE_DELA_VERSION, DELA_VERSION);
  }

  public String getDELA_VERSION() {
    checkCache();
    return DELA_VERSION;
  }
  
  public void setDELA_VERSION(String DELA_VERSION) {
    checkCache();
    this.DELA_VERSION = DELA_VERSION;
  }
}
