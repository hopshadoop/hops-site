package site.hops.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-07-18T22:43:53")
@StaticMetamodel(RegisteredCluster.class)
public class RegisteredCluster_ { 

    public static volatile SingularAttribute<RegisteredCluster, String> dateRegistered;
    public static volatile SingularAttribute<RegisteredCluster, Long> heartbeatsMissed;
    public static volatile SingularAttribute<RegisteredCluster, String> cert;
    public static volatile SingularAttribute<RegisteredCluster, String> email;
    public static volatile SingularAttribute<RegisteredCluster, String> dateLastPing;
    public static volatile SingularAttribute<RegisteredCluster, String> gvodEndpoint;
    public static volatile SingularAttribute<RegisteredCluster, String> searchEndpoint;
    public static volatile SingularAttribute<RegisteredCluster, String> clusterId;

}