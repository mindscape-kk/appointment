package co.mscp.appointment.service.resource

trait ResourceService {
  def create(token: String,institute: String,resource: Resource): Resource
}