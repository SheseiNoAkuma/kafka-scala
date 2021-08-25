import net.liftweb.json.{DefaultFormats, Formats, Serialization}

import java.time.LocalDateTime
import scala.util.Random

case class InputData(name: String, amount: Long, timestamp: LocalDateTime) {
  implicit val formats: Formats = DefaultFormats + new LocalDateTimeSerializer
  def toJson:String = Serialization.write(this)
}
object InputData {
  def randomData(): InputData = {
    val Names = Seq(
      "Marco",
      "Steph",
      "Dude"
    )

    val random: Random = Random
    val name = Names(random.nextInt(Names.length))
    val amount = random.between(0L, 100L)

    InputData(name, amount, LocalDateTime.now())
  }
}




