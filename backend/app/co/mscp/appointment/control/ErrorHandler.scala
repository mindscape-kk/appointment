package co.mscp.appointment.control

import play.api.http.HttpErrorHandler
import play.api.mvc.{RequestHeader, Result}

import scala.concurrent.Future

class ErrorHandler extends HttpErrorHandler{
  override def onClientError(request: RequestHeader, statusCode: Int,
    message: String): Future[Result] = super.onClientError(request, statusCode, message)

  override def onServerError(request: RequestHeader,
    exception: Throwable): Future[Result] = super.onServerError(request, exception)
}
