#!/usr/bin/env bash

IMG=docker.mscp.cp/medicom/appointment-db

ID=$(docker run -d -p5432:5432 --network=bridge ${IMG}:local-test)

docker logs -f ${ID}