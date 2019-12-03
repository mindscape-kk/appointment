package co.mscp.appointment.service.timeslot.pattern.impl

import co.mscp.appointment.entity.TimeslotPattern

import scala.concurrent.Future

trait TimeslotPatternDao {
  def create(pattern: TimeslotPattern): Future[TimeslotPattern]

  def update(pattern: TimeslotPattern): Future[TimeslotPattern]

  def delete(pattern: TimeslotPattern): Future[TimeslotPattern]

  def get(id: String): Future[Option[TimeslotPattern]]

}
