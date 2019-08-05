package co.mscp.appointment.service.authentication.dummy

import co.mscp.appointment.service.authentication._
import co.mscp.mmk2.net.{CrudAction, ServiceError}
import javax.inject.Singleton

@Singleton
class DummyAuthenticationService extends AuthenticationService {
  import AuthenticationService.MY

  override def getUser(token: String): Option[User] = token match {
    case "mandy" => Some(User("helloUser", "mandy@test.com", "Mandy", "Moore",
      "ACME", UserRole.PATIENT))
    case "andy" => Some(User("helloUser", "andy@test.com", "Any", "Staffman",
      "ACME", UserRole.STAFF))
    case _ => None
  }

  override def getUserRole(domain: String, token: String): UserRole.Value =
    getUser(token).filter(_.domain eq domain)
      .map(_.role)
      .getOrElse(UserRole.PUBLIC)

  override def resolveInstitute(institute: String, token: String,
  action: CrudAction, cls: Class[_]): String =
    if (MY == institute)
      getUser(token).map(_.domain).getOrElse(throw ServiceError.badAuthorization(action, cls))
    else
      institute
}
