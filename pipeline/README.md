# Implementing a Deployment Pipeline


## Assumptions/Pre-requisites

### Hardware
Laptop with at least 8 Gb memory (recommended 16 Gb, ideally 32 Gb)

### Software
1. Maven (v 3.6.2)
* Instructions to install here: https://maven.apache.org/download.cgi
* Check installation with the command `mvn -version`



2. VirtualBox(v 6.0)
* Instructions to install here: https://www.virtualbox.org/wiki/Downloads 


3. Vagrant (v 2.2.5) 
* Instructions to install here: https://www.vagrantup.com/downloads.html
* (only if using Windows 10 or Windows 8 Pro) Disable Hyper-V, see instructions to disable here: https://www.poweronplatforms.com/enable-disable-hyper-v-windows-10-8/
* Check installation with the command `vagrant -v`'



## Recommended Workflow

### Summary
1. Create walking skeleton
2. Automate build process
3. Automate deployment process
4. Automate Integration Tests
5. Automate Acceptance Tests
6. Evolve




### 1- Create walking skeleton

#### Info
The example to use is “Hello world” made in java.

This example is created with the command:

`mvn archetype:generate -DgroupId=com.jcg.maven -DartifactId=MavenHelloWorldProject -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false`

**Note:*** Using maven to create the basic example eases the creation of the single unit test. 


#### Build, Test, and Run


1- Get to the working directory
`cd ~/<git_root_folder>/devops/pipeline/s1-create-skeleton/MavenHelloWorldProject`

2- Build process: create the “binaries”
`mvn compile`

3- Unit test process: create and run a single unit test that asserts “true” 
`mvn test`

4- Package the example in a .jar file
`mvn install`

5- Run the packaged version
`java -jar target/MavenHelloWorldProject-1.0-SNAPSHOT.jar`



#### Release 
The goal is to copy the “binaries” where they can be executed as if they were in production. This requires to create the **production environment**.

1- Get to the working directory
`cd ~/<git_root_folder>/devops/pipeline/s1-create-skeleton/end-prod`
    
2-  Check the Vagrantfile contains into the block config.vm.provision the following line:
` apt-get install -y default-jre`

**Note:** this allows to install the jdk-re upon starting of the environment. 

3- Install vagrant plugin to ease the copy of files into vagrant environments.
`vagrant plugin install vagrant-scp`

4- Start the vagrant environment
`vagrant up`

**Note:** the very fist time, it may take a while as the vagrant box has to be downloaded. 

5- Execute the following command to find out the id of the vagrant environment where to copy the file.
`vagrant global-status` 

6- Copy the file into the vagrant environment
`vagrant scp ../MavenHelloWorldProject/target/MavenHelloWorldProject-1.0-SNAPSHOT.jar [id_vagrant_environment]:/home/vagrant`

**Note:** replace [id_vagrant_environment] with the actual value.

7- Get into the environment
`vagrant ssh`

8- Run the binaries
`java -jar MavenHelloWorldProject-1.0-SNAPSHOT.jar`

Then expected output should be:
**Hello World!**










