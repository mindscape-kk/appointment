package co.mscp.appointment.service.resource.impl

import co.mscp.appointment.service.authentication.AuthenticationService
import co.mscp.appointment.service.resource.{Resource, ResourceService}
import javax.inject.{Inject, Singleton}
import co.mscp.foundation.common.error.ServiceError;

@Singleton
class ResourceServiceImpl @Inject() (dao: ResourceDao,auth: AuthenticationService)
  extends  ResourceService {

  override def create(token: String,institute: String,resource: Resource): Resource = {

    if(true) {

      throw new ServiceError(ServiceError.Server(),"test",null)

    }

    //TODO: support auth auth.getUser(token) and validate user rights

    dao.create(resource)

  }
}
