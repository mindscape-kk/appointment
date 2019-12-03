package co.mscp.appointment.control

import co.mscp.appointment.entity.Timeslot
import co.mscp.appointment.service.authentication.AuthenticationService
import co.mscp.appointment.service.timeslot.TimeslotService
import co.mscp.mmk2.scala.play.AbstractCrudController
import javax.inject.Inject
import play.api.libs.json.JsValue
import play.api.mvc.{Action, ControllerComponents}

import scala.concurrent.ExecutionContext


class TimeslotController @Inject()(cc: ControllerComponents,
                                   service: TimeslotService, auth: AuthenticationService)
                                  (implicit ec: ExecutionContext)
  extends AbstractCrudController[Timeslot](cc, classOf[Timeslot])
{

  def create(institute: String, token: String,resourceId: String ): Action[JsValue] = createAction {
    resource => service.create(token, institute,resourceId, resource)
  }

  def update(institute: String, token: String, id : String): Action[JsValue] = createAction {
    resource => service.update(token, institute,id, resource)
  }

  def delete(institute: String, token: String,id : String): Action[JsValue] = deleteAction {
    () => service.delete(token, institute, id)
  }


}