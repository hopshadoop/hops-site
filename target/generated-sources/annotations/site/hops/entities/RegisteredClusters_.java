package site.hops.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-06-29T13:12:59")
@StaticMetamodel(RegisteredClusters.class)
public class RegisteredClusters_ { 

    public static volatile SingularAttribute<RegisteredClusters, String> gvodEndpoint;
    public static volatile SingularAttribute<RegisteredClusters, String> searchEndpoint;
    public static volatile SingularAttribute<RegisteredClusters, Long> heartbeatsMissed;
    public static volatile SingularAttribute<RegisteredClusters, String> dateLastPing;
    public static volatile SingularAttribute<RegisteredClusters, String> cert;
    public static volatile SingularAttribute<RegisteredClusters, String> clusterId;
    public static volatile SingularAttribute<RegisteredClusters, String> email;
    public static volatile SingularAttribute<RegisteredClusters, String> dateRegistered;

}