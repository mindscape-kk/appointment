package co.mscp.appointment.service.resource.impl

import co.mscp.appointment.entity.Resource
import co.mscp.appointment.service.authentication.{AuthenticationService, UserRole}
import co.mscp.appointment.service.resource.ResourceService
import co.mscp.mmk2.net.{CrudAction, ServiceError}
import javax.inject.{Inject, Singleton}

import scala.concurrent.Future

@Singleton
class ResourceServiceImpl @Inject()(dao: ResourceDao, auth: AuthenticationService)
  extends ResourceService
{
  private val cls = classOf[Resource]


  override def create(token: String, institute: String, resource: Resource): Future[Resource] = {
    val resolvedInstitute = auth.resolveInstitute(institute, token, CrudAction.CREATE, cls)

    if(auth.getUserRole(resolvedInstitute, token) != UserRole.STAFF)
      throw ServiceError.badAuthorization(CrudAction.CREATE, cls)

    dao.create(resource)
  }
}
