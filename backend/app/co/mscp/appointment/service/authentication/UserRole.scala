package co.mscp.appointment.service.authentication

import play.api.libs.json._

object UserRole extends Enumeration {
  val OWNER, STAFF, PATIENT, PUBLIC = Value
  implicit val writes: Writes[UserRole.Value] = (o: UserRole.Value) => JsString(o.toString)
}