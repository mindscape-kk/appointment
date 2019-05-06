package co.mscp.appointment.service.hello

import play.api.libs.json.{Format, JsValue, Json}

case class HelloMessage(message: String)

object HelloMessage {
  implicit val format: Format[HelloMessage] = Json.format[HelloMessage]
}