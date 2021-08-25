package topologies

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.scala.{ByteArrayKeyValueStore, Serdes, StreamsBuilder}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes.String
import org.apache.kafka.streams.scala.kstream.Materialized

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

case class BankAccountTopologyAvro(configuration: Configuration) {
  def build: Topology = {
    val builder = new StreamsBuilder

    implicit val bankBalanceSerde: Serde[BankBalance] = Serdes.fromFn(
      obj => TestSerialise.serialise(obj),
      bytes => Some(TestSerialise.deserialise[BankBalance](bytes)))


    implicit val materialized: Materialized[String, BankBalance, ByteArrayKeyValueStore] = {
      Materialized.as[String, BankBalance, ByteArrayKeyValueStore]("exactly-once-aggregated")
        .withKeySerde(Serdes.String)
        .withValueSerde(bankBalanceSerde)
    }

//    builder.stream[String, String](configuration.source)
//      .groupByKey
//          .aggregate(() => BankBalance.Empty)((_, _, aggregator) => aggregator)(materialized)

    ???
  }

  object TestSerialise {
    def serialise(value: Any): Array[Byte] = {
      val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
      val oos = new ObjectOutputStream(stream)
      oos.writeObject(value)
      oos.close()
      stream.toByteArray
    }

    def deserialise[T](bytes: Array[Byte]): T = {
      val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
      val value = ois.readObject
      ois.close()
      value.asInstanceOf[T]
    }
  }
}

