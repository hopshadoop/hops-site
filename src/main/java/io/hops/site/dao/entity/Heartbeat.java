package io.hops.site.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
@Entity
@Table(name = "cluster_heartbeat",
  catalog = "hops_site",
  schema = "")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Heartbeat.findOlderThan",
    query = "SELECT h FROM Heartbeat h WHERE h.lastPinged < :date")})
public class Heartbeat implements Serializable {

  @Id
  @Column(name = "cluster_id")
  private int clusterId;
  @Column(name = "date_last_ping")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastPinged;

  public Heartbeat() {
  }

  public Heartbeat(int clusterId, Date dateLastPinged) {
    this.clusterId = clusterId;
    this.lastPinged = dateLastPinged;
  }

  public int getClusterId() {
    return clusterId;
  }

  public void setClusterId(int clusterId) {
    this.clusterId = clusterId;
  }

  public Date getLastPinged() {
    return lastPinged;
  }

  public void setLastPinged(Date lastPinged) {
    this.lastPinged = lastPinged;
  }
}
