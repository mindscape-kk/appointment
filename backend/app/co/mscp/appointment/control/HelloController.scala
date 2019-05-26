package co.mscp.appointment.control

import javax.inject.Inject

import co.mscp.appointment.service.hello.HelloService
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, ControllerComponents}


class HelloController @Inject() (cc: ControllerComponents, hello: HelloService)
  extends AbstractController(cc)
{
  def getHello(token: String) = Action { request =>
    Ok(Json.toJson(hello.sayHello(token)))
  }
}