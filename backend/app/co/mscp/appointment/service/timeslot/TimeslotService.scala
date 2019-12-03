package co.mscp.appointment.service.timeslot

import co.mscp.appointment.entity.Timeslot

import scala.concurrent.Future

trait TimeslotService {
  def create(token: String, institute: String, timeslot: Timeslot): Future[Timeslot]

  def update(token: String, institute: String,id : String, timeslot: Timeslot): Future[Timeslot]

  def get(token: String, institute: String, id: String): Future[Option[Timeslot]]

  def delete(token: String, institute: String, id: String): Future[Timeslot]
}