package co.mscp.appointment.service.resource

import play.api.libs.json.{JsValue, Json, Reads, Writes}

case class Resource(
                     id: String,
                     `type`: String,
                     name: String,
                     description: Option[String],
                     userDefinedProperties: Option[JsValue])


object Resource {
  implicit val writes: Writes[Resource] = Json.writes[Resource]
  implicit val reads: Reads[Resource] = Json.reads[Resource]
}