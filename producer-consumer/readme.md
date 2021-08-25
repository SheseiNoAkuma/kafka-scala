# Small poc of Producer/Consumer/Streams on Kafka with scala

## to deploy kafka on local machine
in directory `kafka` run `docker-compose up` this will start all containers in confluent architecture:
 *  **zookeeper**
 *  **kafka** on port `9092`
 *  **kafka-tools** this container allows you to perform command line actions   
 *  **control center** available at [http://localhost:9021](http://localhost:9021)
 *  **connect**
 *  **ksql-datagen**
 *  **ksqldb-cli**
 *  **ksqldb-server**
 *  **rest-proxy**
 *  **schama-registry**

## Producer / Consumer
Code was generated following [this tutorial](https://dzone.com/articles/hands-on-apache-kafka-with-scala)

## Kafka Streams
code was generate following [this tutorial](https://kafka.apache.org/28/documentation/streams/) 

## Connectors 

try out -> --property parse.key=true --property key.separator=,


## CLI 

this is taken from [Setting Up Your Local Event-Driven Environment Using Kafka Docker](https://betterprogramming.pub/your-local-event-driven-environment-using-dockerised-kafka-cluster-6e84af09cd95) and from [Apache Kafka Series - Learn Apache Kafka for Beginners
](https://learning.oreilly.com/videos/apache-kafka-series/9781789342604/)

run this commands inside the shell of kafka-tools

 * **create** a new topic: be careful the replication factor must be equal or less the number of brokers in the cluster: `kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 2 --topic to-do-list` 
 * **list** all topics: `kafka-topics --list --bootstrap-server localhost:9092`
 * topic **details**: `kafka-topics --describe --bootstrap-server localhost:9092 --topic to-do-list`
 * **delete** a topic: `kafka-topics --delete --bootstrap-server localhost:9092 --topic my-second-topic`

 * **create a message** on a topic: `kafka-console-producer --broker-list localhost:9092 --topic to-do-list --property "parse.key=true" --property "key.separator=:"` then input key:message for example   
`1:first message`   
`2:second message`  
`3:third message`   
   you can also add additional property for ex. `--producer-property ack=all` 
 * **consume a message** from a topic: `kafka-console-consumer --bootstrap-server localhost:9092 --from-beginning --topic to-do-list --property "print.key=true"`   
   to add the consumer to a group `--group my-consumer-group`

 * list all **consumer groups**: `kafka-consumer-groups --bootstrap-server localhost:9092 --list`
 * **details of a consumer group**: `kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group consumer-group`
 * **reset offset** of a group for all it's topics: `kafka-consumer-groups --bootstrap-server localhost:9092 --reset-offsets --to-earliest --execute --all-topics --group scala-kafka-poc`   
   if you want to reprocess only n messages use `--shift-by -n`


## CLI with avro

NOTE: this should be run not on the broker but on the schema registry image

 * first create a topic
 * create an avro producer: kafka-avro-console-producer --topic example-topic-avro --bootstrap-server broker:9092 --property value.schema="$(< /schemas/schema1.avsc)"

