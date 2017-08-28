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
package io.hops.site.rest.request.filter;

import io.hops.site.old_dto.UserDTO;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GenericReqUserDatasetDTO {

  private UserDTO user;
  private DatasetDTO dataset;

  public GenericReqUserDatasetDTO() {
  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }

  public DatasetDTO getDataset() {
    return dataset;
  }

  public void setDataset(DatasetDTO dataset) {
    this.dataset = dataset;
  }

  @Override
  public String toString() {
    return "GenericRequestDTO{" + "user=" + user + ", dataset=" + dataset + '}';
  }

  public static class DatasetDTO {
  }
}
