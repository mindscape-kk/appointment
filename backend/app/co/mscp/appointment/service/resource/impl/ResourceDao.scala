package co.mscp.appointment.service.resource.impl

import co.mscp.appointment.entity.Resource

import scala.concurrent.Future

trait ResourceDao {
  def create(resource: Resource): Future[Resource]

}
