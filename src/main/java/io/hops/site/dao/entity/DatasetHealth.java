package io.hops.site.dao.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
@Entity
@Table(name = "dataset_health",
  catalog = "hops_site",
  schema = "")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "DatasetHealth.findByDatasetId",
    query = "SELECT dh FROM DatasetHealth dh WHERE dh.datasetId = :datasetIdList"),
  @NamedQuery(name = "DatasetHealth.findByDatasetIdList",
    query = "SELECT dh FROM DatasetHealth dh WHERE dh.datasetId IN :datasetIdList")})
public class DatasetHealth implements Serializable {

  @Id
  @Column(name = "dataset_id")
  private int datasetId;

  @Column(name = "leeches")
  private int leeches;

  @Column(name = "seeds")
  private int seeds;

  public DatasetHealth() {
  }

  public DatasetHealth(int datasetId, int leeches, int seeds) {
    this.datasetId = this.datasetId;
    this.leeches = leeches;
    this.seeds = seeds;
  }

  public int getDatasetId() {
    return datasetId;
  }

  public void setDatasetId(int datasetId) {
    this.datasetId = datasetId;
  }

  public int getLeeches() {
    return leeches;
  }

  public void setLeeches(int leeches) {
    this.leeches = leeches;
  }

  public int getSeeds() {
    return seeds;
  }

  public void setSeeds(int seeds) {
    this.seeds = seeds;
  }
}
