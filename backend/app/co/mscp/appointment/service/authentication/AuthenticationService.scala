package co.mscp.appointment.service.authentication

trait AuthenticationService {
  def getUser(token: String): Option[User]
  def getUserRole(domain: String, token: String): UserRole.Value
}