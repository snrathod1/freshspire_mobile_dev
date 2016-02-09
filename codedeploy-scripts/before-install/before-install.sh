#!/bin/bash

echo "Codedeploy script running" >> /home/ec2-user/log.out
echo "About to stop tomcat8" >> /home/ec2-user/log.out
sudo /etc/init.d/tomcat8 stop
mvn package -f ../../pom.xml
mv ../../target/rest-api-freshspire.war /var/lib/tomcat8/webapps

echo ".war file hopefully moved, here's what's in the tomcat folder:" >> /home/ec2-user/log.out
echo $(ls /var/lib/tomcat8/webapps) >> /home/ec2-user/log.out
