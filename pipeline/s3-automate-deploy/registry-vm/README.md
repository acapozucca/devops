# Deploy on a docker registry 


## Assumptions/Pre-requisites

1. The integration server configured in [automate build process](../../s2-automate-build/README.md) is up and running.

2. A virtual machine (VM) named registry-vm is up and running.

*Notes:*<br>
- The Vagrantfile required to create and run the stage-vm VM is provided in this repo. See inside directory:<br>

`cd ~/<git_root_folder>/devops/pipeline/s3-automate-deploy/registry-vm`



## Procedure 


## Setup a private docker registry with a self sign certificate

Follow steps as indicated in:<br>

https://medium.com/@ifeanyiigili/how-to-setup-a-private-docker-registry-with-a-self-sign-certificate-43a7407a1613



## Setup GitLab Container Registry

With the Container Registry integrated into GitLab, every project can have its own space to store its Docker images.

*Notes:*<br>
- The certificate to be used is the self-signed certificated created in the previous step.

- This certificate is referred to as a TLS certificate, which is made of a certificate (.crt) file and a key (.key) file.

- These files are in: ``/docker_reg_certs` directory in the registry-vm environment



1. Place your TLS certificate and key in /etc/gitlab/ssl/

Certificate: `192.168.33.8.crt` 

Key: `192.168.33.8.key`
    
    
2. Make sure they have correct permissions:

`chmod 600 /etc/gitlab/ssl/192.168.33.8.*`


3. Once the TLS certificate is in place, edit /etc/gitlab/gitlab.rb with:
`registry_external_url ‘192.168.33.8:5000'`



4. Save the file and reconfigure GitLab for the changes to take effect.

`gitlab-ctl reconfigure`

`gitlab-ctl restart`


5. Check the `registry_external_url` is listening on HTTPS.






## Create a GitLab runner

*Notes:*<br>
- Select “shell” as executor
- The runner can be on any machine. In this case, it's on the same machine where is the registry.
- Use `[registry-vm] shell` for description
- Use `registry-vm-shell` as tag 


1. After having created the runner, add gitlab-runner user to docker group:

`sudo usermod -aG docker gitlab-runner`

2. Verify that gitlab-runner has access to Docker:

`sudo -u gitlab-runner -H docker info`





## Adapt the .gitlab-ci.yml:


1. Add at the top of the file:

```

before_script:
  - docker info


```


This allows you to check whether there is access to "docker engine" when running the pipeline.




2. Add a stage named `docker push` and a job as described below:


```
docker:
  stage: docker push
  tags:
  - registry-vm-shell
  script:
  - docker login -u "$CI_REGISTRY_USER" -p "$CI_REGISTRY_PASSWORD" $CI_REGISTRY
 
```

This allows you to check whether there is access to "docker registry" when running the pipeline.





## Appendices 


### HTTPS access for a vagrant VM

1. Redirect on 000-default on Apache configuration
 
`Redirect permanent / https://192.168.33.8/`

2. Enable module

`a2ensite default-ssl`

3. Restart apache

`service apache2 reload`







### Test access to docker registry from client

1. `docker login 192.168.33.8:5000`

2. `docker pull ubuntu:16.04`

3. `docker tag ubuntu:16.04 192.168.33.8:5000/my-ubuntu`

4. `docker push 192.168.33.8:5000/my-ubuntu`







