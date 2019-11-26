#!/bin/bash

echo "docker-compose down"
docker-compose down

echo "pull latest images"
docker pull kauriorg/parity-docker

echo "Build"
#mvn clean install -f ../pom.xml $1
#[ $? -eq 0 ] || exit $?;

echo "Start"
docker-compose up --build
[ $? -eq 0 ] || exit $?;

trap "docker-compose kill" INT
