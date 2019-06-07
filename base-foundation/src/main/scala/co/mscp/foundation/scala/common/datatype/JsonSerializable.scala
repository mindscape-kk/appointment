import play.api.libs.json.{JsObject, Json}

trait JsonSerializable {
  def toJson: JsObject
}