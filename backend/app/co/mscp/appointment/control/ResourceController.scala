package co.mscp.appointment.control

import co.mscp.appointment.service.resource.{Resource, ResourceService}
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

class ResourceController @Inject()(cc: ControllerComponents, service: ResourceService)
  extends AbstractController(cc)
{
  def create(institute: String,token: String) = Action { request =>

    println(institute)
    println(token)
    val resource:Resource = new Resource(name = "Shakthi",`type` = "doc",description = null,id = "1232",userDefinedProperties =  Map())
    Ok(Json.toJson(service.create(token,institute,resource)))
  }
}