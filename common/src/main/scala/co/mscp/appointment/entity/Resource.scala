package co.mscp.appointment.entity

import com.fasterxml.jackson.annotation.JsonValue
import play.api.libs.json.{JsValue, Json, Reads, Writes}



case class Resource(
  id: Option[String],
  `type`: String,
  name: String,
  description: Option[String],
  userDefinedProperties: Option[JsValue])
{
  def this(t: String, name: String, description: String,
    userDefinedProperties: Option[JsValue]) =
      this(None, t, name, Some(description), userDefinedProperties)
}


object Resource {
  implicit val writes: Writes[Resource] = Json.writes[Resource]
  implicit val reads: Reads[Resource] = Json.reads[Resource]
}