package eu.micro.kafka

import java.util.Properties

trait AsJavaProp {
  def asJavaProp: Properties
}

object AsJavaProp {
  implicit class MapAsJavaProp(map: Map[String, String]) extends AsJavaProp {
    override def asJavaProp: Properties = map.foldLeft(new Properties())((p, entry) => {
      p.put(entry._1, entry._2)
      p
    })
  }
}

