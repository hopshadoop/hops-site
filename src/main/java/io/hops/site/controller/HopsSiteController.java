package io.hops.site.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.hops.site.dao.entity.DatasetHealth;
import io.hops.site.dao.entity.Heartbeat;
import io.hops.site.dao.entity.LiveDataset;
import io.hops.site.dao.facade.DatasetHealthFacade;
import io.hops.site.dao.facade.HeartbeatFacade;
import io.hops.site.dao.facade.LiveDatasetFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
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
  private HopsSiteSettings settings;
  @Resource
  TimerService timerService;

  @EJB
  private HeartbeatFacade heartbeatFacade;
  @EJB
  private LiveDatasetFacade liveDatasetFacade;
  @EJB
  private DatasetHealthFacade datasetHealthFacade;

  public HopsSiteController() {
    this.sessionCache = Caffeine.newBuilder()
      .expireAfterWrite(settings.SESSION_EXPIRATION_TIME.getValue0(), settings.SESSION_EXPIRATION_TIME.getValue1())
      .maximumSize(settings.SESSION_MAX_SIZE)
      .build();
    this.heartbeatsToBeCleaned = settings.getDateNow();
  }

  @PostConstruct
  private void init() {
    timerService.createTimer(0, settings.getDATASET_HEALTH_INTERVAL(), DATASET_HEALTH_TIMER);
    timerService.createTimer(0, settings.getHEARTBEAT_CHECK_INTERVAL(), HEARTBEAT_CHECK_TIMER);
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

  private Date heartbeatsToBeCleaned;

  private void hearbeatCheckTimeout() {
    LOG.log(HopsSiteSettings.DEBUG, "{0}", HEARTBEAT_CHECK_TIMER);
    List<Heartbeat> oldHeartbeats = heartbeatFacade.findHeartbeats(heartbeatsToBeCleaned);
    for (Heartbeat h : oldHeartbeats) {
      List<LiveDataset> datasets = liveDatasetFacade.liveDatasets(h.getClusterId());
      for (LiveDataset d : datasets) {
        updateDatasetHealth(d.getId().getDatasetId());
      }
      heartbeatFacade.remove(h);
    }
    heartbeatsToBeCleaned = settings.getDateNow();
  }

  private void updateDatasetHealth(int datasetId) {
    List<DatasetHealth> datasetHealth = datasetHealthFacade.findByDatasetId(datasetId);
    if (datasetHealth.isEmpty()) {
      return;
    }
    if (datasetHealth.size() != 2) {
      LOG.log(Level.SEVERE, "something is wrong with dataset health maintenance");
      return;
    }
    for (DatasetHealth dh : datasetHealth) {
      dh.decCount();
      datasetHealthFacade.edit(dh);
    }
  }
  //***********************************************DATASET HEALTH*******************************************************
  private static final Serializable DATASET_HEALTH_TIMER = "dataset health timer";

  private void datasetHealthTimeout() {
    LOG.log(HopsSiteSettings.DEBUG, "{0}", DATASET_HEALTH_TIMER);
//    datasetHealthFacade.updateAllDatasetHealth();
  }

  //********************************************************************************************************************
  @Timeout
  public void performTimeout(Timer timer) {
    LOG.log(HopsSiteSettings.DEBUG, "timer {0}", timer.getInfo());
    if (timer.getInfo().equals(DATASET_HEALTH_TIMER)) {
      datasetHealthTimeout();
    } else if (timer.getInfo().equals(HEARTBEAT_CHECK_TIMER)) {
      hearbeatCheckTimeout();
    }
  }
}
