package co.mscp.mmk2.scala.play

import co.mscp.mmk2.net.{ServiceError, ValidationIssue}
import play.api.libs.json.{JsValue, _}
import play.api.libs.streams.Accumulator
import play.api.mvc.{AbstractController, Action, BodyParser, ControllerComponents}

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}



object AbstractCrudController {

  private def ignoreJs[JsValue](body: JsValue): BodyParser[JsValue] = BodyParser("ignoreJs") { _ =>
    Accumulator.done(Right(body))
  }

  private def emptyJson: BodyParser[JsValue] = ignoreJs(null)

}


class AbstractCrudController[E <: AnyRef](cc: ControllerComponents, cls: Class[_])(implicit ec: ExecutionContext)
  extends AbstractController(cc)
{
  import AbstractCrudController._

  def createAction(f: E => Future[E])(
    implicit reads: Reads[E], writes: Writes[E]): Action[JsValue] =
    Action.async(parse.json)(r => r.body.validate[E] match {
      case JsSuccess(e, _) => f.apply(e).map(r => Created(Json.toJson(r)))
      case e: JsError => throw toServiceError(e)
    })


  def deleteAction(f: () => Future[E])(
    implicit reads: Reads[E], writes: Writes[E]): Action[JsValue] =
    Action.async(emptyJson)(r => r match {
      case _ => f.apply().map(r => Created(Json.toJson(r)))
    })


  def updateAction(f: E => Future[E])(
    implicit reads: Reads[E], writes: Writes[E]): Action[JsValue] =
    Action.async(parse.json)(r => r.body.validate[E] match {
      case JsSuccess(e, _) => f.apply(e).map(r => Ok(Json.toJson(r)))
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
