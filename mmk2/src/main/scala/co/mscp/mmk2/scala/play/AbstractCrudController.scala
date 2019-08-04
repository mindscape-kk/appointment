package co.mscp.mmk2.scala.play

import co.mscp.mmk2.net.{ServiceError, ValidationIssue}
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, ControllerComponents}

import collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

class AbstractCrudController[E <: AnyRef](cc: ControllerComponents, cls: Class[_])(implicit ec: ExecutionContext)
  extends AbstractController(cc)
{
  def createAction(f: E => Future[E])(
    implicit reads: Reads[E], writes: Writes[E]): Action[JsValue] =
    Action.async(parse.json)(r => r.body.validate[E] match {
      case JsSuccess(e, _) => f.apply(e).map(r => Created(Json.toJson(r)))
      case e: JsError => throw toServiceError(e)
    })

  def toServiceError(error: JsError): ServiceError = {
    ServiceError.invalidEntity(cls,
      error.errors.map(item => new ValidationIssue(
        item._1.toJsonString.replaceFirst("obj.", ""),
        null,
        item._2.map(e => e.message).mkString(", "))).asJava)
  }
}
