package co.mscp.appointment.service.resource.impl

import co.mscp.appointment.entity.Resource
import co.mscp.appointment.service.authentication.{AuthenticationService, UserRole}
import co.mscp.appointment.service.resource.ResourceService
import co.mscp.mmk2.net.{CrudAction, ServiceError}
import javax.inject.{Inject, Singleton}
import co.mscp.mmk2.scala.logging.Log

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ResourceServiceImpl @Inject()(dao: ResourceDao, auth: AuthenticationService)
                                   (implicit ec: ExecutionContext)
  extends ResourceService with Log
{
  private val cls = classOf[Resource]


  override def create(token: String, institute: String, resource: Resource): Future[Resource] = {
    val resolvedInstitute = auth.resolveInstitute(institute, token, CrudAction.CREATE, cls)

    if(auth.getUserRole(resolvedInstitute, token) != UserRole.STAFF)
      throw ServiceError.badAuthorization(CrudAction.CREATE, cls)

    dao.create(resource)
  }

  override def update(token: String, institute: String, id: String, resource: Resource): Future[Resource] = {
    val resolvedInstitute = auth.resolveInstitute(institute, token, CrudAction.UPDATE, cls)

    if(auth.getUserRole(resolvedInstitute, token) != UserRole.STAFF)
      throw ServiceError.badAuthorization(CrudAction.UPDATE, cls)

    if(id == null)
      throw ServiceError.badId(cls,null)

    get(token,institute,id).flatMap(r =>
      if(r.isEmpty)
        throw ServiceError.badId(cls,id)
      else
        dao.update(resource.withId(id)))
  }

  override def delete(token: String, institute: String, id: String): Future[Resource] = {
    val resolvedInstitute = auth.resolveInstitute(institute, token, CrudAction.DELETE, cls)

    if(auth.getUserRole(resolvedInstitute, token) != UserRole.STAFF)
      throw ServiceError.badAuthorization(CrudAction.DELETE, cls)

    if(id == null || id.isEmpty)
      throw ServiceError.badId(cls, null)

    get(token,institute,id).flatMap(r =>
      if(r.isEmpty)
        throw ServiceError.badId(cls,id)
      else
        dao.delete(r.get))
  }

  override def get(token: String, institute: String, id: String): Future[Option[Resource]]  = {
    val resolvedInstitute = auth.resolveInstitute(institute, token, CrudAction.READ, cls)

    if(auth.getUserRole(resolvedInstitute, token) != UserRole.STAFF)
      throw ServiceError.badAuthorization(CrudAction.READ, cls)

    dao.get(id)
  }
}
