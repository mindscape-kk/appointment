package co.mscp.appointment.entity


import java.time.{LocalDate, LocalTime}

import play.api.libs.json.{Json, Reads, Writes}


case class TimeslotPattern(
                            id: Option[String],
                            resourceId: String,
                            startDate: LocalDate,
                            endDate: LocalDate,
                            startTime: LocalTime,
                            repeatPerDay: Int,
                            repeatOnDays: String,
                            duration: Int) {
  def this(  resourceId: String,
             startDate: LocalDate,
            endDate: LocalDate,
            startTime: LocalTime,
            repeatPerDay: Int,
            repeatOnDays: String,
            duration: Int) =
    this(None,resourceId, startDate, endDate, startTime,repeatPerDay,repeatOnDays,duration)

    def withId(id: String): TimeslotPattern = new TimeslotPattern(Some(id),resourceId,  startDate, endDate, startTime,repeatPerDay,repeatOnDays,duration);
    def withResourceId(resourceId: String ): TimeslotPattern = new TimeslotPattern(id,resourceId,  startDate, endDate, startTime,repeatPerDay,repeatOnDays,duration);
}

object TimeslotPattern {
  implicit val writes: Writes[TimeslotPattern] = Json.writes[TimeslotPattern]
  implicit val reads: Reads[TimeslotPattern] = Json.reads[TimeslotPattern]

}


