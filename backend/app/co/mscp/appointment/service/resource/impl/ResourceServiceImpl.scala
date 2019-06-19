package co.mscp.appointment.service.resource.impl

import co.mscp.appointment.service.authentication.AuthenticationService
import co.mscp.appointment.service.resource.ResourceService
import javax.inject.{Inject, Singleton}

import co.mscp.appointment.entity.Resource
import co.mscp.mmk2.net.ServiceError

import scala.concurrent.Future

@Singleton
class ResourceServiceImpl @Inject()(dao: ResourceDao, auth: AuthenticationService)
  extends ResourceService {

  override def create(token: String, institute: String, resource: Resource): Future[Resource] = {

    // TODO validate institute
    // TODO if institute is "my" retrieve actual institute by token

    if (false) {
      throw ServiceError.internal("test")
    }

    //TODO: support auth auth.getUser(token) and validate user rights
    //TODO: resource should belong to institute

    dao.create(resource)
  }
}
