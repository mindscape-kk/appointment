package co.mscp.appointment.service.authentication

import play.api.libs.json._

case class User(
  id: String,
  email: String,
  firstName: String,
  lastName: String,
  domain: String,
  role: UserRole.Value)

object User {
  implicit val writes: Writes[User] = Json.writes[User]
}
