#!/usr/bin/python

from kafka import KafkaProducer, KafkaConsumer
import getpass


def topic_prefix():
    username = getpass.getuser()
    return username.replace(".", "_")


def input_topic():
    return "{}_lecture10_input".format(topic_prefix())


def output_topic():
    return "{}_lecture10_output".format(topic_prefix())


def main():
    print("Input topic is {}".format(input_topic()))
    print("Output topic is {}".format(output_topic()))
    producer = KafkaProducer(bootstrap_servers='spark-cluster-master.newprolab.com:6667')
    consumer = KafkaConsumer(bootstrap_servers='spark-cluster-master.newprolab.com:6667')
    consumer.subscribe(topics=[output_topic()])

    while True:
        line = raw_input("Enter sentence: ")
        producer.send(input_topic(), line)
        result = consumer.poll(max_records=1, timeout_ms=1000 * 5)
        print((result.items()[0][1][0]).value)


if __name__ == '__main__':
    main()
