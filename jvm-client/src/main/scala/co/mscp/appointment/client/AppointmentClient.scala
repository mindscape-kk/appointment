package co.mscp.appointment.client

import co.mscp.appointment.entity.{Resource, Timeslot}
import co.mscp.mmk2.collection.FluentMap
import co.mscp.mmk2.net.CrudClient

class AppointmentClient(host: String, institute: String, token: String) {
  private val param = FluentMap.of("token", token)

  private val resource =
    new CrudClient[Resource](s"$host/v1/$institute/resource", classOf[Resource])

  private def timeslot (resourceId: String) =
    new CrudClient[Timeslot](s"$host/v1/$institute/resource/$resourceId/timeslot", classOf[Timeslot])

  def createResource(r: Resource): Resource = resource.create(r, param)
  def updateResource(id: String, r: Resource): Resource = resource.update(id,r, param)
  def deleteResource(id: String): Resource = resource.delete(id, param)


  def createTimeslot(resourceId: String, t: Timeslot): Timeslot = timeslot(resourceId).create(t, param)
}