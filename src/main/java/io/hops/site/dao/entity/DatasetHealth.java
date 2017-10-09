package io.hops.site.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
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
    query = "SELECT dh FROM DatasetHealth dh WHERE dh.id.datasetId = :datasetId"),
  @NamedQuery(name = "DatasetHealth.findByDatasetIdList",
    query = "SELECT dh FROM DatasetHealth dh WHERE dh.id.datasetId IN :datasetIdList"), 
  @NamedQuery(name = "DatasetHealth.deleteOlderThan",
    query = "DELETE FROM DatasetHealth dh WHERE dh.updated < :timestamp")})
public class DatasetHealth implements Serializable {

  @EmbeddedId
  private PK id;

  @Column(name = "count")
  private Integer count;

  @Column(name = "updated")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updated;

  public DatasetHealth() {
  }

  public PK getId() {
    return id;
  }

  public void setId(PK id) {
    this.id = id;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  @Embeddable
  public static class PK implements Serializable {

    @Column(name = "dataset_id")
    @NotNull
    private int datasetId;
    @Column(name = "status")
    @NotNull
    private int status;

    public PK() {
    }

    public PK(int datasetId, int clusterId) {
      this.datasetId = datasetId;
      this.status = status;
    }

    public int getDatasetId() {
      return datasetId;
    }

    public void setDatasetId(int datasetId) {
      this.datasetId = datasetId;
    }

    public int getStatus() {
      return status;
    }

    public void setStatus(int status) {
      this.status = status;
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 67 * hash + this.datasetId;
      hash = 67 * hash + this.status;
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final PK other = (PK) obj;
      if (this.datasetId != other.datasetId) {
        return false;
      }
      if (this.status != other.status) {
        return false;
      }
      return true;
    }
  }
}
