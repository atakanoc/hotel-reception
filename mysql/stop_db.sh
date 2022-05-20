#!/bin/bash
if ! docker info >/dev/null 2>&1; then
  echo "Docker does not seem to be running, run it first and retry"
  exit 1
fi
CONTAINER_NAME='hotel'
CID=$(docker ps -q -f status=running -f name=^/${CONTAINER_NAME}$)
if [ ! "${CID}" ]; then
  echo "Container doesn't exist or is not running right now"
  echo "Please launch the database with './start_db.sh' first"
  unset CID
  exit 1
fi
if [ "$(docker container inspect -f '{{.State.Running}}' $CONTAINER_NAME)" == "true" ]; then
  docker stop $CONTAINER_NAME
  echo "Done !"
  exit 0
fi
echo "Please launch the database with './start_db.sh' first"
exit 1
