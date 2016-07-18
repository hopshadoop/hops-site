/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@Entity
@Table(name = "file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "File.findAll", query = "SELECT f FROM File f"),
    @NamedQuery(name = "File.findByName", query = "SELECT f FROM File f WHERE f.name = :name"),
    @NamedQuery(name = "File.findByKafkaFile", query = "SELECT f FROM File f WHERE f.kafkaFile = :kafkaFile"),
    @NamedQuery(name = "File.findByAvroSchema", query = "SELECT f FROM File f WHERE f.avroSchema = :avroSchema"),
    @NamedQuery(name = "File.findById", query = "SELECT f FROM File f WHERE f.id = :id")})
public class File implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kafka_file")
    private boolean kafkaFile;
    @Size(max = 3000)
    @Column(name = "avro_schema")
    private String avroSchema;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "dataset_name", referencedColumnName = "dataset_id")
    @ManyToOne(optional = false)
    private DatasetStructure datasetName;

    public File() {
    }

    public File(Integer id) {
        this.id = id;
    }

    public File(Integer id, String name, boolean kafkaFile) {
        this.id = id;
        this.name = name;
        this.kafkaFile = kafkaFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getKafkaFile() {
        return kafkaFile;
    }

    public void setKafkaFile(boolean kafkaFile) {
        this.kafkaFile = kafkaFile;
    }

    public String getAvroSchema() {
        return avroSchema;
    }

    public void setAvroSchema(String avroSchema) {
        this.avroSchema = avroSchema;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DatasetStructure getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(DatasetStructure datasetName) {
        this.datasetName = datasetName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof File)) {
            return false;
        }
        File other = (File) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "site.hops.entities.File[ id=" + id + " ]";
    }
    
}
