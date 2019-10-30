#!/bin/bash
pip install kafka-python
wget http://apache-mirror.rbc.ru/pub/apache/kafka/2.3.1/kafka_2.11-2.3.1.tgz
tar xvzf kafka_2.11-2.3.1.tgz
TOPIC_NAME="$(whoami)_lecture10_input"
TOPIC_NAME=${TOPIC_NAME//./_}
echo $TOPIC_NAME
TOPIC_NAME_OUT="$(whoami)_lecture10_output"
TOPIC_NAME_OUT=${TOPIC_NAME_OUT//./_}
echo $TOPIC_NAME_OUT
./kafka_2.11-2.3.1/bin/kafka-topics.sh --zookeeper localhost:2181 --alter --config retention.ms=18000000 --topic $TOPIC_NAME
./kafka_2.11-2.3.1/bin/kafka-topics.sh --zookeeper localhost:2181 --alter --config retention.ms=18000000 --topic $TOPIC_NAME_OUT
rm -r ./kafka_2.11-2.3.1.tgz
rm -rf ./kafka_2.11-2.3.1
