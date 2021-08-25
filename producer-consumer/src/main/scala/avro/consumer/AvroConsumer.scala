package eu.micro.kafka
package avro.consumer

import AsJavaProp.MapAsJavaProp

import eu.micro.Customer
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import java.time.Duration
import java.util
import scala.jdk.CollectionConverters.IterableHasAsScala

object AvroConsumer extends App {

  private val topic = "avro-customer"

  val configuration = Map(
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> "io.confluent.kafka.serializers.KafkaAvroDeserializer",
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest",
    ConsumerConfig.GROUP_ID_CONFIG -> "scala-avro",
    AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG -> "http://localhost:8081",
    "specific.avro.reader" -> "true",
  )
  //create consumer and subscribe topic
  val consumer: KafkaConsumer[String, Customer] = new KafkaConsumer[String, Customer](configuration.asJavaProp)
  consumer.subscribe(util.Arrays.asList(topic))

  println(s"subscribing to topic $topic")
  while (true) {
    consumer.poll(Duration.ofSeconds(6000)).asScala
      .foreach(data => println(s"read message: ${data.key()} - ${data.value()}"))
  }

}
