import Dependencies._

lazy val compileSettings = Seq(
  scalaVersion := Versions.Scala,
  Compile / sourceGenerators += (Compile / avroScalaGenerateSpecific).taskValue
)

lazy val dependenciesSettings = Seq(
  libraryDependencies ++= prodDeps,
  resolvers ++= CustomResolvers.resolvers
)

lazy val root = (project in file("."))
  .settings(
    name := "scala-kafka-poc",
    organization := "eu.github.micro",
    version := "0.1"
  )
  .settings(compileSettings: _*)
  .settings(compileSettings: _*)
  .settings(dependenciesSettings: _*)

idePackagePrefix := Some("eu.micro.kafka")
