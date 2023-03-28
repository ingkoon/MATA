# Jenkins

## Container build
```
sudo docker build -t jenkins/jenkins:custom .
```

## Container start-up
```
sudo docker run -d -it -u root -v ./jenkins_home:/var/jenkins_home \
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

## spring_app build script
```
cd bigdata-api
docker build -t java/maven:custom .
docker stop spring_app
docker rm spring_app
docker run -it -d --network hadoop-cluster_cluster-net --name spring_app java/maven:custom /bin/bash
docker cp . spring_app:/home/bigdata-api
docker exec -w /home/bigdata-api spring_app sh -c "mvn -Dspring.profiles.active=stage clean compile install package"
docker exec -w /home/bigdata-api -d spring_app sh -c "java -jar -Dspring.profiles.active=stage mata-api-server/target/mata-api-server-0.0.1-SNAPSHOT.jar >> /home/bigdata-api/webserver.log"
```

## vue_app build script
```
cd vue3
docker build -t node/nginx:custom .
docker stop vue_app
docker rm vue_app
docker run -it -d -p 80:80 --network hadoop-cluster_cluster-net --name vue_app node/nginx:custom /bin/bash
docker cp . vue_app:/home/vue3
docker exec -w /home/vue3 vue_app sh -c "npm i"
docker exec -w /home/vue3 vue_app sh -c "npm run build"
docker exec vue_app /etc/init.d/nginx start
```