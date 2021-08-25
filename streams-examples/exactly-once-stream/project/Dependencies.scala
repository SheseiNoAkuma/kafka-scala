import sbt._

object Dependencies {

//  object CustomResolvers {
//    lazy val Confluent = "confluent" at "https://packages.confluent.io/maven/"
//    lazy val Jitpack = "jitpack" at "https://jitpack.io"
//
//    lazy val resolvers: Seq[Resolver] = Seq(Confluent, Jitpack)
//  }

  object Versions {

    lazy val Kafka = "2.6.0"
    lazy val Scala = "2.13.6"
    lazy val ConfluentPlatform  = "5.5.1"
  }

  lazy val prodDeps: Seq[ModuleID] = Seq(
    "org.apache.kafka" % "kafka-clients" % Versions.Kafka,
    "org.apache.kafka" % "kafka-streams" % Versions.Kafka,
    "org.apache.kafka" % "kafka-streams-scala_2.13" % Versions.Kafka,

//    "io.confluent" % "kafka-avro-serializer" % Versions.ConfluentPlatform,

    "net.liftweb" %% "lift-json" % "3.4.3",

    "org.slf4j" % "slf4j-log4j12" % "1.7.30",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",

    "org.scalameta" %% "munit" % "0.7.26" % Test,
    "io.github.embeddedkafka" %% "embedded-kafka" % "2.6.0" % Test
  )

}
