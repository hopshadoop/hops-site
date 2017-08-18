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
import java.util.Collection;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DatasetDTO {

  private String name;
  private String description;
  private Date publishedOn;
  private String ownerClusterPublicId;
  private int status;
  private Collection<String> categoryCollection;

  public DatasetDTO() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Collection<String> getCategoryCollection() {
    return categoryCollection;
  }

  public void setCategoryCollection(Collection<String> categoryCollection) {
    this.categoryCollection = categoryCollection;
  }

  public Date getPublishedOn() {
    return publishedOn;
  }

  public void setPublishedOn(Date publishedOn) {
    this.publishedOn = publishedOn;
  }

  public String getOwnerClusterPublicId() {
    return ownerClusterPublicId;
  }

  public void setOwnerClusterPublicId(String ownerClusterPublicId) {
    this.ownerClusterPublicId = ownerClusterPublicId;
  }

  @Override
  public String toString() {
    return "DatasetDTO{" + "name=" + name + ", description=" + description + ", publishedOn=" + publishedOn +
      ", ownerClusterPublicId=" + ownerClusterPublicId + ", status=" + status +
      ", categoryCollection=" + categoryCollection + '}';
  }
  
  public static DatasetDTO parse(String datasetJSON) {
     return new Gson().fromJson(datasetJSON, DatasetDTO.class);
   }
}
