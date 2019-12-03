package co.mscp.appointment.service.timeslot.pattern

import co.mscp.appointment.entity.TimeslotPattern

import scala.concurrent.Future

trait TimeslotPatternService {
  def create(token: String, institute: String, pattern: TimeslotPattern): Future[TimeslotPattern]

  def update(token: String, institute: String,id : String, pattern: TimeslotPattern): Future[TimeslotPattern]

  def get(token: String, institute: String, id: String): Future[Option[TimeslotPattern]]

  def delete(token: String, institute: String, id: String): Future[TimeslotPattern]
}