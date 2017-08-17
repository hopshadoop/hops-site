package io.hops.site.dto;

import java.util.List;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class SearchDTO {

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

  public static class BaseResult {

    private String sessionId;
    private int nrHits;

    public BaseResult() {
    }

    public BaseResult(String sessionId, int nrHits) {
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

  public static class Item {

    private String datasetId;
    private DatasetDTO dataset;
    private float score;
    private List<AddressJSON> peers;

    public Item() {
    }

    public Item(String datasetId, DatasetDTO dataset, float score, List<AddressJSON> peers) {
      this.datasetId = datasetId;
      this.dataset = dataset;
      this.score = score;
      this.peers = peers;
    }

    public String getDatasetId() {
      return datasetId;
    }

    public void setDatasetId(String datasetId) {
      this.datasetId = datasetId;
    }

    
    public DatasetDTO getDataset() {
      return dataset;
    }

    public void setDataset(DatasetDTO dataset) {
      this.dataset = dataset;
    }

    public float getScore() {
      return score;
    }

    public void setScore(float score) {
      this.score = score;
    }

    public List<AddressJSON> getPeers() {
      return peers;
    }

    public void setPeers(List<AddressJSON> peers) {
      this.peers = peers;
    }
  }

  public static class Page {

    private int startItem;
    private List<Item> items;

    public Page() {
    }

    public Page(int startItem, List<Item> items) {
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
