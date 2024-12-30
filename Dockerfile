# Build 스테이지
FROM gradle:8.11.1-jdk17 AS builder

WORKDIR /apps

COPY . /apps
RUN gradle clean build -x test --no-daemon --parallel

FROM openjdk:17-jdk-slim

LABEL type="application"

WORKDIR /apps

# 빌더 이미지에서 jar 파일만 복사
COPY --from=builder /apps/build/libs/*.jar /apps/app.jar

EXPOSE 8080

# root 대신 nobody 권한으로 실행
ENTRYPOINT [ \
   "java", \
   "-jar", \
   "/app.jar"\
]