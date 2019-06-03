#!/usr/bin/env bash

IMAGE_NAME="co.mscp.medicom.appointment/$1:local"
CONTAINER_NAME="speeda-batch-spec-$1"
COMMAND="docker build ../src/test/resource/docker/$1 --tag ${IMAGE_NAME}"
eval ${COMMAND}

COMMAND="docker run -idt --name ${CONTAINER_NAME} $2 ${IMAGE_NAME}"
eval ${COMMAND}