package co.mscp.appointment.control

import javax.inject.Inject

import co.mscp.appointment.entity.Resource
import co.mscp.appointment.service.resource.ResourceService
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, ControllerComponents}

import scala.concurrent.ExecutionContext


class ResourceController @Inject()(cc: ControllerComponents, service: ResourceService)
  (implicit ec: ExecutionContext)
  extends AbstractController(cc)
{
  def create(institute: String, token: String): Action[Resource] =
    Action.async(parse.json[Resource])(request => service
      .create(token, institute, request.body)
      .map(r => Ok(Json.toJson(r))))
}