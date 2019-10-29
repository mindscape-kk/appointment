package co.mscp.appointment.control

import co.mscp.appointment.entity.Resource
import co.mscp.appointment.service.authentication.AuthenticationService
import co.mscp.appointment.service.resource.ResourceService
import co.mscp.mmk2.scala.play.AbstractCrudController
import javax.inject.Inject

import play.api.libs.json.JsValue
import play.api.mvc.{Action, ControllerComponents}

import scala.concurrent.ExecutionContext


class ResourceController @Inject()(cc: ControllerComponents,
  service: ResourceService, auth: AuthenticationService)
  (implicit ec: ExecutionContext)
  extends AbstractCrudController[Resource](cc, classOf[Resource])
{

  def create(institute: String, token: String): Action[JsValue] = createAction {
    resource => service.create(token, institute, resource)
  }

  def update(institute: String, token: String): Action[JsValue] = createAction {
    resource => service.update(token, institute, resource)
  }


}