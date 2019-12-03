package co.mscp.appointment.entity

import java.time.LocalDateTime

import play.api.libs.json.{Json, Reads, Writes}


case class Timeslot(
                     id: Option[String],
                     resourceId: String,
                     begin: LocalDateTime,
                     duration: Int,
                     assignee: Option[String],
                     confirmed: Boolean,
                     cancelRequest: Boolean) {
  def this( resourceId: String,begin: LocalDateTime,
            duration: Int,
            assignee: String,
            confirmed: Boolean,
            cancelRequest: Boolean) =
    this(None,resourceId, begin, duration, Some(assignee), confirmed,cancelRequest)

    def withId(id: String): Timeslot = new Timeslot(Some(id),resourceId, begin, duration, assignee, confirmed,cancelRequest);
}


object Timeslot {
  implicit val writes: Writes[Timeslot] = Json.writes[Timeslot]
  implicit val reads: Reads[Timeslot] = Json.reads[Timeslot]

}

