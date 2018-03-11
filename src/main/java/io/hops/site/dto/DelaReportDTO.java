/*
 * Copyright 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hops.site.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
@XmlRootElement
public class DelaReportDTO {

  private String delaId;
  private String torrentId;
  private long reportId;
  private String reportVal;

  public DelaReportDTO() {
  }

  public String getDelaId() {
    return delaId;
  }

  public void setDelaId(String delaId) {
    this.delaId = delaId;
  }

  public String getTorrentId() {
    return torrentId;
  }

  public void setTorrentId(String torrentId) {
    this.torrentId = torrentId;
  }

  public long getReportId() {
    return reportId;
  }

  public void setReportId(long reportId) {
    this.reportId = reportId;
  }

  public String getReportVal() {
    return reportVal;
  }

  public void setReportVal(String data) {
    this.reportVal = data;
  }

  @Override
  public String toString() {
    return "DelaReportDTO{" + "delaId=" + delaId + ", torrentId=" + torrentId + ", reportId=" + reportId + ", reportVal=" +
      reportVal + '}';
  }
}
