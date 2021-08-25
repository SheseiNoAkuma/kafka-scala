package topologies

import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes.String
import org.apache.kafka.streams.scala.StreamsBuilder

case class InnerJoinTopology(configuration: Configuration){
  def build : Topology = {
    val builder = new StreamsBuilder

    val stream1 = builder.table[String, String](configuration.source1)
    val stream2 = builder.table[String, String](configuration.source2)

    stream1
      .join[String, String](stream2)((v1, v2) => v1 + "-" + v2)
      .toStream
      .to(configuration.destination)

    builder.build()
  }
}
