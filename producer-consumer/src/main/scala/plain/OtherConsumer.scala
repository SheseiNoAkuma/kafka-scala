package eu.micro.kafka
package plain

import AsJavaProp.MapAsJavaProp

import org.apache.kafka.clients.consumer.KafkaConsumer

import java.time.Duration
import java.util
import scala.jdk.CollectionConverters.IterableHasAsScala

object OtherConsumer extends App {

  def consumeFromKafka(topic: String): Unit = {

    //configuration
    val configuration = Map(
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "auto.offset.reset" -> "latest",
      "group.id" -> "scala-kafka-poc"
    )

    //create consumer and subscribe topic
    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](configuration.asJavaProp)
    consumer.subscribe(util.Arrays.asList(topic))

    println(s"subscribing to topic $topic")
    while (true) {
      val record = consumer.poll(Duration.ofSeconds(10)).asScala
      for (data <- record.iterator)
        println(s"read message: ${data.key()} - ${data.value()}")
    }
  }

  consumeFromKafka("quick-keyOne")
}
