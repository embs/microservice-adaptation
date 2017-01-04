#!/bin/sh
docker service create --name server -p "8080:8080" --network microservice-net --log-driver=fluentd --log-opt fluentd-address=$1:24224 --log-opt fluentd-async-connect=true  --log-opt tag="{{.Name}}/{{.FullID}}" --log-opt fluentd-async-connect=true maveric/ms-http-server
