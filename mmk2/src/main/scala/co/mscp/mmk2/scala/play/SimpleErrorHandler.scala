package co.mscp.mmk2.scala.play

import java.util.concurrent.CompletionException

import co.mscp.mmk2.net.ServiceError
import co.mscp.mmk2.scala.logging.Log
import play.api.http.{HttpErrorHandler, MimeTypes}
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}
import play.mvc.Http

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random
import scala.util.matching.Regex


class SimpleErrorHandler extends HttpErrorHandler with Log {

  override def onClientError(request: RequestHeader, statusCode: Int,
    message: String): Future[Result] =
  {
    error(s"Client Error: $message, Code: $statusCode, Request: $request\n")
    toResult(statusCode, ServiceError.clientError(message))
  }


  override def onServerError(request: RequestHeader, exception: Throwable):
  Future[Result] = exception match {
    case _: CompletionException => onServerError(request, exception.getCause)

    case e: ServiceError =>
      error(e)
      toResult(Http.Status.BAD_REQUEST, e)

    case _ =>
      val message = exception.getMessage
      val tracker = Random.nextInt()
      alert(s"Internal Server Error:$message, Tracker: $tracker")
      toResult(Http.Status.INTERNAL_SERVER_ERROR,
        ServiceError.internal(exception))
  }


  def toResult(code: Int, e: ServiceError): Future[Result] =
    Future(Status(code)(e.getData.toJson)
      .as(MimeTypes.JSON)
      .withHeaders(("Access-Control-Allow-Origin", "*")))

}

