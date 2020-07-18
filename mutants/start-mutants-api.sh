#!/usr/bin/env bash

./mvnw clean install -P docker

docker-compose down -v
docker-compose up -d
