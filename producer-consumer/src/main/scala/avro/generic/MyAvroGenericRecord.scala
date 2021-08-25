package eu.micro.kafka
package avro.generic

import org.apache.avro.Schema
import org.apache.avro.file.{DataFileReader, DataFileWriter}
import org.apache.avro.generic._

import java.io.{File, FileInputStream}
import scala.io.{BufferedSource, Source}
import scala.util.Using

object MyAvroGenericRecord extends App {

  val schemaResource: BufferedSource = Source.fromInputStream(new FileInputStream("src/main/avro/schema1.avsc"))

  val schema: Schema = new Schema.Parser().parse(schemaResource.mkString)

  println(s"my schema is: $schema")

  private val builder = new GenericRecordBuilder(schema)
    .set("first_name", "Marco")
    .set("last_name", "Righi")
    .set("age", 42)
    .set("height", 190)
    .set("weight", 79)

  private val record: GenericData.Record = builder.build()

  println(s"my first record is: $record")

  val outputFile = new File("target/output.avro")

  Using(new DataFileWriter[GenericRecord](new GenericDatumWriter[GenericRecord](schema))) { fileWriter =>
    fileWriter.create(record.getSchema, outputFile)
    fileWriter.append(record)
  }

  println(s"Created file: ${outputFile.getAbsoluteFile.toString}")

  Using(new DataFileReader[GenericRecord](outputFile, new GenericDatumReader[GenericRecord])) { fileReader =>
    val recordLoaded: GenericRecord = fileReader.next()
    println(s"record loaded: $recordLoaded")
  }

}
