package co.mscp.appointment.service.resource.impl

import co.mscp.appointment.service.resource.Resource

import scala.concurrent.Future

trait ResourceDao {
  def create(resource: Resource): Future[Resource]

}
