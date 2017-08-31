#!/bin/bash
set -e

INSTALL_PATH='/srv/hops'
GLASSFISH_PATH='/srv/hops/glassfish/versions/glassfish-4.1.2.172'
DOMAIN_DIR='/srv/hops/domains'
DOMAIN='domain2'
DOMAINPW_FILE="${DOMAIN_DIR}/${DOMAIN}/master-password"
CERTS_DIR='/srv/hops/certs-dir'
MYSQL_SERVER='10.0.2.15'
MYSQL_PORT='3306'
DB_NAME='hops_site'
MSQL_DIR='/srv/hops/mysql-cluster/ndb/scripts/'
HOPS_SITE_BASE="${INSTALL_PATH}/hops-site"
HOPS_SITE_TABLES="${HOPS_SITE_BASE}/sql/tables.sql"
HOPS_SITE_ROWS="${HOPS_SITE_BASE}/sql/rows.sql"
HOPS_SITE_TAR='hops-site.tar.gz'
HOPS_SITE_WAR="${INSTALL_PATH}/hops-site/target/hops-site.war"
HOPS_SITE_DOWNLOAD_URL="http://snurran.sics.se/hops/"
ASASMDIN_PW="--user adminuser --passwordfile ${DOMAINPW_FILE}"
KEYSTOREPW="adminpw"
KEYSTORE_PASSWORD="-srcstorepass $KEYSTOREPW -deststorepass $KEYSTOREPW -destkeypass $KEYSTOREPW"
KEY_PASSWORD="-keypass $KEYSTOREPW -storepass $KEYSTOREPW"

DOMAIN_BASE_PORT=50000
ADMIN_PORT=`expr $DOMAIN_BASE_PORT + 48` # HTTPS listener port: portbase + 81

cd ${INSTALL_PATH}

sudo wget ${HOPS_SITE_DOWNLOAD_URL}${HOPS_SITE_TAR} && tar xvzf ${HOPS_SITE_TAR} && sudo rm ${HOPS_SITE_TAR}

cd ${MSQL_DIR}
./mysql-client.sh -e "CREATE DATABASE IF NOT EXISTS hops_site"
./mysql-client.sh hops_site < ${HOPS_SITE_TABLES}
./mysql-client.sh hops_site < ${HOPS_SITE_ROWS}


cd ${GLASSFISH_PATH}/bin
echo -e "AS_ADMIN_PASSWORD=${KEYSTOREPW}\nAS_ADMIN_MASTERPASSWORD=${KEYSTOREPW}" > $DOMAINPW_FILE
./asadmin create-domain --portbase ${DOMAIN_BASE_PORT} ${DOMAIN} $ASASMDIN_PW 
./asadmin $ASASMDIN_PW start-domain ${DOMAIN}

cp ${DOMAIN_DIR}/domain1/lib/${MSQL_CONNECTOR} ${DOMAIN_DIR}/${DOMAIN}/lib/

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

keytool -genkey -alias hops.site-admin -keyalg RSA -keysize 1024 -keystore keystore.jks -dname "CN=hops.site-admin, O=SICS, L=Stockholm, ST=Sweden, C=SE" $KEY_PASSWORD
keytool -certreq -alias hops.site-admin -keyalg RSA -file hops.site-admin.req -keystore keystore.jks $KEY_PASSWORD

keytool -genkey -alias hops.site-instance -keyalg RSA -keysize 1024 -keystore keystore.jks -dname "CN=hops.site-instance, O=SICS, L=Stockholm, ST=Sweden, C=SE" $KEY_PASSWORD
keytool -certreq -alias hops.site-instance -keyalg RSA -file hops.site-instance.req -keystore keystore.jks $KEY_PASSWORD

cd ${CERTS_DIR}
sudo openssl ca -batch -in ${DOMAIN_DIR}/${DOMAIN}/config/hops.site-admin.req -cert certs/ca.cert.pem -keyfile private/ca.key.pem -out ${DOMAIN_DIR}/${DOMAIN}/config/hops.site-admin.pem -config ./openssl-ca.cnf -key $KEYSTOREPW
sudo openssl ca -batch -in ${DOMAIN_DIR}/${DOMAIN}/config/hops.site-instance.req -cert certs/ca.cert.pem -keyfile private/ca.key.pem -out ${DOMAIN_DIR}/${DOMAIN}/config/hops.site-instance.pem -config ./openssl-ca.cnf -key $KEYSTOREPW

cd ${DOMAIN_DIR}/${DOMAIN}/config
openssl x509 -in hops.site-admin.pem -outform DER -out hops.site-admin.der
keytool -import -noprompt -alias hops.site-admin -file hops.site-admin.der -keystore keystore.jks $KEYSTORE_PASSWORD

openssl x509 -in hops.site-instance.pem -outform DER -out hops.site-instance.der
keytool -import -noprompt -alias hops.site-instance -file hops.site-instance.der -keystore keystore.jks $KEYSTORE_PASSWORD


cd ${GLASSFISH_PATH}/bin
./asadmin --port $ADMIN_PORT $ASASMDIN_PW set "configs.config.server-config.network-config.protocols.protocol.http-listener-2.ssl.cert-nickname=hops.site"
./asadmin $ASASMDIN_PW stop-domain ${DOMAIN}
./asadmin $ASASMDIN_PW start-domain ${DOMAIN}

./asadmin --port $ADMIN_PORT $ASASMDIN_PW enable-secure-admin --adminalias hops.site-admin --instancealias hops.site-instance
./asadmin $ASASMDIN_PW stop-domain ${DOMAIN}
./asadmin $ASASMDIN_PW start-domain ${DOMAIN}

./asadmin --interactive=false --port $ADMIN_PORT $ASASMDIN_PW deploy --force=true ${HOPS_SITE_WAR}

cd ${HOPS_SITE_BASE}/scripts
./hops-site_elastic.sh

# hack fix for RootCA private key not readable by vagrant user
sudo chown root:vagrant /srv/hops/certs-dir/private/ca.key.pem
sudo chmod 440 /srv/hops/certs-dir/private/ca.key.pem
# hack fix for .rnd not owned by vagrant
sudo chown vagrant:vagrant ~/.rnd

