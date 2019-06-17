package co.mscp.appointment.client

import co.mscp.appointment.entity.Resource
import co.mscp.mmk2.collection.FluentMap
import co.mscp.mmk2.net.CrudClient

class AppointmentClient(host: String, institute: String, token: String) {

  private val param = FluentMap.of("token", token)

  private val resource =
    new CrudClient[Resource](s"$host/v1/$institute/resource", classOf[Resource])

  def createResource(r: Resource): Resource = resource.create(r, param)

}