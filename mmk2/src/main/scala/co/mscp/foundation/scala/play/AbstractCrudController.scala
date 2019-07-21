package co.mscp.foundation.scala.play

import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc.{AbstractController, Action, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}


class AbstractCrudController[E <: AnyRef](cc: ControllerComponents, cls: Class[_])(implicit ec: ExecutionContext)
  extends AbstractController(cc)
{
  def createAction(f: E => Future[E])(
    implicit reads: Reads[E], writes: Writes[E]): Action[E] =
    Action.async(parse.json[E])(r =>
      f.apply(r.body)
        .map(v => Created(Json.toJson(v))))
}
