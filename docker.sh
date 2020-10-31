#!/bin/bash
cd backend
./mvnw -Dmaven.test.failure.ignore=true clean package
cd ..
docker-compose down --remove-orphans
docker-compose build
docker-compose up -d