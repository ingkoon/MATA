# Jenkins

## Container build
```
sudo docker build -t jenkins/jenkins:custom .
```

## Container start-up
```
sudo docker run -d -it -u root -v jenkins_home:/var/jenkins_home \
-v /var/run/docker.sock:/var/run/docker.sock -p 9090:8080 -p 50000:50000 \
--network hadoop-cluster_cluster-net --name jenkins_container jenkins/jenkins:custom
```

## SSH key deployment
```
sudo docker exec -u 0 jenkins_container /usr/sbin/sshd-keygen -A
sudo docker exec -u 0 jenkins_container /etc/init.d/ssh start
sudo docker exec -u 0 jenkins_container ssh-keygen -t rsa -P '' -f /root/.ssh/id_rsa
sudo bash sbin/deploy-ssh-authorized-keys.sh
```