# hops-site
Bootstrap server for HopsWorks


Required database -> hops_site

Required tables -> create table registered_cluster(cluster_id varchar(200) not null, search_endpoint varchar(100) not null, email varchar(100) not null, cert varchar(1000) not null, gvod_endpoint varchar(100) not null, heartbeats_missed bigint not null, date_registered varchar(100) not null, date_last_ping varchar(100) not null, PRIMARY KEY(cluster_id));

and 


create table popular_dataset(dataset_id varchar(300) not null, manifest longtext not null, partners longtext not null, leeches int not null, seeds int not null, PRIMARY KEY(dataset_id)); 


# Bash script for installation

    !#/bin/bash
    /srv/hops/mysql-cluster/ndb/scripts/mysql-client.sh -e "create database hops_site"

    /srv/hops/mysql-cluster/ndb/scripts/mysql-client.sh -e "grant all privileges on hops_site.* to 'alex'@'localhost' identfied by 'hops'"

    /srv/hops/mysql-cluster/ndb/scripts/mysql-client.sh hops_site -e "create table registered_cluster(cluster_id varchar(200) not null, search_endpoint varchar(100) not null, email varchar(100) not null, cert varchar(1000) not null, gvod_endpoint varchar(100) not null, heartbeats_missed bigint not null, date_registered varchar(100) not null, date_last_ping varchar(100) not null, PRIMARY KEY(cluster_id))"

    /srv/hops/mysql-cluster/ndb/scripts/mysql-client.sh hops_site -e "create table popular_dataset(dataset_id varchar(300) not null, manifest longtext not null, partners longtext not null, leeches int not null, seeds int not null, PRIMARY KEY(dataset_id))"

