package site.hops.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import site.hops.entities.DatasetStructure;
import site.hops.entities.Partner;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-07-19T12:11:37")
@StaticMetamodel(PopularDataset.class)
public class PopularDataset_ { 

    public static volatile SingularAttribute<PopularDataset, Integer> leeches;
    public static volatile SingularAttribute<PopularDataset, String> datasetId;
    public static volatile CollectionAttribute<PopularDataset, Partner> partnerCollection;
    public static volatile SingularAttribute<PopularDataset, Integer> seeds;
    public static volatile SingularAttribute<PopularDataset, DatasetStructure> datasetStructure;

}