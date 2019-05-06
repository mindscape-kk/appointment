package co.mscp.appointment.service.hello

trait HelloService {
  def sayHello(token: String): HelloMessage
}
