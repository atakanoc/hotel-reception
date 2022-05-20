#!/bin/bash
chmod u+x stop_db.sh
chmod u+x show_db.sh
if ! docker info >/dev/null 2>&1; then
  echo "Docker does not seem to be running, run it first and retry"
  exit 1
fi
CONTAINER_NAME='hotel'
if [ "$(docker container inspect -f '{{.State.Running}}' $CONTAINER_NAME)" == "true" ]; then
  docker stop $CONTAINER_NAME
  docker start $CONTAINER_NAME
  echo "Done !"
  exit 0
fi
if [ "$(docker container inspect -f '{{.State.Running}}' $CONTAINER_NAME)" == "false" ]; then
  docker start $CONTAINER_NAME
  echo "Done !"
  exit 0
fi
docker pull mdeboute/hotel-sql:latest
docker run -d -p 3306:3306 --name hotel -e MYSQL_ROOT_PASSWORD=root mdeboute/hotel-sql:latest
echo "Wait a second..."
sleep 15
cd ..
java -jar exec/csv_to_db.jar
echo "Done !"
exit 0
