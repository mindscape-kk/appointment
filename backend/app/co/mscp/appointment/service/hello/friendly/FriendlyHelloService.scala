package co.mscp.appointment.service.hello.friendly

import javax.inject.{Inject, Singleton}

import co.mscp.appointment.service.authentication.AuthenticationService
import co.mscp.appointment.service.hello.{HelloMessage, HelloService}

@Singleton
class FriendlyHelloService @Inject() (auth: AuthenticationService)
  extends HelloService
{
  override def sayHello(token: String): HelloMessage = HelloMessage(
    String.format("Hello, %s!",
      auth.getUser(token).map(_.firstName).getOrElse("dear guest")))
}
