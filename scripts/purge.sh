#!/bin/bash
set -e

sudo su -c '/srv/hops/mysql-cluster/ndb/scripts/mysql-client.sh -e "DROP DATABASE IF EXISTS hops_site"' mysql
sudo -u $DESIRED_USERNAME bash <<'EOF'
. hops-site-env.sh

cd ${GLASSFISH_PATH}/bin
./asadmin $ASASMDIN_PW stop-domain ${DOMAIN}
./asadmin $ASASMDIN_PW delete-domain ${DOMAIN}

rm ${DOMAINPW_FILE}
EOF

sudo truncate -s 0 ${CERTS_DIR}/index.txt
sudo rm -rf /srv/hops/hops-site
