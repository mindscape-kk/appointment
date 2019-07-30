package co.mscp.appointment.control

import java.util.concurrent.CompletionException

import co.mscp.mmk2.net.ServiceError
import play.api.http.HttpErrorHandler
import play.api.mvc.{RequestHeader, Result, Results}

import scala.concurrent.Future

object ErrorHandler {
  import java.util.regex.Pattern
  private val JSON_ERROR_PATTERN = Pattern.compile("Error decoding json body:[ \\w\\.]+: (.*)")
}


class ErrorHandler extends HttpErrorHandler {
  import ErrorHandler._

  override def onClientError(request: RequestHeader, statusCode:
  Int,message: String): Future[Result] = {
    // TODO Always log
    /*
    Matcher m = JSON_ERROR_PATTERN.matcher(message);
    if(m.find()) {
        String msg = m.group(1);
        throw new HttpError(HttpErrorStatus.NOT_ACCEPTABLE, msg);
    }
    throw new HttpError(HttpErrorStatus.of(statusCode),
        "Bad invocation of endpoint: " + request.toString());
   */
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] =
    // TODO Always log
    exception match {
      case _: CompletionException => onServerError(request, exception.getCause)
      case e: ServiceError => Future(toResult(e))
      case _ => onServerError(request, ServiceError.internal(exception.getMessage))
    }

  def toResult(e: ServiceError): Result = ???

}

