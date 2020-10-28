# 3- Automate deployment process


## Assumptions/Pre-requisites

1. The integration server configured in [automate build process](../s2-automate-build/README.md) is up and running.

2. A virtual machine (VM) named stage-vm is up and running.

*Notes:*<br>
- The Vagrantfile required to create and run the stage-vm VM is provided in this repo. See inside directory:<br>

`cd ~/<git_root_folder>/devops/pipeline/s3-automate-deploy/stage-vm`



## Deploy on a stage environment 

The objective is to deploy the candidate for the MavenHelloWorldProject (used in the previous steps of the tutorial) into a deploy environment.


### Strategy 1

First, deploy the candidate on a artefact repository on the integration server, and then transfer the candidate to the stage environment.

1. Create directory `/home/vagrant/artefact-repository` on the integration server.

2. Change rights of directory artefact-repository

`chmod 777 artefact-repository/`

3. Create a GitLab runner (with executor "shell") on the integration server. 

*Notes:*<br>
- Use `[integration-server] shell` for description
- Use `integration-shell` as tag


4. Change the .gitlab-ci.yml file to the following content:


```

image: maven:latest

stages:
  - build
  - test
  - run
  - upload
  - deploy

  
cache:
  paths:
    - target/

build_app:
  stage: build
  tags:
    - integration
  script:
    - mvn compile

test_app:
  stage: test
  tags:
    - integration
  script:
    - mvn test

run_app:
  tags:
    - integration
  stage: run
  script:
    - mvn  package
    - mvn exec:java -Dexec.mainClass="com.jcg.maven.App"


upload_app:
    stage: upload
    tags:
    - integration
    script:
    - echo "Deploy review app"
    artifacts:
        name: "my-app"
        paths:
        - target/*.jar
        


deploy:
    stage: deploy
    tags:
    - integration-shell
    script:
    - cp target/*.jar /home/vagrant/artefact-repository
      

        
```

5. Create script to automate the deploy of a particular file into the stage-vm.

*Notes:*<br>
- The directory `/home/vagrant/stage` is the target place to deploy the file
- A ssh key pair has to be set between `integration-server` and `stage-vm` for vagrant user. This allows not to enter password when connecting through ssh.

5.1. Create script

`vim deploy-proc.sh`

5.2. Content of the script

```
#!/bin/bash

scp /home/vagrant/artefact-repository/$1 vagrant@$2:/home/vagrant/stage

```

6. Make the file executable.

`chmod 744 deploy-proc.sh`


7. Run the file.

`./deploy-proc.sh MavenHelloWorldProject-1.0-SNAPSHOT.jar stage-vm`


8. Check file is copied into the stage-vm VM, in /home/vagrant/stage directory.





### Strategy 2

Deploy directly the candidate into the stage environment.

1. Create a GitLab runner (with executor "shell") on the stage-vm server. 

*Notes:*<br>
- Use `[stage-vm] shell` for description
- Use `stage-vm-shell` as tag


2. Add gitlab-runner user to vagrant's group.

`sudo usermod -aG vagrant gitlab-runner`


3. Change the .gitlab-ci.yml file such that the deploy job looks like:

```

deploy:
    stage: deploy
    tags:
    - stage-vm-shell
    script:
    - cp target/*.jar /home/vagrant/stage


```


4. Check the candidate appears into the /home/vagrant/stage directory of the stage-vm machine.







## Appendices 


### Generate ssh key pair


1. run the following command om the local host machine:

`ssh-keygen`

*Notes:*<br>
- Use `id_rsa_stage` as file name for the id_rsa
- Leave passphrase empty (i.e. no password)


2. move keys to the directory `~/.ssh/` on local host.


3. add to the `~/.ssh/config` file on local host

```
Host stage-vm
	User vagrant
	Hostname 192.168.33.7
	IdentityFile ~/.ssh/id_rsa_stage
```

4. add file `id_rsa_stage.pub` to `/home/vagrant~/.ssh/` directory on remote host

*Notes:*<br>
- Use shared folder to ease the exchange of files among vagrant VMs. This is achieved by having in the Vagrant file the following line:

`config.vm.synced_folder "../../data", "/vagrant_data"`


5. get to the directory `/home/vagrant/.ssh/` and run:

`cat id_rsa_stage.pub >> authorized_keys`


6. connect from the local host to the remote host via ssh:

`ssh stage-vm`







### [GitLab Runner](https://docs.gitlab.com/runner/) setup



#### Install the Runner


1. ssh to the VM where you want to install the runner 

`vagrant ssh`

2. install the GitLab Runner. This is done by executing the following commands:

`curl -L https://packages.gitlab.com/install/repositories/runner/gitlab-runner/script.deb.sh | sudo bash`

`sudo apt-get install gitlab-runner`



#### Register the Runner

1. execute the following command:

`sudo gitlab-runner register`

2. enter the information related to the GitLab instance (which is part of your integration server). This requested information is as follows:

For GitLab instance URL enter:<br>
`http://192.168.33.9/gitlab/`


For the gitlab-ci token enter the generated token.<br>
`Example: 85y84QhgbyaqWo38b7qg`

For a description for the runner enter:<br>
`[stage-vm] shell`

For the gitlab-ci tags for this runner enter:<br>
`stage-vm-shell`

For the executor enter:<br>
`shell`


3. Restart the runner:

`sudo gitlab-runner restart`



*Notes:*<br>
- The current available runners on a GitLab instance can be found at:

http://<i></i>gitlab.example.com/admin/runners

Example: (http://192.168.33.9/gitlab/admin/runners)



