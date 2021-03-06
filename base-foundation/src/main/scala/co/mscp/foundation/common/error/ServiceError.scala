package co.mscp.foundation.common.error

import java.io.{BufferedOutputStream, ByteArrayOutputStream, PrintWriter}
import java.nio.charset.StandardCharsets

import com.fasterxml.jackson.databind.JsonSerializable
import play.api.libs.json.JsObject


object ServiceError {
  abstract class ErrorCode {
    override def toString: String = getClass.getSimpleName.toUpperCase
  }
  case class Server extends ErrorCode
  case class Client extends ErrorCode
  case class Validation extends ErrorCode
}


class ServiceError(code: ServiceErrorCode, message: String, e: Exception)
  extends RuntimeException(code + ": " + message, e) with JsonSerializable
{
  def this(code: ServiceErrorCode, message: String) = this(code, message, null)
  def toJson(withStackTrace: boolean): JsObject = Json.obj(
      "code" -> code.toString,
      "message" -> message
    ) ++ if(withStackTrace) Json.obj(
      "stackTrace" -> {
        val os = new ByteArrayOutputStream()
        printStackTrace(new PrintWriter(os))
        os.toString(StandardCharsets.UTF_8)
      })
    else Json.obj()

  override def toJson: JsObject = toJson(false)

}


class InternalServerError(message: String, e: Exception)
  extends ServiceError(ServiceError.Server, message, e)
{
  def this(message: String, null) = this(code, message, null)
  def this(e: Exception) = this(e.getMessage, e)
}


class ClientError(message: String)
  extends ServiceError(ServiceError.Client, message, null)


class ValidationError(typeName: String, propertyPath: String, was: String, excpected: String)
  extends ServiceError(ServiceError.Validation,
    s"$typeName.$propertyPath was: $was, expected: $excpected")
{
  def this(typeName: Class, propertyPath: String, was: String, expected: String)
    = this(typeName.getSimpleName, propertyPath, was, expected)

  override def toJson(withStackTrace: boolean): JsObject =
    super.toJson(withStackTrace) ++ Json.obj(
      "typeName" -> typeName,
      "propertyPath" -> propertyPath,
      "was" -> was,
      "expected" -> excpected
    )
}