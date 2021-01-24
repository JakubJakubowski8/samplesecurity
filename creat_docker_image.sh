#!/bin/sh

TAG="0.0.1-SNAPSHOT"
DOCKER_USER=user
DOCKER_PASSWORD=password
./mvnw spring-boot:build-image
docker image tag samplesecurity:${TAG} jakubjakubowski/samplesecurity:${TAG}
docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}
docker image push jakubjakubowski/samplesecurity:${TAG}
docker logout
