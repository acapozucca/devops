# 4- Automate test cases



## Testing frameworks


There exist many testing frameworks out there. Some of them (and for a particular kind of testing) can be found in this [reference](https://medium.com/@alicealdaine/top-10-api-testing-tools-rest-soap-services-5395cb03cfa9). 

Probably by the time you are reading this tutorial, new testing frameworks would have come out. However, the *kind of testing* (i.e. unit, integration, acceptance, performance, etc) a framework helps to fulfil must remain.

The table below presents some testing frameworks for wich a detailed tutorial has been developed. These testing framework were chosen to cover different type of testings. 


| Name | Unit  | Integration | Acceptance |  NFR | Tutorial |
| :---: | :---: |   :---:     | :---: 	  | :---:| :---:	|
| [TestNG](https://testng.org/doc/) | | | Y | | https://github.com/acapozucca/TestNG|
| [Mockito](https://site.mockito.org/) | | Y |  | Y | https://github.com/venkateshwarant/Mockito_Tutorial |
| [Liquibase](http://www.liquibase.org/index.html) | | Y |  | Y | https://github.com/acapozucca/liquibase|
| [DbUnit](http://www.dbunit.org/) | | | | Y | https://github.com/venkateshwarant/DBUnit_tutorial|
| [Artillery](https://artillery.io/) | | | | Y | https://github.com/venkateshwarant/Artillery_Tutorial|




## Adding automated test cases to the pipeline
This section details the steps to include automated test cases into a pipeline.


### Assumptions/Pre-requisites

1. The integration server configured in [automate build process](../s2-automate-build/README.md) is up and running.

2. This tutorial makes use of the [Web-based Hello World](https://github.com/acapozucca/helloworld) as case study to be tested. 
Thus, it is strongly recommended to get familiar with the case study before running this tutorial. 


### Setup stage environment

1. The Vagrantfile required to create and run the stage-vm-helloworld VM is provided in this repo. Go to the directory:

```
cd ~/<git_root_folder>/devops/pipeline/s4-automate-testing/stage-vm-helloworld
```

2. Start the staging environment and jump into it.
```
vagrant up
vagrant ssh
```

3. Check Tomcat installation and configuration. 
Open a browser, and try to access to these URLs:
```
http://192.168.33.17:8080
http://192.168.33.17:8080/manager/html
```
**Notes**: 

* User and password are the same: "admin".

* In case these URLs cannot be reached, then try to fix it by restarting tomcat:
```
sudo /opt/tomcat/bin/shutdown.sh
sudo /opt/tomcat/bin/startup.sh
```


4. Create case study's DB. 
This is done by executing a the following command from within the VM
```
mysql -u root -p  < /vagrant_scripts/db-helloworld.sql
```


5. Install GitLab Runner. This is done by executing the following commands:

```
curl -L https://packages.gitlab.com/install/repositories/runner/gitlab-runner/script.deb.sh | sudo bash

sudo apt-get install gitlab-runner
```



6. Register a runner on the staging environment:

```
sudo gitlab-runner register
```

Then enter the information related to the GitLab instance (which is part of your integration server). This requested information is as follows:

For GitLab instance URL enter:<br>
`http://192.168.33.9/gitlab/`


For the gitlab-ci token enter the generated token.<br>
`Example: 85y84QhgbyaqWo38b7qg`

For a description for the runner enter:<br>
`[stage-vm-helloworld] shell`

For the gitlab-ci tags for this runner enter:<br>
`stage-vm-helloworld-shell`

For the executor enter:<br>
`shell`



7. Restart the runner:
```
sudo gitlab-runner restart
```



8. Grant sudo permissions to the gitlab-runner
```
sudo usermod -a -G sudo gitlab-runner
sudo visudo
```

Now add the following to the bottom of the file:
```
gitlab-runner ALL=(ALL) NOPASSWD: ALL
```

9.  Restart the staging environment
```
exit
vagrant reload
```

10. Check if the case study is accessible
```
http://192.168.33.17:8080/helloworld/
http://192.168.33.17:8080/helloworld/helloworld.html
http://192.168.33.17:8080/helloworld/FirstServlet
http://192.168.33.17:8080/helloworld/select
```

**Note**: in case these URLs cannot be reached, then try to fix it by restarting tomcat:
```
sudo /opt/tomcat/bin/shutdown.sh
sudo /opt/tomcat/bin/startup.sh
```


### Setup local and remote git repositories

Use the version of GitLab running into the integration server to create a new repository named "helloworld".

1. Clone this repository on your local working computer (i.e. the host)

2. Add to the local repository the Web-based Hello World case study maven project. It is available at:
```
https://github.com/acapozucca/helloworld/tree/master/product.helloworld
```

3. Add the Maven project named "product.helloworld.testing.testng" to your local repository. This project is available at:
```
~/<git_root_folder>/devops/pipeline/s4-automate-testing/
```


4. Commit and push the modifications to the remote repository.




### Setup pipeline

1. Create file *.gitlab-ci.yml* and place it in the root of your local repository

2. Copy and paste the following content in the file:
```
image: maven:3.6.2-jdk-8

stages:
  - build
  - upload
  - deploy
  - test

variables:
  # This will suppress any download for dependencies and plugins or upload messages which would clutter the console log.
  # `showDateTime` will show the passed time in milliseconds. You need to specify `--batch-mode` to make this work.
  MAVEN_OPTS: "-Dhttps.protocols=TLSv1.2 -Dmaven.repo.local=$CI_PROJECT_DIR/.m2/repository -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=WARN -Dorg.slf4j.simpleLogger.showDateTime=true -Djava.awt.headless=true"
  # As of Maven 3.3.0 instead of this you may define these options in `.mvn/maven.config` so the same config is used
  # when running from the command line.
  # `installAtEnd` and `deployAtEnd` are only effective with recent version of the corresponding plugins.
  MAVEN_CLI_OPTS: "--batch-mode --errors --fail-at-end --show-version -DinstallAtEnd=true -DdeployAtEnd=true"

  STAGE_BASE_URL: "http://192.168.33.17:8080"


  # Define Tomcat's variables on Staging environment
  APACHE_HOME: "/opt/tomcat"
  APACHE_BIN: "$APACHE_HOME/bin"
  APACHE_WEBAPPS: "$APACHE_HOME/webapps"

  # Define product's variables
  PRODUCT_SNAPSHOT_NAME: "product.helloworld-0.0.1-SNAPSHOT.war"
  RESOURCE_NAME: "helloworld.war"



cache:
  paths:
    - .m2/repository
    - product.helloworld/target/

build_app:
  stage: build
  tags:
    - integration
  script:
    - mvn -f product.helloworld/pom.xml $MAVEN_CLI_OPTS clean install


upload_app:
  stage: upload
  tags:
    - integration
  script:
    - echo "Upload WAR file"
  artifacts:
    name: "helloworld"
    paths:
      - product.helloworld/target/*.war
    expire_in: 3 mins 4 sec
        

deploy:
  stage: deploy
  tags:
    - stage-vm-helloworld-shell
  script:
    - echo "Shutdown Tomcat"
    - sudo sh $APACHE_BIN/shutdown.sh
    
    - echo "Deploy generated product into the stage-vm-helloworld environment" 
    - sudo cp product.helloworld/target/$PRODUCT_SNAPSHOT_NAME $APACHE_WEBAPPS
    
    - echo "Set right name"
    - sudo mv $APACHE_WEBAPPS/$PRODUCT_SNAPSHOT_NAME $APACHE_WEBAPPS/$RESOURCE_NAME
    
    - echo "Set user asnd group rights"
    - sudo chown tomcat:tomcat $APACHE_WEBAPPS/$RESOURCE_NAME
    
    - echo "Start up Tomcat"
    - sudo sh $APACHE_BIN/startup.sh


testng:
  stage: test
  tags:
    - integration
  services:
    - name: selenium/standalone-chrome:latest 
  script:
    - mvn -f product.helloworld.testing.testng/pom.xml $MAVEN_CLI_OPTS -Denv.BASEURL=$STAGE_BASE_URL test

```

**Notes**: 

* The pipeline relies on the existence of yet another GitLab Runner tagged with label *integration*. 
This runner exists if you have followed the instructions given in [Automate build process](https://github.com/acapozucca/devops/tree/master/pipeline/s2-automate-build)

* The current available runners on GitLab can be found at:
```
http://192.168.33.9/gitlab/admin/runners
```


3. Commit and push the modifications. This event (i.e. pushing to the remote repository) will trigger the pipeline.


4. That's all. Now, the remote repository is configured to execute the pipeline every time a modification is pushed.

**Note**: if the pipeline is not automatically started, then check in the settings of the project if "Auto DevOps" is selected.
To access to these settings, go to "Settings" -> "CI/CD" -> "Auto DevOps".



### Important remarks

* The test cases being executed by the pipeline correspond to those described in [TestNG tutorial](https://github.com/acapozucca/TestNG).
* These test cases have been modified to execute headless. These modifications are: 

```
@BeforeTest
  public static void configureDriver() throws MalformedURLException {
	final ChromeOptions chromeOptions = new ChromeOptions();
	chromeOptions.addArguments("--headless");
	chromeOptions.addArguments("--no-sandbox");
	chromeOptions.addArguments("--disable-dev-shm-usage");
	chromeOptions.addArguments("--window-size=1200x600");

	chromeOptions.setBinary("/usr/bin/google-chrome");
	DesiredCapabilities capability = DesiredCapabilities.chrome();
	capability.setBrowserName("chrome");
	capability.setPlatform(Platform.LINUX);

	capability.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

	driver = new RemoteWebDriver(new URL("http://selenium__standalone-chrome:4444/wd/hub"), capability);
 }

```

* The test cases have also been modified to set the *serverBaseURL* using information that is passed as parameter via the pom.xml file. 
In the test file:
```
private static String serverBaseURL= System.getProperty("serverBaseURL");
```

In the pom.xml:
```
 <configuration>
   	<systemPropertyVariables>
    	<serverBaseURL>${env.BASEURL}</serverBaseURL>
	</systemPropertyVariables>
   ...
  </configuration>
```

In the pipeline, in the job "testng" for stage "test":
```
 - mvn -f product.helloworld.testing.testng/pom.xml $MAVEN_CLI_OPTS -Denv.BASEURL=$STAGE_BASE_URL test
```

Notice that *$STAGE_BASE_URL* is a variable defined in the pipeline:
```
  STAGE_BASE_URL: "http://192.168.33.17:8080"
```

* The job "testng" relies on the notion of *service* to run a docker image inside the job. This image is "selenium/standalone-chrome" (the latest available version).
This container runs a Chrome standalone selenium server, which is used to run the test cases using a Chrome browser being run inside such a container,   

For more information about services in GitLab look at this reference:
```
https://docs.gitlab.com/ee/ci/services/
```


