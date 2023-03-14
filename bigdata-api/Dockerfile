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