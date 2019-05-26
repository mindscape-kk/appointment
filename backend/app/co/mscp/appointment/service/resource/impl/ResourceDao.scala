package co.mscp.appointment.service.resource.impl

import co.mscp.appointment.service.resource.Resource

trait ResourceDao {
  def create(resource: Resource): Resource

}
