package co.mscp.appointment.control

import java.util.concurrent.CompletionException

import co.mscp.mmk2.net.ServiceError
import play.api.http.HttpErrorHandler
import play.api.mvc.{RequestHeader, Result, Results}

import scala.concurrent.Future

class ErrorHandler extends HttpErrorHandler {
  override def onClientError(request: RequestHeader, statusCode: Int,
    message: String): Future[Result] = null

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] =
    exception match {
      case _: CompletionException => onServerError(request, exception.getCause)
      case e: ServiceError => Future(toResult(e))
      case _ => onServerError(request, ServiceError.internal(exception.getMessage))
    }

  def toResult(e: ServiceError): Result = ???

}

