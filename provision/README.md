# Provision
-------------------------

## Objectives

The objectives are:
- to get native ssh access to a VM created with Vagrant.
- to provision a VM with basic services using Ansible.


## Pre-requisite
1. VirtualBox (v 6.0, or higher)<br>
* Instructions to install here: https://www.virtualbox.org/wiki/Downloads 

2. Ansible 
* Instructions to install here: https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html.




## Tasks

1. Get to the working directory

`cd ~/<git_root_folder>/devops/provision`



2. Generate ssh key pair (if not already done)

Run the following command:

`ssh-keygen`

*Notes:*<br>
- Use `id_rsa_devops_course` as file name for the id_rsa
- Leave passphrase empty (i.e. no password)


3. Move keys to the directory `~/.ssh/` on host.


4. Add to the `~/.ssh/config` file on host

```
Host devops-vm
	User vagrant
	Hostname 192.168.58.111
	IdentityFile ~/.ssh/id_rsa_devops_course
```

5. Start the guest (i.e. VM)

`vagrant up`


6. Connect to the guest via ssh:

`ssh devops-vm`


7. Exit from the guest

`exit`


8. Provision with ansible

Run the following command in the `./ansible/` directory:

`ansible-playbook -i inventory main.yml`


The output of the previous command should look like (something similar to):

```
PLAY [all] ***********************************************************************************************************

TASK [Gathering Facts] ***********************************************************************************************
ok: [devops-vm]

TASK [package] *******************************************************************************************************
ok: [devops-vm] => (item=wget)
ok: [devops-vm] => (item=vim)
ok: [devops-vm] => (item=python-pip)

PLAY RECAP ***********************************************************************************************************
devops-vm                  : ok=2    changed=0    unreachable=0    failed=0   
```



