FROM openjdk:8-jdk-alpine
MAINTAINER chelcovdanik@gmail.com

RUN mkdir -p /usr/src/app/
WORKDIR /usr/src/app/

COPY . /usr/src/app

EXPOSE 8080
ENTRYPOINT ["sh", "build.sh"]