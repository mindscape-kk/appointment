package co.mscp.appointment.service.resource

import scala.concurrent.Future

trait ResourceService {
  def create(token: String, institute: String, resource: Resource): Future[Resource]
}