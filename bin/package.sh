#!/usr/bin/env bash

TAG=${1:-local-test}

DOCKERINFO=$(sbt dockerinfo)

VER=$(echo "${DOCKERINFO}" | sed -n "s/\\[VAL\\] version: \(.*\)$/\1/p")
IMG=$(echo "${DOCKERINFO}" | sed -n "s/\\[VAL\\] dockerImageName: \(.*\)$/\1/p")

sbt "project backend" docker:publishLocal
docker tag "$IMG:$VER" "$IMG:$TAG"

DB_IMG=docker.mscp.cp/medicom/appointment-db

docker build -t "${DB_IMG}:${VER}" spec/src/test/resources/docker/db
docker tag "$DB_IMG:$VER" "$DB_IMG:$TAG"

echo "Database: $DB_IMG:$TAG"
echo "Backend : $IMG:$TAG"