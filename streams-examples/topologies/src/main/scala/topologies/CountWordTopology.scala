package topologies

import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes.{Long, String}
import org.apache.kafka.streams.scala.StreamsBuilder

final case class CountWordTopology(config: Configuration) {

  def build(): Topology = {
    val streamsBuilder = new StreamsBuilder

    streamsBuilder
      .stream[String, String](config.source)
      .mapValues(value => value.toLowerCase())
      .flatMapValues(value => value.split(" "))
      .selectKey((_, value) => value)
      .groupByKey
      .count()
      .toStream
      .to(config.destination)

    streamsBuilder.build
  }

}
