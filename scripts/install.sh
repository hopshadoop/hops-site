#!/bin/bash
set -e

. hops-site-env.sh

cd ${INSTALL_PATH}
sudo mkdir $HOPS_SITE_BASE
cd $HOPS_SITE_BASE
sudo wget ${HOPS_SITE_DOWNLOAD_URL}${HOPS_SITE_TAR} && sudo tar xvzf ${HOPS_SITE_TAR} && sudo rm ${HOPS_SITE_TAR}
sudo chown -R glassfish:glassfish $HOPS_SITE_BASE

sudo chown mysql:mysql ${HOPS_SITE_TABLES}
sudo chown mysql:mysql ${HOPS_SITE_ROWS}
sudo su -c '/srv/hops/mysql-cluster/ndb/scripts/mysql-client.sh -e "CREATE DATABASE IF NOT EXISTS hops_site"' mysql
sudo su -c "/srv/hops/mysql-cluster/ndb/scripts/mysql-client.sh hops_site < ${HOPS_SITE_TABLES}" mysql
sudo su -c "/srv/hops/mysql-cluster/ndb/scripts/mysql-client.sh hops_site < ${HOPS_SITE_ROWS}" mysql

sudo -u $DESIRED_USERNAME sh -s "${HOPS_SITE_BASE}/scripts" <<'EOF'
. $1/hops-site-env.sh

cd ${GLASSFISH_PATH}/bin
echo "AS_ADMIN_PASSWORD=${KEYSTOREPW}\nAS_ADMIN_MASTERPASSWORD=${KEYSTOREPW}" > $DOMAINPW_FILE
./asadmin $ASASMDIN_PW create-domain --portbase ${DOMAIN_BASE_PORT} ${DOMAIN}
./asadmin $ASASMDIN_PW start-domain ${DOMAIN}

cp ${DOMAIN_DIR}/domain1/lib/${MSQL_CONNECTOR} ${DOMAIN_DIR}/${DOMAIN}/lib

./asadmin --port $ADMIN_PORT $ASASMDIN_PW  create-jdbc-connection-pool --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlDataSource \
                                                                       --restype javax.sql.DataSource \
                                                                       --property user=kthfs:password=kthfs:DatabaseName=${DB_NAME}:ServerName=${MYSQL_SERVER}:port=${MYSQL_PORT} \
                                                                       hopsSitePool
./asadmin --port $ADMIN_PORT $ASASMDIN_PW create-jdbc-resource --connectionpoolid hopsSitePool jdbc/hopssite

./asadmin --port $ADMIN_PORT $ASASMDIN_PW delete-auth-realm certificate
./asadmin --port $ADMIN_PORT $ASASMDIN_PW create-auth-realm --classname com.sun.enterprise.security.auth.realm.certificate.CertificateRealm \
                                                            --property assign-groups=clusters certificate


cd ${DOMAIN_DIR}/${DOMAIN}/config
rm cacerts.jks
rm keystore.jks
keytool -import -noprompt -trustcacerts -alias HopsRootCA -file ${CERTS_DIR}/certs/ca.cert.pem -keystore cacerts.jks $KEYSTORE_PASSWORD
keytool -import -noprompt -trustcacerts -alias HopsRootCA -file ${CERTS_DIR}/certs/ca.cert.pem -keystore keystore.jks $KEYSTORE_PASSWORD
chmod 600 cacerts.jks
chmod 600 keystore.jks

keytool -genkey -alias ${ADMIN_CERT_ALIAS} -keyalg RSA -keysize 1024 -keystore keystore.jks -dname "CN=hops.site-admin, O=SICS, L=Stockholm, ST=Sweden, C=SE" $KEY_PASSWORD
keytool -certreq -alias ${ADMIN_CERT_ALIAS} -keyalg RSA -file hops.site-admin.req -keystore keystore.jks $KEY_PASSWORD

keytool -genkey -alias hops.site-instance -keyalg RSA -keysize 1024 -keystore keystore.jks -dname "CN=hops.site-instance, O=SICS, L=Stockholm, ST=Sweden, C=SE" $KEY_PASSWORD
keytool -certreq -alias hops.site-instance -keyalg RSA -file hops.site-instance.req -keystore keystore.jks $KEY_PASSWORD

cp ${OPENSSL_CONF} "${CERTS_DIR}/openssl-ca.cnf"

EOF

sudo openssl ca -batch -in ${DOMAIN_DIR}/${DOMAIN}/config/hops.site-admin.req -cert ${CERTS_DIR}/certs/ca.cert.pem -keyfile ${CERTS_DIR}/private/ca.key.pem -out ${DOMAIN_DIR}/${DOMAIN}/config/hops.site-admin.pem -config ${CERTS_DIR}/openssl-ca.cnf -key $KEYSTOREPW
sudo openssl ca -batch -in ${DOMAIN_DIR}/${DOMAIN}/config/hops.site-instance.req -cert ${CERTS_DIR}/certs/ca.cert.pem -keyfile ${CERTS_DIR}/private/ca.key.pem -out ${DOMAIN_DIR}/${DOMAIN}/config/hops.site-instance.pem -config ${CERTS_DIR}/openssl-ca.cnf -key $KEYSTOREPW

sudo -u $DESIRED_USERNAME sh -s "${HOPS_SITE_BASE}/scripts" <<'EOF'
. $1/hops-site-env.sh

cd ${DOMAIN_DIR}/${DOMAIN}/config
openssl x509 -in hops.site-admin.pem -outform DER -out hops.site-admin.der
keytool -import -noprompt -alias ${ADMIN_CERT_ALIAS} -file hops.site-admin.der -keystore keystore.jks $KEYSTORE_PASSWORD

openssl x509 -in hops.site-instance.pem -outform DER -out hops.site-instance.der
keytool -import -noprompt -alias hops.site-instance -file hops.site-instance.der -keystore keystore.jks $KEYSTORE_PASSWORD


cd ${GLASSFISH_PATH}/bin
./asadmin --port $ADMIN_PORT $ASASMDIN_PW set "configs.config.server-config.network-config.protocols.protocol.http-listener-2.ssl.cert-nickname=${ADMIN_CERT_ALIAS}"
./asadmin $ASASMDIN_PW stop-domain ${DOMAIN}
./asadmin $ASASMDIN_PW start-domain ${DOMAIN}

./asadmin --port $ADMIN_PORT $ASASMDIN_PW enable-secure-admin --adminalias hops.site-admin --instancealias hops.site-instance
./asadmin $ASASMDIN_PW stop-domain ${DOMAIN}
./asadmin $ASASMDIN_PW start-domain ${DOMAIN}

./asadmin --interactive=false --port $ADMIN_PORT $ASASMDIN_PW deploy --force=true ${HOPS_SITE_WAR}

cd ${HOPS_SITE_BASE}/scripts
./hops-site_elastic.sh

EOF

# hack fix for RootCA private key not readable by vagrant user
sudo chown root:glassfish /srv/hops/certs-dir/private/ca.key.pem
sudo chmod 440 /srv/hops/certs-dir/private/ca.key.pem
# hack fix for .rnd not owned by vagrant
sudo chown glassfish:glassfish ~/.rnd

cd ${DOMAIN_DIR}
sudo su -c 'python domain1/bin/csr-ca.py' glassfish