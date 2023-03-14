# Vue3 Deployment

## Container build
```
sudo docker build -t nginx:custom .
```

## Container start-up
```
sudo docker run -d -it -u root -p 80:4000 \
--network hadoop-cluster_cluster-net --name vue_app nginx:custom /bin/bash
```

## Copy Vue codes
```
mkdir -p /home/vue3
sudo docker cp . vue_app:/home/vue3
```

## Vue app start-up
```
sudo docker exec -w /home/vue3 vue_app npm i
sudo docker exec -w /home/vue3 vue_app npm run build
sudo docker exec vue_app service nginx start
```

---

# cork-vue3

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
