package co.mscp.appointment.control

import co.mscp.appointment.service.resource.{Resource, ResourceService}
import javax.inject.Inject
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class ResourceController @Inject()(cc: ControllerComponents, service: ResourceService)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {


  implicit val reads: Reads[Resource] = Json.reads[Resource]


  def create(institute: String, token: String) = Action.async(parse.json[Resource]) { implicit request =>
    println(institute)
    println(token)
    val resource: Resource = request.body

    service.create(token, institute, resource) map (r => Ok(Json.toJson(r)))
  }


}