# hops-site
Bootstrap server for HopsWorks


Required database

     CREATE DATABASE hops_site;

Required tables -> 

    CREATE TABLE registered_cluster(cluster_id VARCHAR(200) NOT NULL, \
    search_endpoint VARCHAR(100) NOT NULL, email VARCHAR(100) NOT NULL, \
    cert VARCHAR(1000) NOT NULL, gvod_endpoint VARCHAR(100) NOT NULL, \
    heartbeats_missed BIGINT NOT NULL, date_registered VARCHAR(100) NOT NULL, \
    date_last_ping VARCHAR(100) NOT NULL, PRIMARY KEY(cluster_id));

and 


    CREATE TABLE popular_dataset(dataset_id VARCHAR(300) NOT NULL, \
    manifest LONGTEXT NOT NULL, partners LONGTEXT NOT NULL, \
    leeches INT NOT NULL, seeds INT NOT NULL, PRIMARY KEY(dataset_id)); 


# Bash script for installation

    !#/bin/bash
    /srv/hops/mysql-cluster/ndb/scripts/mysql-client.sh -e "create database hops_site"

    /srv/hops/mysql-cluster/ndb/scripts/mysql-client.sh -e "grant all privileges on hops_site.* to 'alex'@'localhost' identfied by 'hops'"

    /srv/hops/mysql-cluster/ndb/scripts/mysql-client.sh hops_site < /hops-site/sql/tables.sql
    
    rm -f dela-hops.war
    /srv/hops/glassfish/versions/current/bin/asadmin --host localhost --port 4848 --user adminuser --passwordfile /srv/hops/domains/domain1_admin_passwd --interactive=false undeploy --target server dela-hops

    wget http://snurran.sics.se/hops/hopsworks/0.1.0/dela-hops.war

    /srv/hops/glassfish/versions/current/bin/asadmin --host localhost --port 4848 --user adminuser --passwordfile /srv/hops/domains/domain1_admin_passwd --interactive=false --echo=true --terse=false deploy --name dela-hops --force=true --precompilejsp=true --verify=false --enabled=true --generatermistubs=false --availabilityenabled=false --asyncreplication=false --target server --keepreposdir=false --keepfailedstubs=false --isredeploy=false --logreportederrors=true --keepstate=false --lbenabled true --_classicstyle=false --upload=true dela-hops.war



