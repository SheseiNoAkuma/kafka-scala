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
    name := "producer-consumer",
    organization := "eu.github.micro",
    version := "0.1"
  )
  .settings(compileSettings: _*)
  .settings(compileSettings: _*)
  .settings(dependenciesSettings: _*)

idePackagePrefix := Some("eu.micro.kafka")

//note to generate code run sbt avroScalaGenerate (see https://github.com/julianpeeters/sbt-avrohugger)
//the code will be generated in producer-consumer/target/scala-2.13/src_managed/main/compiled_avro
