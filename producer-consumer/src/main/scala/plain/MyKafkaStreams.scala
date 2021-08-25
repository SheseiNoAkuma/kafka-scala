package eu.micro.kafka
package plain

import AsJavaProp.MapAsJavaProp

import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes.String
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

import java.time.Duration

object MyKafkaStreams extends App {

  val configuration = Map(
    StreamsConfig.APPLICATION_ID_CONFIG -> "filter-and-transform",
    StreamsConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092"
  )

  val builder: StreamsBuilder = new StreamsBuilder
  builder.stream[String, String]("quick-start")
    .filter((key, _) => key.startsWith("key1"))
    .mapValues(value => value.prependedAll("filtered: "))
    .peek((key, value) => println(s"filtered message $key - $value"))
    .to("quick-keyOne")

  val streams: KafkaStreams = new KafkaStreams(builder.build(), configuration.asJavaProp)
  streams.start()

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }

}
