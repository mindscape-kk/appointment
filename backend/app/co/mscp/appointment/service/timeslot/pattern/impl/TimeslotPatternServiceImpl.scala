package co.mscp.appointment.service.timeslot.pattern.impl

import co.mscp.appointment.entity.TimeslotPattern
import co.mscp.appointment.service.authentication.{AuthenticationService, UserRole}
import co.mscp.appointment.service.timeslot.pattern.TimeslotPatternService
import co.mscp.mmk2.net.{CrudAction, ServiceError}
import co.mscp.mmk2.scala.logging.Log
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TimeslotPatternServiceImpl @Inject()(dao: TimeslotPatternDao, auth: AuthenticationService)
                                          (implicit ec: ExecutionContext)
  extends TimeslotPatternService with Log
{
  private val cls = classOf[TimeslotPattern]


  override def create(token: String, institute: String,resourceId: String , pattern: TimeslotPattern): Future[TimeslotPattern] = {
    val resolvedInstitute = auth.resolveInstitute(institute, token, CrudAction.CREATE, cls)

    if(auth.getUserRole(resolvedInstitute, token) != UserRole.STAFF)
      throw ServiceError.badAuthorization(CrudAction.CREATE, cls)

    dao.create(pattern.withResourceId(resourceId))
  }

  override def update(token: String, institute: String, id: String, pattern: TimeslotPattern): Future[TimeslotPattern] = {
    val resolvedInstitute = auth.resolveInstitute(institute, token, CrudAction.UPDATE, cls)

    if(auth.getUserRole(resolvedInstitute, token) != UserRole.STAFF)
      throw ServiceError.badAuthorization(CrudAction.UPDATE, cls)

    if(id == null)
      throw ServiceError.badId(cls,null)

    get(token,institute,id).flatMap(r =>
      if(r.isEmpty)
        throw ServiceError.badId(cls,id)
      else
        dao.update(pattern.withId(id)))
  }

  override def delete(token: String, institute: String, id: String): Future[TimeslotPattern] = {
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

  override def get(token: String, institute: String, id: String): Future[Option[TimeslotPattern]]  = {
    val resolvedInstitute = auth.resolveInstitute(institute, token, CrudAction.READ, cls)

    if(auth.getUserRole(resolvedInstitute, token) != UserRole.STAFF)
      throw ServiceError.badAuthorization(CrudAction.READ, cls)

    dao.get(id)
  }
}
