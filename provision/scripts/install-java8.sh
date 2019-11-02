#!/bin/bash

cp /vagrant_data/jdk-8u231-linux-x64.tar.gz /home/vagrant/
cd /home/vagrant/
tar zxvf jdk-8u231-linux-x64.tar.gz 
sudo update-alternatives --install "/usr/bin/java" "java" "/home/vagrant/jdk1.8.0_231/bin/java" 1
