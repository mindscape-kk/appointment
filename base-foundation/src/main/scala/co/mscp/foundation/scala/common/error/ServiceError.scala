package co.mscp.foundation.common.error

import java.io.{ByteArrayOutputStream, PrintWriter}
import java.nio.charset.StandardCharsets

import co.mscp.foundation.common.error.ServiceError.ErrorCode
import co.mscp.net.HttpErrorStatus
import play.api.libs.json.{JsObject, Json}


object ServiceError {
  abstract class ErrorCode {
    override def toString: String = getClass.getSimpleName.toUpperCase
  }
  case class Server() extends ErrorCode
  case class Client() extends ErrorCode
  case class Validation() extends ErrorCode
}

w
class ServiceError(code: ServiceError.ErrorCode, message: String, e: Exception)
  extends RuntimeException(code + ": " + message, e)
{
  def this(code: ErrorCode, message: String) = this(code, message, null)
  def toJson(withStackTrace: Boolean): JsObject = Json.obj(
      "code" -> code.toString,
      "message" -> message
    ) ++ (if(withStackTrace) Json.obj(
      "stackTrace" -> {
        val os = new ByteArrayOutputStream()
        printStackTrace(new PrintWriter(os))
        os.toString(StandardCharsets.UTF_8.name())
      })
    else Json.obj())

  def toJson: JsObject = toJson(false)

}



class InternalServerError(message: String, e: Exception)
  extends ServiceError(ServiceError.Server(), message, e)
{
  def this(message: String) = this( message, null)
  def this(e: Exception) = this(e.getMessage, e)
}


class ClientError(code: HttpErrorStatus, message: String)
  extends ServiceError(ServiceError.Client(), message, null)
{
  def this(code: HttpErrorStatus) = this(code, code.reason());
}


class ValidationError(typeName: String, propertyPath: String, was: String, excpected: String)
  extends ServiceError(ServiceError.Validation(),
    s"$typeName.$propertyPath was: $was, expected: $excpected")
{
  def this(typeName: Class[Any], propertyPath: String, was: String, expected: String)
    = this(typeName.getSimpleName, propertyPath, was, expected)

  override def toJson(withStackTrace: Boolean): JsObject =
    super.toJson(withStackTrace) ++ Json.obj(
      "typeName" -> typeName,
      "propertyPath" -> propertyPath,
      "was" -> was,
      "expected" -> excpected
    )
}