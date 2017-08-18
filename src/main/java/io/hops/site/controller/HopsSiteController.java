package io.hops.site.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.hops.site.common.Settings;
import io.hops.site.dao.facade.DatasetHealthFacade;
import io.hops.site.dao.facade.HeartbeatFacade;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class HopsSiteController {

  private static final Logger LOGGGER = Logger.getLogger(HopsSiteController.class.getName());

  @EJB
  private Settings settings;

  public HopsSiteController() {
    this.sessionCache = Caffeine.newBuilder()
      .expireAfterWrite(settings.SESSION_EXPIRATION_TIME.getValue0(), settings.SESSION_EXPIRATION_TIME.getValue1())
      .maximumSize(settings.SESSION_MAX_SIZE)
      .build();
    this.heartbeatsToBeCleaned = settings.getDateNow();
  }

  //***************************************************SESSIONS*********************************************************
  private final Cache<String, Session> sessionCache;

  public void put(String sessionId, Session session) {
    sessionCache.put(sessionId, session);
  }

  public Optional<Session> get(String sessionId) throws ExecutionException {
    return Optional.ofNullable(sessionCache.getIfPresent(sessionId));
  }

  public long active() {
    return sessionCache.estimatedSize();
  }

  public void removeSession(String sessionId) {
    sessionCache.invalidate(sessionId);
  }

  public static interface Session {
  }
  //****************************************************HEARTBEATS******************************************************
  @EJB
  private HeartbeatFacade heartbeatFacade;

  private Date heartbeatsToBeCleaned;

  @Schedule(
    hour = "*/1",
    persistent = false)
  private void heartbeatCheck() {
    heartbeatFacade.deleteHeartbeats(heartbeatsToBeCleaned);
    heartbeatsToBeCleaned = settings.getDateNow();
  }
  //***********************************************DATASET HEALTH*******************************************************
  @EJB
  private DatasetHealthFacade datasetHealthFacade;

  @Schedule(
    hour = "*/6",
    persistent = false)
  private void datasetHealthUpdate() {
    datasetHealthFacade.updateAllDatasetHealth();
  }
}
