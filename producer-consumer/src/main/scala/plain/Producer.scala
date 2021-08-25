package eu.micro.kafka
package plain

import AsJavaProp.MapAsJavaProp

import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.Random

/**
 * when runs produces 5 maybeMessages with key starting with key1 and 4 starting with key2 then shutdown
 */
object Producer extends App {

  val logger = Logger("Producer")

  case class MessagesForKey(keyRoot: String, times: Int)

  def writeToKafka(topic: String, keys: List[MessagesForKey], configuration: Map[String, String]): Future[List[RecordMetadata]] = {

    val producer = new KafkaProducer[String, String](configuration.asJavaProp)

    val random: Random = Random
    //generate record
    val messages = for {
      messageKey <- keys
      _ <- 1 to messageKey.times
    } yield new ProducerRecord[String, String](topic, s"${messageKey.keyRoot}-${random.nextInt()}", s"value-${random.nextInt()}")

    //send and aggregate all eventual RecordMetadata in a single future
    val eventualMessage = Future.traverse(messages)(message => Future { producer.send(message).get() })

    eventualMessage.onComplete(_ => producer.close())
    eventualMessage
  }

  //configure and generate producer
  val configuration = Map(
    "bootstrap.servers" -> "localhost:9092",
    "key.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
    "value.serializer" -> "org.apache.kafka.common.serialization.StringSerializer"
  )

  private val maybeMessages = writeToKafka("quick-start", List(MessagesForKey("key1", 5), MessagesForKey("key2", 4)), configuration)
  private val messages: List[RecordMetadata] = Await.result(maybeMessages, 2 second)
  logger.info(s"messages sent to broker: $messages")
}
