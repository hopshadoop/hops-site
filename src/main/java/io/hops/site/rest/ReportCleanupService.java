package io.hops.site.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;

@Singleton
public class ReportCleanupService {

  private final static Logger LOG = Logger.getLogger(ReportCleanupService.class.getName());
// No cleanup yet
//  @Schedule(hour = "3")
  public void cleanup() {
    Date yesterday = yesterday();
    LOG.log(Level.INFO, "cleanup:{0}", yesterday);
    getJunkDirs(yesterday).stream().forEach((dir) -> {
      deleteReportDir(dir);
    });
  }

  private Set<File> getJunkDirs(Date date) {
    String path = ReportService.getCleanupPath(date);
    File interval = new File(path);
    Set<File> cleanup = new HashSet<>();
    if (interval.exists()) {
      String[] nodes = interval.list();
      if (nodes != null) {
        for (String nodeS : nodes) {
          File node = new File(interval, nodeS);
          String[] torrents = node.list();
          if (torrents != null) {
            for (String torrentS : torrents) {
              File torrent = new File(node, torrentS);
              String[] instants = torrent.list();
              if (instants != null) {
                for (String instantS : instants) {
                  File instant = new File(torrent, instantS);
                  if (isJunkDir(instant)) {
                    LOG.log(Level.FINE, "cleanup:{0}", instant.getPath());
                    cleanup.add(instant);
                  }
                }
              }
            }
          }
        }
      }
    }
    return cleanup;
  }

  private boolean isJunkDir(File dir) {
    try (BufferedReader br = new BufferedReader(new FileReader(new File(dir, "transfer.csv")))) {
      boolean cleanupDir = br.readLine().startsWith("time:-") 
        || br.readLine().startsWith("time:1")
        || br.readLine().startsWith("time:2")
        || br.readLine().startsWith("time:3");
      return cleanupDir;
    } catch (IOException ex) {
      return false;
    }
  }

  private Date yesterday() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1);
    return cal.getTime();
  }

  private void deleteReportDir(File dir) {
    if (dir.exists()) {
      File data = new File(dir, "data.csv");
      data.delete();
      File download = new File(dir, "download.csv");
      download.delete();
      File transfer = new File(dir, "transfer.csv");
      transfer.delete();
    }
    File torrent = dir.getParentFile();
    dir.delete();
    //delete parents if they are empty
    if(torrent.list() != null && torrent.list().length == 0) {
      File node = torrent.getParentFile();
      torrent.delete();
      if(node.list() != null && node.list().length == 0) {
        node.delete();
      }
    }
  }
}
