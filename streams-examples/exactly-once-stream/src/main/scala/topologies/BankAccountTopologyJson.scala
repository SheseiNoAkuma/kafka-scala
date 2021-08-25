package topologies

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{JsonNodeFactory, ObjectNode}
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.connect.json.{JsonDeserializer, JsonSerializer}
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.{Consumed, Grouped, Produced}
import org.apache.kafka.streams.scala.Serdes.String
import org.apache.kafka.streams.scala.kstream.Materialized
import org.apache.kafka.streams.scala.{ByteArrayKeyValueStore, StreamsBuilder}

import java.time.Instant

case class BankAccountTopologyJson(configuration: Configuration) {
  def build: Topology = {
    val builder = new StreamsBuilder

    implicit val jsonSerde: Serde[JsonNode] = Serdes.serdeFrom(new JsonSerializer, new JsonDeserializer)

    implicit val consumed: Consumed[String, JsonNode] = Consumed.`with`(Serdes.String(), jsonSerde)
    implicit val grouped: Grouped[String, JsonNode] = Grouped.`with`(Serdes.String(), jsonSerde)
    implicit val producer: Produced[String, JsonNode] = Produced.`with`(Serdes.String(), jsonSerde)

    val materialized = Materialized.as[String, JsonNode, ByteArrayKeyValueStore](configuration.intermediary)
      .withKeySerde(Serdes.String())
      .withValueSerde(jsonSerde)

    builder.stream[String, JsonNode](configuration.source)
      .groupByKey
      .aggregate[JsonNode](initialAccount)((_, value, aggregator) => aggregateBankAccount(aggregator, value))(materialized)
      .toStream.to(configuration.destination)

    builder.build()
  }

  private def initialAccount: JsonNode = {
    val initialBalance: ObjectNode = JsonNodeFactory.instance.objectNode()
    initialBalance.put("count", 0)
    initialBalance.put("balance", 0)
    initialBalance.put("time", Instant.ofEpochMilli(0L).toString)
    initialBalance
  }

  def aggregateBankAccount(aggregator: JsonNode, value: JsonNode): JsonNode = {
    val aggregatedBalance: ObjectNode = JsonNodeFactory.instance.objectNode()
    aggregatedBalance.put("count", aggregator.get("count").asInt() + 1)
    aggregatedBalance.put("balance", aggregator.get("balance").asLong() + value.get("amount").asLong())
    aggregatedBalance.put("time", Instant.ofEpochMilli(0L).toString)
    aggregatedBalance
  }
}

