import Dependencies._
import sbt.Keys.resolvers

lazy val compileSettings = Seq(
  scalaVersion := Versions.Scala
)

lazy val dependenciesSettings = Seq(
  libraryDependencies ++= prodDeps
//    resolvers ++= CustomResolvers.resolvers
)

lazy val root = (project in file("."))
  .settings(
    name := "exactly-once-stream",
    organization := "microhatesyou",
    version := "0.1"
  )
  .settings(compileSettings: _*)
  .settings(compileSettings: _*)
  .settings(dependenciesSettings: _*)

// plugin configuration for fat jar (command is sbt assembly)
assembly / assemblyJarName := "exactly-once-stream-fat-0.1.jar"
assembly / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
