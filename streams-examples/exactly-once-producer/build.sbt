import Dependencies._

lazy val compileSettings = Seq(
  scalaVersion := Versions.Scala,
)

lazy val dependenciesSettings = Seq(
  libraryDependencies ++= prodDeps,
)

lazy val root = (project in file("."))
  .settings(
    name := "exactly-once-producer",
    organization := "microhatesyou",
    version := "0.1"
  )
  .settings(compileSettings: _*)
  .settings(compileSettings: _*)
  .settings(dependenciesSettings: _*)

// plugin configuration for fat jar (command is sbt assembly)
assembly / assemblyJarName := "scala-kafka-stream-fat-0.1.jar"
assembly / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
