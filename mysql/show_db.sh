#!/bin/bash
if ! docker info >/dev/null 2>&1; then
  echo "Docker does not seem to be running, run it first and retry"
  exit 1
fi
CONTAINER_NAME='hotel'
CID=$(docker ps -q -f status=running -f name=^/${CONTAINER_NAME}$)
if [ ! "${CID}" ]; then
  echo "Container doesn't exist"
  echo "Please launch the database with './start_db.sh' first"
  unset CID
  exit 1
fi
echo -e "Once you're on the mysql shell please type 'use hotel;' and then 'show tables;' to see the database\nIf u want to exit just type 'exit'\n"
sleep 4
docker exec -it $CONTAINER_NAME bash -c "mysql -uroot -proot"
exit
exit 0
