package io.hops.site.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.hops.site.common.Settings;
import io.hops.site.dao.facade.DatasetHealthFacade;
import io.hops.site.dao.facade.HeartbeatFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
@Startup
@Singleton
public class HopsSiteController {

  private static final Logger LOG = Logger.getLogger(HopsSiteController.class.getName());

  @EJB
  private Settings settings;
  @EJB
  private HopsSiteSettings hsettings;
  @Resource
  TimerService timerService;

  public HopsSiteController() {
    this.sessionCache = Caffeine.newBuilder()
      .expireAfterWrite(settings.SESSION_EXPIRATION_TIME.getValue0(), settings.SESSION_EXPIRATION_TIME.getValue1())
      .maximumSize(settings.SESSION_MAX_SIZE)
      .build();
    this.heartbeatsToBeCleaned = settings.getDateNow();
  }

  @PostConstruct
  private void init() {
    timerService.createTimer(0, hsettings.DATASET_HEALTH_INTERVAL, DATASET_HEALTH_TIMER);
    timerService.createTimer(0, hsettings.HEARTBEAT_CHECK_INTERVAL, HEARTBEAT_CHECK_TIMER);
  }

  @PreDestroy
  private void destroyTimer() {
    for (Timer timer : timerService.getTimers()) {
      timer.cancel();
    }
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
  private static final Serializable HEARTBEAT_CHECK_TIMER = "heartbeat check timer";

  @EJB
  private HeartbeatFacade heartbeatFacade;

  private Date heartbeatsToBeCleaned;

  private void hearbeatCheckTimeout() {
    LOG.log(Settings.DEBUG, "{0}", HEARTBEAT_CHECK_TIMER);
    heartbeatFacade.deleteHeartbeats(heartbeatsToBeCleaned);
    heartbeatsToBeCleaned = settings.getDateNow();
  }
  //***********************************************DATASET HEALTH*******************************************************
  private static final Serializable DATASET_HEALTH_TIMER = "dataset health timer";
  @EJB
  private DatasetHealthFacade datasetHealthFacade;

  private void datasetHealthTimeout() {
    LOG.log(Settings.DEBUG, "{0}", DATASET_HEALTH_TIMER);
    datasetHealthFacade.updateAllDatasetHealth();
  }

  //********************************************************************************************************************
  @Timeout
  public void performTimeout(Timer timer) {
    LOG.log(Settings.DEBUG, "timer {0}", timer.getInfo());
    if (timer.getInfo().equals(DATASET_HEALTH_TIMER)) {
      datasetHealthTimeout();
    } else if (timer.getInfo().equals(HEARTBEAT_CHECK_TIMER)) {
      hearbeatCheckTimeout();
    }
  }
}
