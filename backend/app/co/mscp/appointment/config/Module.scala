package co.mscp.appointment.config

import co.mscp.appointment.service.authentication.AuthenticationService
import co.mscp.appointment.service.authentication.dummy.DummyAuthenticationService
import co.mscp.appointment.service.hello.HelloService
import co.mscp.appointment.service.hello.friendly.FriendlyHelloService
import co.mscp.appointment.service.resource.ResourceService
import co.mscp.appointment.service.resource.impl.postgre.PostgreResourceDao
import co.mscp.appointment.service.resource.impl.{ResourceDao, ResourceServiceImpl}
import com.google.inject.AbstractModule

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[AuthenticationService]).to(classOf[DummyAuthenticationService])
    bind(classOf[HelloService]).to(classOf[FriendlyHelloService])

    bind(classOf[ResourceService]).to(classOf[ResourceServiceImpl])
    bind(classOf[ResourceDao]).to(classOf[PostgreResourceDao])
  }
}