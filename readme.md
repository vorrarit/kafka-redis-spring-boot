
# Kafka, Redis, Spring Boot Integration #

This project is created for demonstrating kafka redis and spring boot integration.

## Test Kafka ##

1. Start zookeeper

        $ bin/zookeeper-server-start.sh config/zookeeper.properties

2. Start kafka

        $ bin/kafka-server-start.sh config/server.properties

    If you want to start more than one kafka, you can create server-1.properties, server-2.properties, and then start them.

        $ bin/kafka-server-start.sh config/server-1.properties
        $ bin/kafka-server-start.sh config/server-2.properties

    server-1.properties is a copy of server.properties with the following modification.

        broker.id=1
        listeners=PLAINTEXT://:9093
        log.dirs=/tmp/kafka-logs-1

3. Start message producer

        $ bin/kafka-console-producer.sh --broker-list localhost:9092,localhost:9093,localhost:9094 --topic m

4. Start message consumer

        $ bin/kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic m

5. Type something in message producer window, Then message shoud be shown in message consumer window.

---

## Test Redis ##
