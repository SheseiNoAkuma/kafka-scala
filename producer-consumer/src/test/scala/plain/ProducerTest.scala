package eu.micro.kafka
package plain

import plain.Producer.MessagesForKey

import munit.FunSuite
import net.manub.embeddedkafka.Codecs.stringDeserializer
import net.manub.embeddedkafka.EmbeddedKafka.withRunningKafka
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class ProducerTest extends FunSuite {

  test("publish one message and read the same message from embedded kafka") {

    val kafkaConfig: EmbeddedKafkaConfig = implicitly[EmbeddedKafkaConfig]
    val configuration = Map (
      "bootstrap.servers" -> s"localhost:${kafkaConfig.kafkaPort}",
      "key.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
      "value.serializer" -> "org.apache.kafka.common.serialization.StringSerializer"
    )

    withRunningKafka {
      val maybeMessages = Producer.writeToKafka("new-topic", List(MessagesForKey("key", 1)), configuration)
      val messages = Await.result(maybeMessages, 1 second)

      EmbeddedKafka.consumeFirstMessageFrom("new-topic")
      assert(messages.size == 1)
    }
  }

  test("publish multiple messages and read them all from embedded kafka") {

    //todo place this in before all
    val kafkaConfig: EmbeddedKafkaConfig = implicitly[EmbeddedKafkaConfig]
    val configuration = Map (
      "bootstrap.servers" -> s"localhost:${kafkaConfig.kafkaPort}",
      "key.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
      "value.serializer" -> "org.apache.kafka.common.serialization.StringSerializer"
    )

    withRunningKafka {

    }
  }
}
