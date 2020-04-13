# 4- Automate test cases



## Testing frameworks


There exist many testing frameworks out there. Some of them (and for a particular kind of testing) can be found in this [reference](https://medium.com/@alicealdaine/top-10-api-testing-tools-rest-soap-services-5395cb03cfa9). 

Probably by the time you are reading this tutorial, new testing frameworks would have come out. However, the *kind of testing* (i.e. unit, integration, acceptance, performance, etc) a frameworks helps to fulfil must remain.

The table below presents some testing frameworks for wich a detailed tutorial has been developed. These testing framework were chosen to cover different type of testings. 


| Name | Unit  | Integration | Acceptance |  NFR | Tutorial |
| :--- | :---: |   :---:     | :---: 	  | :---:| ---:	|
| [TestNG](https://testng.org/doc/) | | | Y | | [Link](https://github.com/acapozucca/TestNG)|

| [Mockito](https://site.mockito.org/) | | Y |  | Y | [Link](https://github.com/venkateshwarant/Mockito_Tutorial)|

| [Liquibase](http://www.liquibase.org/index.html) | | Y |  | Y | [Link](https://github.com/acapozucca/liquibase)|

| [DbUnit](http://dbunit.wikidot.com/) | | | | Y | [Link](https://github.com/venkateshwarant/DBUnit_tutorial)|

| [Artillery](https://artillery.io/) | | | | | Y [Link](https://github.com/venkateshwarant/Artillery_Tutorial)|








## Adding automated test cases to the pipeline



### Assumptions/Pre-requisites

1. The integration server configured in [automate build process](../s2-automate-build/README.md) is up and running.

2. A virtual machine (VM) named stage-vm is up and running.

*Notes:*<br>
- The Vagrantfile required to create and run the stage-vm VM is provided in this repo. See inside directory:<br>

`cd ~/<git_root_folder>/devops/pipeline/s3-automate-deploy/stage-vm`



## Deploy on a stage environment 

The objective is to deploy the candidate for the MavenHelloWorldProject (used in the previous steps of the tutorial) into a deploy environment.


### Strategy 1
