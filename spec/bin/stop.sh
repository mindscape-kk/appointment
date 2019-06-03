#!/usr/bin/env bash

CONTAINER_NAME="speeda-batch-spec-$1"
COMMAND="docker rm $(docker stop ${CONTAINER_NAME})"
eval $COMMAND