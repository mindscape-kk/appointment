package co.mscp.appointment.service.authentication.dummy

import javax.inject.Singleton

import co.mscp.appointment.service.authentication._

@Singleton
class DummyAuthenticationService extends AuthenticationService {
  override def getUser(token: String): Option[User] = token match {
    case "mandy" => Some(User("helloUser", "hello@dummy.com", "Mandy", "Moor",
      "Mindscape", UserRole.PATIENT))
    case _ => None
  }

  override def getUserRole(domain: String, token: String): UserRole.Value =
    getUser(token).filter(_.domain eq domain)
      .map(_.role)
      .getOrElse(UserRole.PUBLIC)
}
