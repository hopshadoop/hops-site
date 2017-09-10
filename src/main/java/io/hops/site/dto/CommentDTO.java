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

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

public class CommentDTO {

  @XmlRootElement
  public static class Publish {

    private String content;
    private String userEmail;

    public Publish() {
    }

    public Publish(String content, String userEmail) {
      this.content = content;
      this.userEmail = userEmail;
    }
    
    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public String getUserEmail() {
      return userEmail;
    }

    public void setUserEmail(String userEmail) {
      this.userEmail = userEmail;
    }
  }
  
    public static class RetrieveComment {

    private Integer id;
    private String content;
    private UserDTO user;
    private Date datePublished;

    public RetrieveComment() {
    }

    public RetrieveComment(Integer id, String content, UserDTO user, Date datePublished) {
      this.id = id;
      this.content = content;
      this.user = user;
      this.datePublished = datePublished;
    }

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public UserDTO getUser() {
      return user;
    }

    public void setUser(UserDTO user) {
      this.user = user;
    }

    public Date getDatePublished() {
      return datePublished;
    }

    public void setDatePublished(Date datePublished) {
      this.datePublished = datePublished;
    }
  }
}
