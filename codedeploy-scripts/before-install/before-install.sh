#/bin/sh

mvn package -f ../../pom.xml
sudo /etc/init.d/tomcat8 stop
mv ../../target/rest-api-freshspire.war /var/lib/tomcat8/webapps
sudo /etc/init.d/tomcat8 start
