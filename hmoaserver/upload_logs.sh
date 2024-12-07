#!/bin/bash

# 그린, 블루 컨테이너 ID 확인
GREEN_CONTAINER=$(docker ps -qf "name=green")
BLUE_CONTAINER=$(docker ps -qf "name=blue")

LOG_DIR="/var/lib/docker/containers"
source ./env_vars.sh  # 환경 변수 로드

# 활성 컨테이너 선택
if [[ $(docker inspect -f '{{.State.Running}}' $GREEN_CONTAINER) == "true" ]]; then
  echo "현재 실행 중인 컨테이너: GREEN"
  ACTIVE_CONTAINER=$GREEN_CONTAINER
  ACTIVE_ENV="green"
elif [[ $(docker inspect -f '{{.State.Running}}' $BLUE_CONTAINER) == "true" ]]; then
  echo "현재 실행 중인 컨테이너: BLUE"
  ACTIVE_CONTAINER=$BLUE_CONTAINER
  ACTIVE_ENV="blue"
else
  echo "활성화된 컨테이너가 없습니다. 종료합니다."
  exit 1
fi

# 로그 파일 경로 확인
FULL_CONTAINER_ID=$(docker inspect -f '{{.Id}}' $ACTIVE_CONTAINER)
CONTAINER_LOG_FILE="${LOG_DIR}/${FULL_CONTAINER_ID}/${FULL_CONTAINER_ID}-json.log"

if sudo test -f "$CONTAINER_LOG_FILE"; then
  echo "로그 파일 확인: $CONTAINER_LOG_FILE"
else
  echo "로그 파일이 존재하지 않습니다: $CONTAINER_LOG_FILE"
  exit 1
fi

# 시간 설정 및 백업 파일 경로 정의
CURRENT_TIME=$(date +%Y-%m-%d-%H-%M-%S)
BACKUP_FILE="/tmp/${ACTIVE_ENV}-${CURRENT_TIME}.log"

# 로그 파일 백업
sudo cp "$CONTAINER_LOG_FILE" "$BACKUP_FILE"
if [[ $? -ne 0 ]]; then
  echo "로그 파일 백업에 실패했습니다. 종료합니다."
  exit 1
fi
echo "로그 파일 백업 완료: $BACKUP_FILE"

# S3 업로드
if [[ -z "$S3_LOG_BUCKET" ]]; then
  echo "S3 버킷 이름이 설정되지 않았습니다. env_vars.sh 파일을 확인하세요."
  exit 1
fi

sudo aws s3 cp "$BACKUP_FILE" "s3://${S3_LOG_BUCKET}/${ACTIVE_ENV}/${CURRENT_TIME}.log"
if [[ $? -ne 0 ]]; then
  echo "S3 업로드에 실패했습니다. 종료합니다."
  exit 1
fi
echo "S3 업로드 완료: s3://${S3_LOG_BUCKET}/${ACTIVE_ENV}/${CURRENT_TIME}.log"

# Docker 로그 초기화
echo "Docker 로그 파일을 초기화합니다..."
sudo truncate -s 0 "$CONTAINER_LOG_FILE"
if [[ $? -ne 0 ]]; then
  echo "로그 파일 초기화에 실패했습니다. 종료합니다."
  exit 1
fi
echo "Docker 로그 파일 초기화 완료: $CONTAINER_LOG_FILE"