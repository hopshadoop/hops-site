package site.hops.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import site.hops.entities.PopularDataset;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-07-26T21:11:56")
@StaticMetamodel(Partner.class)
public class Partner_ { 

    public static volatile SingularAttribute<Partner, Integer> partnerId;
    public static volatile CollectionAttribute<Partner, PopularDataset> popularDatasetCollection;
    public static volatile SingularAttribute<Partner, String> gvodUdpEndpoint;

}