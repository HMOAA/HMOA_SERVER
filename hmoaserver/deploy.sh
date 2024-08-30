#!/bin/bash

source /home/${SERVER_USERNAME}/deploy/env_vars.sh

sed 's/^export //' /home/${SERVER_USERNAME}/deploy/env_vars.sh > /home/${SERVER_USERNAME}/docker/.env

source /home/${SERVER_USERNAME}/deploy/firebase.sh

sed 's/^export //' /home/${SERVER_USERNAME}/deploy/firebase.sh >> /home/${SERVER_USERNAME}/docker/.env

cd ../docker

sudo docker pull ${DOCKERHUB_USERNAME}/${DOCKERHUB_REPOSITORY}:${DOCKER_IMAGE_TAG}

EXIST_GREEN=$(docker ps | grep green)

if [ -z "$EXIST_GREEN"]; then
  echo "BLUE -> GREEN"
  echo "블루에서 그린으로 변경합니다."
  docker-compose pull green
  docker-compose up -d green

  while [ 1 = 1 ]; do
  echo "그린 체크"
  sleep 3

  REQUEST=$(curl http://127.0.0.1:8080 | grep HMOA)
    if [ -n "$REQUEST"]; then
      echo "정상적으로 그린이 작동합니다"
      break ;
    fi
  done

  sleep 20

  echo "nginx를 리로드 합니다."
  sudo cp /etc/nginx/hmoa.shop.green.conf /etc/nginx/sites-enabled/hmoa.shop.conf
  sudo nginx -s reload

  echo "블루를 종료합니다."
  docker-compose stop blue

else
  echo "그린 -> 블루"
  docker-compose pull blue
  docker-compose up -d blue

  while [ 1 = 1 ]; do
  echo "블루 체크"
  sleep 3

  REQUEST=$(curl http://127.0.0.1:8081 | grep HMOA)
    if [ -n "$REQUEST"]; then
      echo "정상적으로 블루가 작동합니다"
      break ;
    fi
  done

  sleep 20

  echo "nginx를 리로드 합니다."
  sudo cp /etc/nginx/hmoa.shop.blue.conf /etc/nginx/sites-enabled/hmoa.shop.conf
  sudo nginx -s reload

  echo "그린을 종료합니다."
  docker-compose stop green

fi

sudo docker image prune -a -f