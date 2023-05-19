#!/usr/bin/env bash
gradle clean build --no-daemon
docker-compose -f "docker-compose.yml" up -d --build
docker-compose logs -f -t permission-api