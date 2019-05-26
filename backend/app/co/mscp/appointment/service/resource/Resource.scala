package co.mscp.appointment.service.resource

import play.api.libs.json.{JsValue, Json, Writes}

case class Resource(
                     id: String,
                     `type`: String,
                     name: String,
                     description: String,
                     userDefinedProperties: Map[String,String])


object Resource {
  implicit val writes: Writes[Resource] = Json.writes[Resource]
}