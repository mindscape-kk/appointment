package co.mscp.appointment.service.resource.impl.postgre

import co.mscp.appointment.service.resource.Resource
import co.mscp.appointment.service.resource.impl.ResourceDao

class PostgreResourceDao extends ResourceDao{
  override def create(resource: Resource): Resource = {

    println("TODO:  Postgre DB save")

    resource
  }
}
