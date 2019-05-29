package co.mscp.appointment.control

import co.mscp.appointment.service.resource.{Resource, ResourceService}
import javax.inject.Inject
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.{Await, Future}

class ResourceController @Inject()(cc: ControllerComponents, service: ResourceService)
  extends AbstractController(cc) {

  import scala.concurrent.duration._


  implicit val reads: Reads[Resource] = Json.reads[Resource]


  def create(institute: String, token: String) = Action.async(parse.json[Resource]) { implicit request =>
    println(institute)
    println(token)
    val resource: Resource = request.body

    Future.successful(Ok(Json.toJson(Await.result(service.create(token, institute, resource), 5000 millis))))
  }


}