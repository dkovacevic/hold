#!/usr/bin/env bash
#docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
mvn package -DskipTests=true -Dmaven.javadoc.skip=true
docker build -t $DOCKER_USERNAME/hold:latest .
docker push $DOCKER_USERNAME/hold
kubectl delete pod -l name=hold -n prod
kubectl get pods -l name=hold -n prod

