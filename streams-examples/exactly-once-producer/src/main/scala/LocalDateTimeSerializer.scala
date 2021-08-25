import net.liftweb.json.JsonAST.JString
import net.liftweb.json.{Formats, JValue, MappingException, Serializer, TypeInfo}

import java.time.LocalDateTime

class LocalDateTimeSerializer extends Serializer[LocalDateTime] {
  private val LocalDateTimeClass = classOf[LocalDateTime]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), LocalDateTime] = {
    case (TypeInfo(LocalDateTimeClass, _), json) => json match {
      case JString(dt) =>  LocalDateTime.parse(dt)
      case x => throw new MappingException("Can't convert " + x + " to LocalDateTime")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: LocalDateTime => JString(x.toString)
  }
}
