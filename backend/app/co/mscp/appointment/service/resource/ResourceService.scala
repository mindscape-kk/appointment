package co.mscp.appointment.service.resource

import co.mscp.appointment.entity.Resource

import scala.concurrent.Future

trait ResourceService {
  def create(token: String, institute: String, resource: Resource): Future[Resource]

  def update(token: String, institute: String,id : String, resource: Resource): Future[Resource]

  def get(token: String, institute: String, id: String): Future[Option[Resource]]

  def delete(token: String, institute: String, id: String): Future[Resource]
}