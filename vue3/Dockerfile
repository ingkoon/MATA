FROM ubuntu:22.04

MAINTAINER malachai<prussian1933@naver.com>
USER root
WORKDIR /home

# Nginx installation
RUN \
    apt update -y && \
    apt install wget vim -y && \
    apt install nginx -y && \
    rm /etc/nginx/sites-available/default && \
    rm /etc/nginx/sites-enabled/default && \
    touch /etc/nginx/sites-available/myapp.conf && \
    ln -s /etc/nginx/sites-available/myapp.conf /etc/nginx/sites-enabled/myapp.conf
COPY nginx/sites-available/myapp.conf /etc/nginx/sites-available/myapp.conf

# NodeJS Installation
ENV NODE_VERSION=v19.7.0
RUN \
    apt install -y curl && \
    apt install -y build-essential && \
    curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash && \
    export NVM_DIR="$([ -z "${XDG_CONFIG_HOME-}" ] && printf %s "${HOME}/.nvm" || printf %s "${XDG_CONFIG_HOME}/nvm")" && \
    [ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh" && \
    nvm install node && \
    ln -sf $NVM_DIR/versions/node/v$NODE_VERSION/bin/node /usr/bin/nodejs && \
    ln -sf $NVM_DIR/versions/node/v$NODE_VERSION/bin/node /usr/bin/node && \
    ln -sf $NVM_DIR/versions/node/v$NODE_VERSION/bin/npm /usr/bin/npm
