package co.mscp.appointment.service.hello.informal

import javax.inject.Singleton

import co.mscp.appointment.service.hello.{HelloMessage, HelloService}

@Singleton
class InformalHelloService extends HelloService {
  override def sayHello(token: String): HelloMessage = HelloMessage("Howdy?")
}
