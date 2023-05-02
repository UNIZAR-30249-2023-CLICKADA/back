#!/bin/bash

if [ "$#" -ne 1 ];
    then printf "Usage: $0 <development/production>\n"
    exit 85
fi

printf "[down.sh] Tearing down old artifacts\n"
sudo bash down.sh

if [ "$1" == "development" ];
  then printf "\n[up.sh] Development mode. It won't tear up java microservices\n"
  printf "\n[up.sh] Tearing up database containers\n"
  sudo docker-compose up --build edificio-db
  exit 0
fi

if [ "$1" == "production" ];
  then printf "\n[up.sh] Production mode. It will tear up ALL containers\n"
    mvn clean install
    sudo docker-compose up --build
fi