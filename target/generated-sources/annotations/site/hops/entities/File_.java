package site.hops.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import site.hops.entities.DatasetStructure;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-07-18T22:43:53")
@StaticMetamodel(File.class)
public class File_ { 

    public static volatile SingularAttribute<File, Integer> id;
    public static volatile SingularAttribute<File, Boolean> kafkaFile;
    public static volatile SingularAttribute<File, DatasetStructure> datasetName;
    public static volatile SingularAttribute<File, String> name;
    public static volatile SingularAttribute<File, String> avroSchema;

}