# 1- Create walking skeleton

## Info
The example to use is “Hello world” made in java.

This example is created with the command:

`mvn archetype:generate -DgroupId=com.jcg.maven -DartifactId=MavenHelloWorldProject -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false`

**Note:** Using maven to create the basic example eases the creation of the single unit test. 


## Build, Test, and Run


1- Get to the working directory

`cd ~/<git_root_folder>/devops/pipeline/s1-create-skeleton/MavenHelloWorldProject`

2- Build process: create the “binaries”

`mvn clean compile`

3- Unit test process: create and run a single unit test that asserts “true” 

`mvn test`

4- Package the example in a .jar file

`mvn package`

5- Run the packaged version

`java -jar target/MavenHelloWorldProject-1.0-SNAPSHOT.jar`



## Release 
The goal is to copy the “binaries” where they can be executed as if they were in production. This requires to create the **production environment**.

1- Get to the working directory

`cd ~/<git_root_folder>/devops/pipeline/s1-create-skeleton/env-prod`
    
2-  Check the Vagrantfile contains into the block config.vm.provision the following line:

` apt-get install -y default-jre`

**Note:** this allows to install the Java Runtime Environment (JRE) upon starting of the environment. 

3- Install vagrant plugin to ease the copy of files into vagrant environments.

`vagrant plugin install vagrant-scp`

4- Start the vagrant **production environment**

`vagrant up`

**Note:** the very fist time, it may take a while as the vagrant box has to be downloaded. 

5- Execute the following command to find out the id of the vagrant **production environment** where to copy the file.

`vagrant global-status` 

6- Copy the file into the vagrant **production environment**

`vagrant scp ../MavenHelloWorldProject/target/MavenHelloWorldProject-1.0-SNAPSHOT.jar [id_vagrant_environment]:/home/vagrant`

**Note:** replace [id_vagrant_environment] with the actual value.

7- Get into the **production environment**

`vagrant ssh`

8- Run the binaries in the **production environment**

`java -jar MavenHelloWorldProject-1.0-SNAPSHOT.jar`

The expected output should be: **Hello World!**



## Deploy  

The goal is to copy the “binaries” where they can be tested (as if they were in production). This requires to create the **staging environment**.

1- Get to the working directory

`cd ~/<git_root_folder>/devops/pipeline/s1-create-skeleton/env-stage`


2-  Check the Vagrantfile contains into the block config.vm.provision the following line:

` apt-get install -y default-jdk`

**Note:** this allows to install the Java Development Kit (JDK) upon starting of the environment.


3- Start the vagrant **staging environment**

`vagrant up`


4- Copy the file into the vagrant **staging environment**

`vagrant scp ../MavenHelloWorldProject/target/MavenHelloWorldProject-1.0-SNAPSHOT.jar [id_vagrant_environment]:/home/vagrant`

**Note:** replace [id_vagrant_environment] with the actual value.


5- Get into the **staging environment**

`vagrant ssh`

6- Run the binaries in the **staging environment**

`java -jar MavenHelloWorldProject-1.0-SNAPSHOT.jar`

The expected output should be: **Hello World!**




## Integration/Acceptance Test

The goal is to create and run a single integration (or acceptance test) against the deployed copy (of course, using the staging environment).

1- Get to the working directory

`cd ~/<git_root_folder>/devops/pipeline/s1-create-skeleton/ITMavenHelloWorldProject`


2- Add file to local repository

`mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=../MavenHelloWorldProject/target/MavenHelloWorldProject-1.0-SNAPSHOT.jar`


3- Compile and package the integration tests

`mvn clean compile assembly:single`


4- Run the integration tests into the local environment


`java -jar target/ITMavenHelloWorldProject-1.0-SNAPSHOT-jar-with-dependencies.jar`

The expected output should be: 

**Integration Testing**

**Call 1**<br>
**Hello World!**

**Call 2**<br>
**Hello World!**

**Call 3**<br>
**Hello World!**



5- Copy the integration tests into the vagrant **staging environment**

`cd ../../env-stage/`

`vagrant scp ../ITMavenHelloWorldProject/target/ITMavenHelloWorldProject-1.0-SNAPSHOT-jar-with-dependencies.jar [id_vagrant_environment]:/home/vagrant`

**Note:** replace [id_vagrant_environment] with the actual value.


6- Get into the **staging environment**

`vagrant ssh`

7- Run the integration tests in the **staging environment**

`java -jar ITMavenHelloWorldProject-1.0-SNAPSHOT-jar-with-dependencies.jar`

The expected output should be the same as in the local environment.



## Next steps

A more elaborated [Web-based "Hello World" example](https://github.com/acapozucca/helloworld) has been developed.

This example presents the working environment from the developer viewpoint. Thus, it shows how to setup a development working environment for the tasks meant to be done when developing (i.e. coding, testing).



**Exercises**:

1. Analyse how this example is locally run by the developer.
2. Analyse how this example is tested by the developer.
3. Use Vagrant to create the "production environment" for this example.
4. Use Vagrant to create the "staging environment" for this example.










