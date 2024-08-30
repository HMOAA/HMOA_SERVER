#!/bin/bash

source /home/${SERVER_USERNAME}/deploy/env_vars.sh

sed 's/^export //' /home/${SERVER_USERNAME}/deploy/env_vars.sh > /home/${SERVER_USERNAME}/docker/.env

source /home/${SERVER_USERNAME}/deploy/firebase.sh

sed 's/^export //' /home/${SERVER_USERNAME}/deploy/firebase.sh >> /home/${SERVER_USERNAME}/docker/.env

cd ../docker

sudo docker pull ${DOCKERHUB_USERNAME}/${DOCKERHUB_REPOSITORY}:${DOCKER_IMAGE_TAG}

EXIST_GREEN=$(docker ps | grep green)
COUNT=0
MAX_COUNT=12

if [ -z "$EXIST_GREEN" ]; then
  echo "BLUE -> GREEN"
  echo "블루에서 그린으로 변경합니다."
  docker-compose pull green
  docker-compose up -d green

  while [ $COUNT -lt $MAX_COUNT ]; do
    echo "그린 체크"
    sleep 5

    REQUEST=$(curl -s -o /dev/null -w "%{http_code}" http://127.0.0.1:8080)
    if [ "$REQUEST" -eq 200 ]; then
      echo "정상적으로 그린이 작동합니다"
      break
    fi
    COUNT=$((COUNT + 1))
  done

  echo "nginx를 리로드 합니다."
  sudo cp /etc/nginx/hmoa.shop.green.conf /etc/nginx/sites-enabled/hmoa.shop.conf
  sudo nginx -s reload

  echo "블루를 종료합니다."
  docker-compose stop blue

else
  echo "그린 -> 블루"
  docker-compose pull blue
  docker-compose up -d blue

  while [ $COUNT -lt $MAX_COUNT ]; do
    echo "블루 체크"
    sleep 5

    REQUEST=$(curl -s -o /dev/null -w "%{http_code}" http://127.0.0.1:8081)
    if [ "$REQUEST" -eq 200 ]; then
      echo "정상적으로 블루가 작동합니다"
      break
    fi
    COUNT=$((COUNT + 1))
  done

  echo "nginx를 리로드 합니다."
  sudo cp /etc/nginx/hmoa.shop.blue.conf /etc/nginx/sites-enabled/hmoa.shop.conf
  sudo nginx -s reload

  echo "그린을 종료합니다."
  docker-compose stop green

fi

sudo docker image prune -a -f