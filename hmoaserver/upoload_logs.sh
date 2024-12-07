#!/bin/bash

# 그린, 블루 컨테이너 ID
GREEN_CONTAINER=$(docker ps -qf "name=green")
BLUE_CONTAINER=$(docker ps -qf "name=blue")

LOG_DIR="/var/lib/docker/containers"

# 현재 돌아가는 컨테이너 선택
if [[ $(docker inspect -f '{{.State.Running}}' $GREEN_CONTAINER) == "true" ]]; then
  ACTIVE_CONTAINER=$GREEN_CONTAINER
  ACTIVE_ENV="green"
elif [[ $(docker inspect -f '{{.State.Running}}' $BLUE_CONTAINER) == "true" ]]; then
  ACTIVE_CONTAINER=$BLUE_CONTAINER
  ACTIVE_ENV="blue"
else
  echo "활성화된 컨테이너 x"
  exit 1
fi

CONTAINER_LOG_FILE="${LOG_DIR}/${ACTIVE_CONTAINER}/${ACTIVE_CONTAINER}-json.log"

# 로그 저장 및 S3 업로드
if [ -f "$CONTAINER_LOG_FILE" ]; then
  CURRENT_TIME=$(date +%Y-%m-%d)
  BACKUP_FILE="/tmp/${ACTIVE_ENV}-${CURRENT_TIME}.log"

  # 로그 파일 복사
  cp "$CONTAINER_LOG_FILE" "$BACKUP_FILE"

  # s3 업로드
  aws s3 cp "$BACKUP_FILE" "s3://${S3_LOG_BUCKET}/${ACTIVE_ENV}/${CURRENT_TIME}.log" --region "ap-northeast-2"

  # Docker 로그 초기화
  : > "$CONTAINER_LOG_FILE"

  echo "$CURRENT_TIME - 로그 초기화 및 업로드 완료"
else
  echo "로그 파일을 찾을 수 없음."
  exit 1
fi