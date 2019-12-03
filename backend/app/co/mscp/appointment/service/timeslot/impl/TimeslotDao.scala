package co.mscp.appointment.service.timeslot.impl

import co.mscp.appointment.entity.Timeslot

import scala.concurrent.Future

trait TimeslotDao {
  def create(timeslot: Timeslot): Future[Timeslot]

  def update(timeslot: Timeslot): Future[Timeslot]

  def delete(timeslot: Timeslot): Future[Timeslot]

  def get(id: String): Future[Option[Timeslot]]

}
