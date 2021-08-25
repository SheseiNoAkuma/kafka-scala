import Main.logger
import com.typesafe.scalalogging.Logger
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}
import topologies.{BankAccountTopologyJson, Configuration}

import java.util.Properties
import java.util.concurrent.CountDownLatch
import scala.concurrent.duration.{DurationInt, FiniteDuration}

object Main extends App {

  val logger = Logger("Main")

  val props: Properties = new Properties()
  props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "exactlyOnceStream")
  props.setProperty(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE)

  private val aggregateTopology: Topology = BankAccountTopologyJson(Configuration("exactly-once-producer", "exactly-once-sink", "exactly-once-intermediate")).build

  private val streams = List(new KafkaStreams(aggregateTopology, props))

  sys.addShutdownHook {
    logger.info("Shutting down")
    streams.foreach(stream => {
      if (stream.state.isRunningOrRebalancing) {
        val shutdownTimeout = 1.second
        stream.close(duration2JavaDuration(shutdownTimeout))
      }
    })
  }

  val latch = new CountDownLatch(streams.size)

  streams.foreach(stream => {
    sys.addShutdownHook {
      logger.info("Shutting down")
      if (stream.state.isRunningOrRebalancing) {
        val shutdownTimeout = 1.second
        stream.close(duration2JavaDuration(shutdownTimeout))
      }
      latch.countDown()
    }

    stream.setUncaughtExceptionHandler { (_: Thread, e: Throwable) =>
      logger.error("Uncaught exception while running streams", e)
      System.exit(1)
    }
    stream.setStateListener { (newState: KafkaStreams.State, _: KafkaStreams.State) =>
      if (newState == KafkaStreams.State.ERROR) {
        logger.error(s"Transitioning to state $newState, shutting down")
        System.exit(1)
      }
    }
  })

  try {
    logger.info("Starting streams")
    streams.foreach(stream => stream.start())
    latch.await()
  } catch {
    case e: Throwable =>
      logger.error("Exception starting streams", e)
      System.exit(1)
  }

  def duration2JavaDuration(d: FiniteDuration): java.time.Duration =
    java.time.Duration.ofNanos(d.toNanos)
}
