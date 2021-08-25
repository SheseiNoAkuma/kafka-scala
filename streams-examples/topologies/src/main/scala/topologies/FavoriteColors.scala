package topologies

import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes.{Long, String}
import org.apache.kafka.streams.scala.StreamsBuilder

/**
 * this is a stream application that reads data in the form user,color and return the count of favorites color
 * Note that the favorite color for a user can change over time, for example:
 *
 * stephan,blue
 * john,green
 * stephan,red
 * alice,red
 *
 * give as output
 * blue,0
 * green,1
 * red,2
 *
 * --config "cleanup.policy=compact" --config "delete.retention.ms=100"  --config "segment.ms=100" --config "min.cleanable.dirty.ratio=0.01"
 */
final case class FavoriteColors(config: Configuration) {

  def build(): Topology = {
    val builder = new StreamsBuilder

    builder.stream[String, String](config.source)
      .map((_, value) => {
        val splatted : Array[String] = value.split(",")
        (splatted(0), splatted(1))
      })
      .to(config.intermediary)

    builder.table[String, String](config.intermediary)
      .groupBy((_: String, colour: String) => (colour, colour))
      .count()
      .toStream
      .to(config.destination)

    builder.build
  }
}
