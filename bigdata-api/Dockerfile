FROM ubuntu:22.04

MAINTAINER malachai<prussian1933@naver.com>
USER root
WORKDIR /home

# Java Development Kit(Zulu8) installation
RUN \
    apt update -y && \
    apt install wget vim -y && \
    wget https://cdn.azul.com/zulu/bin/zulu8.68.0.21-ca-jdk8.0.362-linux_x64.tar.gz && \
    tar -xvzf zulu8.68.0.21-ca-jdk8.0.362-linux_x64.tar.gz && \
    mkdir /usr/lib/jvm && \
    mv zulu8.68.0.21-ca-jdk8.0.362-linux_x64 /usr/lib/jvm/zulu8.68.0.21-ca-jdk8.0.362-linux_x64
ENV JAVA_HOME=/usr/lib/jvm/zulu8.68.0.21-ca-jdk8.0.362-linux_x64
ENV PATH=$PATH:$JAVA_HOME/bin

# Maven Installation
RUN \
    wget https://dlcdn.apache.org/maven/maven-3/3.9.0/binaries/apache-maven-3.9.0-bin.tar.gz && \
    tar -xvzf apache-maven-3.9.0-bin.tar.gz && \
    mv apache-maven-3.9.0 /usr/local/lib/apache-maven-3.9.0
ENV MAVEN_HOME=/usr/local/lib/apache-maven-3.9.0
ENV PATH=$PATH:$MAVEN_HOME/bin

# Redis Installation
RUN \
    apt install redis-server -y
COPY redis/redis.conf /etc/redis/redis.conf