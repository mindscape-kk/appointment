package co.mscp.appointment.control

import co.mscp.appointment.entity.Resource
import co.mscp.appointment.service.authentication.AuthenticationService
import co.mscp.appointment.service.resource.ResourceService
import co.mscp.foundation.scala.play.AbstractCrudController
import javax.inject.Inject
import play.api.mvc.{Action, ControllerComponents}

import scala.concurrent.ExecutionContext


class ResourceController @Inject()(cc: ControllerComponents, service: ResourceService, auth: AuthenticationService)
  (implicit ec: ExecutionContext)
  extends AbstractCrudController[Resource](cc, classOf[Resource])
{

  def create(institute: String, token: String): Action[Resource] = createAction {
    resource => service.create(token, institute, resource)
  }

}