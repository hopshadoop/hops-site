package site.hops.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-03-27T17:48:11")
@StaticMetamodel(Registeredclusters.class)
public class Registeredclusters_ { 

    public static volatile SingularAttribute<Registeredclusters, String> dateregistered;
    public static volatile SingularAttribute<Registeredclusters, String> cert;
    public static volatile SingularAttribute<Registeredclusters, String> email;
    public static volatile SingularAttribute<Registeredclusters, String> name;
    public static volatile SingularAttribute<Registeredclusters, String> restendpoint;
    public static volatile SingularAttribute<Registeredclusters, String> udpendpoint;
    public static volatile SingularAttribute<Registeredclusters, Long> heartbeatsmissed;

}