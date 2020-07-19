#!/usr/bin/env bash

docker stop mutants-api
docker stop mutants-postgresql

./mvnw clean install -P docker

docker-compose down -v
docker rmi mutants-api:latest
docker-compose up -d
