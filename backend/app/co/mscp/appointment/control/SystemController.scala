package co.mscp.appointment.control

import javax.inject.Inject

import play.api.mvc.{AbstractController, Action, ControllerComponents}

class SystemController @Inject() (cc: ControllerComponents)
  extends AbstractController(cc)
{


  def getHealth: Action[Unit] = Action(parse.empty)(_ => Ok)

}
