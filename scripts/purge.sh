#!/bin/bash

. hops-site-env.sh

sudo -u $MYSQL_USER bash <<'EOF'
. hops-site-env.sh
cd ${MYSQL_DIR}
./mysql-client.sh -e "DROP DATABASE IF EXISTS hops_site"
EOF

sudo -u $GLASSFISH_USER bash <<'EOF'
. hops-site-env.sh
cd ${GLASSFISH_PATH}/bin
./asadmin $ASADMIN_PW stop-domain ${DOMAIN}
./asadmin $ASADMIN_PW delete-domain ${DOMAIN}
rm ${DOMAINPW_FILE}
EOF

curl -XDELETE '10.0.2.15:9200/hops-site?pretty'

sudo truncate -s 0 ${CERTS_DIR}/index.txt
sudo rm -rf /srv/hops/hops-site
sudo rm ${CERTS_DIR}/hops-site-certs/ca_pub.pem
sudo rm ${CERTS_DIR}/hops-site-certs/priv.key
sudo rm ${CERTS_DIR}/hops-site-certs/pub.pem
sudo rm ${CERTS_DIR}/hops-site-certs/keystores/*

