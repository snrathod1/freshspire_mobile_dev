#!/bin/bash
exec 3>&1 4>&2
trap 'exec 2>&4 1>&3' 0 1 2 3
exec 1>log.out 2>&1
### Everything below will go to the file 'log.out'


echo "$(date) : running codedeploy beforeinstall script" >&3
sudo /etc/init.d/tomcat8 stop
mvn package -f ../../pom.xml
mv ../../target/rest-api-freshspire.war /var/lib/tomcat8/webapps
