import com.fasterxml.jackson.databind.JsonNode
import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.StringSerializer

import java.util.Properties
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.reflect.classTag

object Main extends App {

  val logger = Logger("Producer")

  val props: Properties = new Properties()
  props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classTag[StringSerializer].runtimeClass.getCanonicalName)
  props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classTag[StringSerializer].runtimeClass.getCanonicalName)
  // producer acks
  props.setProperty(ProducerConfig.ACKS_CONFIG, "all") // strongest producing guarantee

  props.setProperty(ProducerConfig.RETRIES_CONFIG, "3")
  props.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1")
  // leverage idempotent producer from Kafka
  props.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true") // ensure we don't push duplicates

  logger.info("Producer starting")

  val topic = "exactly-once-producer"

  val kafkaProducer = new KafkaProducer[String, JsonNode](props)

  val producer = new KafkaProducer[String, String](props)

  private val records: IndexedSeq[Future[RecordMetadata]] = for (i <- 0 to 10000) yield {
    val inputData = InputData.randomData()
    logger.info(s"$i - value: $inputData")
    val eventualMetadata = Future { producer.send(new ProducerRecord[String, String](topic, inputData.name, inputData.toJson), (meta: RecordMetadata, e: Exception) =>
      if (e == null)
        logger.info(s"sent message $meta")
      else
        logger.warn(s"an exception occurred", e)
    ).get() }
    Thread.sleep(1000)
    eventualMetadata
  }

  private val future: Future[IndexedSeq[RecordMetadata]] = Future.sequence(records)

  Await.ready(future, Duration.Inf)
  producer.close()
}

