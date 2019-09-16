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

*Note:* Using maven to create the basic example eases the creation of the single unit test. 


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











