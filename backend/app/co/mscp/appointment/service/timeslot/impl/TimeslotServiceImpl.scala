package co.mscp.appointment.service.timeslot.impl

import co.mscp.appointment.entity.Timeslot
import co.mscp.appointment.service.authentication.{AuthenticationService, UserRole}
import co.mscp.appointment.service.timeslot.TimeslotService
import co.mscp.mmk2.net.{CrudAction, ServiceError}
import co.mscp.mmk2.scala.logging.Log
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TimeslotServiceImpl @Inject()(dao: TimeslotDao, auth: AuthenticationService)
                                   (implicit ec: ExecutionContext)
  extends TimeslotService with Log
{
  private val cls = classOf[Timeslot]


  override def create(token: String, institute: String,resourceId: String , timeslot: Timeslot): Future[Timeslot] = {
    val resolvedInstitute = auth.resolveInstitute(institute, token, CrudAction.CREATE, cls)

    if(auth.getUserRole(resolvedInstitute, token) != UserRole.STAFF)
      throw ServiceError.badAuthorization(CrudAction.CREATE, cls)

    dao.create(timeslot.withResourceId(resourceId))
  }

  override def update(token: String, institute: String, id: String, timeslot: Timeslot): Future[Timeslot] = {
    val resolvedInstitute = auth.resolveInstitute(institute, token, CrudAction.UPDATE, cls)

    if(auth.getUserRole(resolvedInstitute, token) != UserRole.STAFF)
      throw ServiceError.badAuthorization(CrudAction.UPDATE, cls)

    if(id == null)
      throw ServiceError.badId(cls,null)

    get(token,institute,id).flatMap(r =>
      if(r.isEmpty)
        throw ServiceError.badId(cls,id)
      else
        dao.update(timeslot.withId(id)))
  }

  override def delete(token: String, institute: String, id: String): Future[Timeslot] = {
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

  override def get(token: String, institute: String, id: String): Future[Option[Timeslot]]  = {
    val resolvedInstitute = auth.resolveInstitute(institute, token, CrudAction.READ, cls)

    if(auth.getUserRole(resolvedInstitute, token) != UserRole.STAFF)
      throw ServiceError.badAuthorization(CrudAction.READ, cls)

    dao.get(id)
  }
}
