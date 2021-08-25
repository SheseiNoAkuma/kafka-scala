package topologies

import munit.FunSuite
import org.apache.kafka.common.serialization.{LongDeserializer, StringDeserializer, StringSerializer}
import org.apache.kafka.streams.{KeyValue, StreamsConfig, Topology, TopologyTestDriver}

import java.lang
import java.util.Properties

class CountWordTopologyTest extends FunSuite {

  val configuration: Configuration = Configuration("word-count-from", "word-count-to")
  val serializer: StringSerializer = new StringSerializer
  val stringDeserializer: StringDeserializer = new StringDeserializer
  val longDeserializer: LongDeserializer = new LongDeserializer

  val topologyTestDriver: FunFixture[TopologyTestDriver] = FunFixture[TopologyTestDriver](
    setup = { _ =>
      val config: Properties = new Properties()
      config.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      config.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "streamApp03")
      config.setProperty(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE)

      val countWordsTopology: Topology = CountWordTopology(configuration).build()
      new TopologyTestDriver(countWordsTopology, config)
    },
    teardown = { topologyTestDriver =>
      topologyTestDriver.close()
    }
  )

  topologyTestDriver.test("experimenting with topologyTestDriver") { topologyTestDriver =>
    val source = topologyTestDriver.createInputTopic(configuration.source, serializer, serializer)
    val destination = topologyTestDriver.createOutputTopic(configuration.destination, stringDeserializer, longDeserializer)

    assert(clue(destination.isEmpty))

    source.pipeInput("hello world")
    assertEquals(new KeyValue("hello", new lang.Long(1L)), destination.readKeyValue())
    assertEquals(new KeyValue("world", new lang.Long(1L)), destination.readKeyValue())

    assert(clue(destination.isEmpty))

    source.pipeInput("hello again")
    assertEquals(new KeyValue("hello", new lang.Long(2L)), destination.readKeyValue())
    assertEquals(new KeyValue("again", new lang.Long(1L)), destination.readKeyValue())

    assert(clue(destination.isEmpty))
  }

}
