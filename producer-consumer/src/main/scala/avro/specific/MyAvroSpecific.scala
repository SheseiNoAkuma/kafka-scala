package eu.micro.kafka
package avro.specific

import eu.micro.Customer

object MyAvroSpecific extends App {

  val customer = new Customer("Name", "Surname", None, 21, 34, 5)

  println(customer)
  println(customer.getSchema)

}
