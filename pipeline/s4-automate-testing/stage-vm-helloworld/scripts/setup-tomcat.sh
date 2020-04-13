#!/bin/bash

echo "Add Tomcat user"
sudo groupadd tomcat
sudo useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat
 
 
# Download Tomcat
echo "Download Tomcat"
cd /home/vagrant
sudo apt-get -y install curl
curl -o apache-tomcat-9.0.34.tar.gz https://downloads.apache.org/tomcat/tomcat-9/v9.0.34/bin//apache-tomcat-9.0.34.tar.gz
 
# Extract into target directory
echo "Extract into target directory"
sudo mkdir /opt/tomcat
sudo tar -xzvf apache-tomcat-9*tar.gz -C /opt/tomcat --strip-components=1
sudo rm apache-tomcat-9*tar.gz
 
 
# Assign ownership over target directory
echo "Assign ownership over target directory"
cd /opt/tomcat
sudo chgrp -R tomcat /opt/tomcat
sudo chmod -R g+r conf
sudo chmod g+x conf
sudo chown -R tomcat webapps/ work/ temp/ logs/
 
 
# Copy basic Tomcat configuration files
echo "Copy basic Tomcat configuration files"
sudo cp /vagrant_scripts/config/context.xml /opt/tomcat/webapps/manager/META-INF/context.xml
sudo cp /vagrant_scripts/config/context.xml /opt/tomcat/webapps/host-manager/META-INF/context.xml
sudo cp /vagrant_scripts/config/tomcat-users.xml /opt/tomcat/conf/tomcat-users.xml
 
 
# Copy service file and reload daemon
echo "Copy service file and reload daemon"
sudo cp /vagrant_scripts/config/tomcat.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl start tomcat
sudo ufw allow 8080
sudo systemctl enable tomcat
 
echo "Done."