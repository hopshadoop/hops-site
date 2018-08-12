package io.hops.site.rest;

import io.hops.site.controller.HopsSiteSettings;
import io.hops.site.dto.DelaReportDTO;
import io.hops.site.rest.annotation.NoCache;
import io.swagger.annotations.Api;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("public/report")
@Stateless
@Api(value = "public/cluster",
  description = "Cluster Register And Ping service")
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ReportService {

  private final static Logger LOG = Logger.getLogger(ReportService.class.getName());

  @POST
  @NoCache
  @Path("transfer")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response transferReport(DelaReportDTO report) {
    LOG.log(Level.FINE, "transfer:{0}", report);
    String reportPath = getReportPath(report.getDelaId(), report.getTorrentId(), new Date(report.getReportId()));
    writeToFile(reportPath, "transfer.csv", report.getReportVal());
    return Response.ok("ok").build();
  }

  @POST
  @NoCache
  @Path("data")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response dataReport(DelaReportDTO report) {
    LOG.log(Level.FINE, "data:{0}", report);
    String reportPath = getReportPath(report.getDelaId(), report.getTorrentId(), new Date(report.getReportId()));
    writeToFile(reportPath, "data.csv", report.getReportVal());
    return Response.ok("ok").build();
  }

  @POST
  @NoCache
  @Path("download")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response downloadReport(DelaReportDTO report) {
    LOG.log(Level.FINE, "download:{0}", report);
    String reportPath = getReportPath(report.getDelaId(), report.getTorrentId(), new Date(report.getReportId()));
    writeToFile(reportPath, "download.csv", report.getReportVal());
    return Response.ok("ok").build();
  }
  public static String getCleanupPath(Date reportId) {
    String path = HopsSiteSettings.getReportDir()
      + File.separator + reportIdInterval(reportId);
    return path;
  }

  private String getReportPath(String nodeId, String torrentId, Date reportId) {
    String path = HopsSiteSettings.getReportDir()
      + File.separator + reportIdInterval(reportId)
      + File.separator + "node_" + nodeId
      + File.separator + "torrent_" + torrentId
      + File.separator + "report_" + reportIdInstant(reportId);
    return path;
  }

  private void writeToFile(String dirPath, String fileName, String reportVal) {
    File dir = new File(dirPath);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File file = new File(dir, fileName);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException ex) {
        throw new RuntimeException("file report error");
      }
    }
    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)))) {
      writer.write(reportVal);
      writer.write("\n");
    } catch (IOException ex) {
      throw new RuntimeException("file report error");
    }
  }

  private static String reportIdInterval(Date reportId) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
    return dateFormat.format(reportId);
  }

  private static String reportIdInstant(Date reportId) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH_mm_ss_SSS");
    return dateFormat.format(reportId);
  }
}
