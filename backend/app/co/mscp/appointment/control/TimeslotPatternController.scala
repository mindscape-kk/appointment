package co.mscp.appointment.control

import co.mscp.appointment.entity.TimeslotPattern
import co.mscp.appointment.service.authentication.AuthenticationService
import co.mscp.appointment.service.timeslot.pattern.TimeslotPatternService
import co.mscp.mmk2.scala.play.AbstractCrudController
import javax.inject.Inject
import play.api.libs.json.JsValue
import play.api.mvc.{Action, ControllerComponents}

import scala.concurrent.ExecutionContext


class TimeslotPatternController @Inject()(cc: ControllerComponents,
                                          service: TimeslotPatternService, auth: AuthenticationService)
                                         (implicit ec: ExecutionContext)
  extends AbstractCrudController[TimeslotPattern](cc, classOf[TimeslotPattern])
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