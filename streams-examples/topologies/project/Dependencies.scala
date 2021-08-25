import sbt._

object Dependencies {

  object Versions {
    lazy val Kafka = "2.6.0"
    lazy val Scala = "2.13.6"
  }

  lazy val prodDeps: Seq[ModuleID] = Seq(
    "org.apache.kafka" % "kafka-clients" % Versions.Kafka,
    "org.apache.kafka" % "kafka-streams" % Versions.Kafka,
    "org.apache.kafka" % "kafka-streams-scala_2.13" % Versions.Kafka,

    "org.slf4j" % "slf4j-log4j12" % "1.7.30",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",

    "org.scalameta" %% "munit" % "0.7.26" % Test,
    "org.apache.kafka" % "kafka-streams-test-utils" % Versions.Kafka % Test
  )

}
