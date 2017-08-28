package io.hops.site.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

public class SearchServiceDTO {
  @XmlRootElement
  public static class Params {

    private String searchTerm;

    public Params() {
    }

    public Params(String searchTerm) {
      this.searchTerm = searchTerm;
    }

    public String getSearchTerm() {
      return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
      this.searchTerm = searchTerm;
    }
  }

  @XmlRootElement
  public static class SearchResult {

    private String sessionId;
    private int nrHits;

    public SearchResult() {
    }

    public SearchResult(String sessionId, int nrHits) {
      this.sessionId = sessionId;
      this.nrHits = nrHits;
    }

    public String getSessionId() {
      return sessionId;
    }

    public void setSessionId(String sessionId) {
      this.sessionId = sessionId;
    }

    public int getNrHits() {
      return nrHits;
    }

    public void setNrHits(int nrHits) {
      this.nrHits = nrHits;
    }
  }

  @XmlRootElement
  public static class Item {

    private String publicDSId;
    private DatasetDTO.Complete dataset;
    private float score;
    private List<ClusterAddressDTO> bootstrap;

    public Item() {
    }

    public Item(String publicDSId, DatasetDTO.Complete dataset, float score, List<ClusterAddressDTO> bootstrap) {
      this.publicDSId = publicDSId;
      this.dataset = dataset;
      this.score = score;
      this.bootstrap = bootstrap;
    }

    public String getPublicDSId() {
      return publicDSId;
    }

    public void setPublicDSId(String publicDSId) {
      this.publicDSId = publicDSId;
    }

    public DatasetDTO.Complete getDataset() {
      return dataset;
    }

    public void setDataset(DatasetDTO.Complete dataset) {
      this.dataset = dataset;
    }

    public float getScore() {
      return score;
    }

    public void setScore(float score) {
      this.score = score;
    }

    public List<ClusterAddressDTO> getBootstrap() {
      return bootstrap;
    }

    public void setBootstrap(List<ClusterAddressDTO> bootstrap) {
      this.bootstrap = bootstrap;
    }
  }

  public static class Page {
    private int startItem;
    private int nrItems;

    public Page() {
    }

    public Page(int startItem, int nrItems) {
      this.startItem = startItem;
      this.nrItems = nrItems;
    }

    public int getStartItem() {
      return startItem;
    }

    public void setStartItem(int startItem) {
      this.startItem = startItem;
    }

    public int getNrItems() {
      return nrItems;
    }

    public void setNrItems(int nrItems) {
      this.nrItems = nrItems;
    }
  }

  public static class PageResult {

    private int startItem;
    private List<Item> items;

    public PageResult() {
    }

    public PageResult(int startItem, List<Item> items) {
      this.startItem = startItem;
      this.items = items;
    }

    public int getStartItem() {
      return startItem;
    }

    public void setStartItem(int startItem) {
      this.startItem = startItem;
    }

    public List<Item> getItems() {
      return items;
    }

    public void setItems(List<Item> items) {
      this.items = items;
    }
  }
}
