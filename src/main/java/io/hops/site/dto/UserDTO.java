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

import io.hops.site.dao.entity.Users;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDTO {

  public static class Publish {

    private String firstname;
    private String lastname;
    private String email;

    public Publish() {
    }

    public Publish(Users user) {
      this.firstname = user.getFirstname();
      this.lastname = user.getLastname();
      this.email = user.getEmail();
    }

    public String getFirstname() {
      return firstname;
    }

    public void setFirstname(String firstname) {
      this.firstname = firstname;
    }

    public String getLastname() {
      return lastname;
    }

    public void setLastname(String lastname) {
      this.lastname = lastname;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }
  }

  @XmlRootElement
  public static class Retrieve {

    private Integer userId;
    private String firstname;
    private String lastname;
    private String email;
    private String organization;

    public Retrieve() {
    }

    public Retrieve(Users user) {
      this.userId = user.getId();
      this.firstname = user.getFirstname();
      this.lastname = user.getLastname();
      this.email = user.getEmail();
      this.organization = user.getCluster().getOrgName();
    }
    
    public Integer getUserId() {
      return userId;
    }

    public void setUserId(Integer userId) {
      this.userId = userId;
    }

    public String getFirstname() {
      return firstname;
    }

    public void setFirstname(String firstname) {
      this.firstname = firstname;
    }

    public String getLastname() {
      return lastname;
    }

    public void setLastname(String lastname) {
      this.lastname = lastname;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getOrganization() {
      return organization;
    }

    public void setOrganization(String organization) {
      this.organization = organization;
    }
  }
}
