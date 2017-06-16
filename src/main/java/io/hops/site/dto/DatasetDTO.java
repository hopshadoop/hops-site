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

import java.util.Collection;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DatasetDTO {
  
  private Integer id;
  private String publicId;
  private String name;
  private String description;
  private Date madePublicOn;
  private String owner;
  private String readme;
  private int status;
  private Collection<String> categoryCollection;
  private String clusterId;

  public DatasetDTO() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getPublicId() {
    return publicId;
  }

  public void setPublicId(String publicId) {
    this.publicId = publicId;
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

  public Date getMadePublicOn() {
    return madePublicOn;
  }

  public void setMadePublicOn(Date madePublicOn) {
    this.madePublicOn = madePublicOn;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getReadme() {
    return readme;
  }

  public void setReadme(String readme) {
    this.readme = readme;
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

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }
  
  
}
