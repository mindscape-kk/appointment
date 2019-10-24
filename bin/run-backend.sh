#!/usr/bin/env bash

IMG=$(sbt dockerinfo | sed -n "s/\\[VAL\\] dockerImageName: \(.*\)$/\1/p")

ID=$(docker run -d -p9000:9000 --network=bridge ${IMG}:local-test)

docker logs -f ${ID}