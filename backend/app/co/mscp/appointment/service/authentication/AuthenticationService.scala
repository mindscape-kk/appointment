package co.mscp.appointment.service.authentication

import co.mscp.mmk2.net.CrudAction


object AuthenticationService {
  val MY = "my"
}

trait AuthenticationService {
  def getUser(token: String): Option[User]
  def getUserRole(domain: String, token: String): UserRole.Value
  def resolveInstitute(institute: String, token: String, method: CrudAction, cls: Class[_]): String
}