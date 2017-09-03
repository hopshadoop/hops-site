package io.hops.site.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

public class DatasetDTO {
  @XmlRootElement
  public static class Proto implements Serializable {
    private String name;
    private String description;
    private Collection<String> categories;
    private long size;

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

    public Collection<String> getCategories() {
      return categories;
    }

    public void setCategories(Collection<String> categories) {
      this.categories = categories;
    }

    public long getSize() {
      return size;
    }

    public void setSize(long size) {
      this.size = size;
    }
  }
  
  @XmlRootElement
  public static class Search implements Serializable {
    private String name;
    private String description;

    public Search() {
    }

    public Search(String name, String description) {
      this.name = name;
      this.description = description;
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
  }
  
  @XmlRootElement
  public static class Details implements Serializable {
    private Owner owner;
    private Collection<String> categories;
    private Date publishedOn;
    private long size;

    public Details() {
    }

    public Details(Owner owner, Collection<String> categories, Date publishedOn, long size) {
      this.owner = owner;
      this.categories = categories;
      this.publishedOn = publishedOn;
      this.size = size;
    }

    public Owner getOwner() {
      return owner;
    }

    public void setOwner(Owner owner) {
      this.owner = owner;
    }

    public Collection<String> getCategories() {
      return categories;
    }

    public void setCategories(Collection<String> categories) {
      this.categories = categories;
    }

    public Date getPublishedOn() {
      return publishedOn;
    }

    public void setPublishedOn(Date publishedOn) {
      this.publishedOn = publishedOn;
    }

    public long getSize() {
      return size;
    }

    public void setSize(long size) {
      this.size = size;
    }
  }
  
  @XmlRootElement
  public static class Complete implements Serializable {
    private Owner owner;
    private String name;
    private String description;
    private Collection<String> categories;
    private Date publishedOn;
    private int rating;
    private long size;

    public Complete() {
    }

    public Complete(Owner owner, String name, String description, Collection<String> categories, Date publishedOn,
      int rating, long size) {
      this.owner = owner;
      this.name = name;
      this.description = description;
      this.categories = categories;
      this.publishedOn = publishedOn;
      this.rating = rating;
      this.size = size;
    }

    public Owner getOwner() {
      return owner;
    }

    public void setOwner(Owner owner) {
      this.owner = owner;
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

    public Collection<String> getCategories() {
      return categories;
    }

    public void setCategories(Collection<String> categories) {
      this.categories = categories;
    }

    public Date getPublishedOn() {
      return publishedOn;
    }

    public void setPublishedOn(Date publishedOn) {
      this.publishedOn = publishedOn;
    }

    public int getRating() {
      return rating;
    }

    public void setRating(int rating) {
      this.rating = rating;
    }

    public long getSize() {
      return size;
    }

    public void setSize(long size) {
      this.size = size;
    }
  }
  
  @XmlRootElement
  public static class Owner{
    private String clusterDescription;
    private String userDescription;

    public Owner() {
    }

    public Owner(String clusterDescription, String userDescription) {
      this.clusterDescription = clusterDescription;
      this.userDescription = userDescription;
    }

    public String getClusterDescription() {
      return clusterDescription;
    }

    public void setClusterDescription(String clusterDescription) {
      this.clusterDescription = clusterDescription;
    }

    public String getUserDescription() {
      return userDescription;
    }

    public void setUserDescription(String userDescription) {
      this.userDescription = userDescription;
    }
  }
}
