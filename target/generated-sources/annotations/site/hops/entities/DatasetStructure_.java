package site.hops.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import site.hops.entities.File;
import site.hops.entities.PopularDataset;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-07-18T22:43:53")
@StaticMetamodel(DatasetStructure.class)
public class DatasetStructure_ { 

    public static volatile SingularAttribute<DatasetStructure, String> datasetName;
    public static volatile SingularAttribute<DatasetStructure, String> datasetId;
    public static volatile SingularAttribute<DatasetStructure, PopularDataset> popularDataset;
    public static volatile CollectionAttribute<DatasetStructure, File> fileCollection;
    public static volatile SingularAttribute<DatasetStructure, String> datasetDescription;

}