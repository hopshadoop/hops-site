#!/bin/bash
set -e

INSTALL_PATH='/srv/hops'
GLASSFISH_PATH='/srv/hops/glassfish/versions/glassfish-4.1.2.172'
DOMAIN_DIR='/srv/hops/domains'
DOMAIN='domain2'
DOMAINPW_FILE="${DOMAIN_DIR}/master-password"
CERTS_DIR='/srv/hops/certs-dir'
MYSQL_SERVER='10.0.2.15'
MYSQL_PORT='3306'
DB_NAME='hops_site'
MSQL_DIR='/srv/hops/mysql-cluster/ndb/scripts/'
ASASMDIN_PW="--user adminuser --passwordfile ${DOMAINPW_FILE}"

sudo su -c '/srv/hops/mysql-cluster/ndb/scripts/mysql-client.sh -e "DROP DATABASE IF EXISTS hops_site"' mysql

cd ${GLASSFISH_PATH}/bin
./asadmin $ASASMDIN_PW stop-domain ${DOMAIN}
./asadmin $ASASMDIN_PW delete-domain ${DOMAIN}


rm ${DOMAINPW_FILE}

sudo truncate -s 0 ${CERTS_DIR}/index.txt