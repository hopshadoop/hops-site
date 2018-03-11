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

import com.google.gson.Gson;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
@XmlRootElement
public class DownloadReportDTO {
  private List<String> values = new LinkedList<>();
  
  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }
  
  public static DownloadReportDTO fromJson(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, DownloadReportDTO.class);
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Iterator<String> it = values.iterator();
    if(it.hasNext()) {
      sb.append(it.next());
    }
    while(it.hasNext()) {
      sb.append(",").append(it.next());
    }
    return sb.toString();
  }
}

