#!/usr/bin/env bash

runDbDocker() {
  COMMAND="./start.sh $1 \"-p$2:3306 -e MYSQL_ROOT_PASSWORD=uzabase\""
  eval ${COMMAND}
}

runDbDocker common 3361
runDbDocker finance 3362