package eu.micro.kafka
package avro.producer

import AsJavaProp.MapAsJavaProp

import com.typesafe.scalalogging.Logger
import eu.micro.Customer
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.Random

object AvroProducer extends App {

  val logger = Logger("AvroProducer")

  val random: Random = Random

  def writeToKafka(topic: String, configuration: Map[String, String]): Future[RecordMetadata] = {

    val producer = new KafkaProducer[String, Customer](configuration.asJavaProp)

    val customer = Customer(random.nextString(7), "last-name", None, random.nextInt(), random.nextFloat(), random.nextInt())

    val record = new ProducerRecord[String, Customer](topic, random.nextString(10), customer)

    Future { producer.send(record, (meta: RecordMetadata, e: Exception) =>
      if (e == null)
        logger.info(s"sent message $meta")
      else
        logger.warn(s"an exception occurred", e)
    ).get() }
  }



  //producer configuration
  val configuration = Map(
    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
    AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG -> "http://localhost:8081",
    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringSerializer",
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> "io.confluent.kafka.serializers.KafkaAvroSerializer",
    ProducerConfig.ACKS_CONFIG -> "1",
    ProducerConfig.RETRIES_CONFIG -> "10"
  )

  private val maybeMessage = writeToKafka("avro-customer", configuration)
  private val messages = Await.result(maybeMessage, 10 second)
  logger.info(s"messages sent to broker: $messages")

}
