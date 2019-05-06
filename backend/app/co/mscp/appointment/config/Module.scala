package co.mscp.appointment.config

import co.mscp.appointment.service.authentication.AuthenticationService
import co.mscp.appointment.service.authentication.dummy.DummyAuthenticationService
import co.mscp.appointment.service.hello.HelloService
import co.mscp.appointment.service.hello.friendly.FriendlyHelloService
import com.google.inject.AbstractModule

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[AuthenticationService]).to(classOf[DummyAuthenticationService])
    bind(classOf[HelloService]).to(classOf[FriendlyHelloService])
  }
}