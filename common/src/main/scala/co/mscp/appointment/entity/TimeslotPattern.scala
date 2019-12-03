package co.mscp.appointment.entity


import java.time.{LocalDate, LocalTime}

import play.api.libs.json.{Json, Reads, Writes}


case class TimeslotPattern(
                            id: Option[String],
                            startDate: LocalDate,
                            endDate: LocalDate,
                            startTime: LocalTime,
                            repeatPerDay: Int,
                            repeatOnDays: String,
                            duration: Int) {
  def this( startDate: LocalDate,
            endDate: LocalDate,
            startTime: LocalTime,
            repeatPerDay: Int,
            repeatOnDays: String,
            duration: Int) =
    this(None, startDate, endDate, startTime,repeatPerDay,repeatOnDays,duration)

    def withId(id: String): TimeslotPattern = new TimeslotPattern(Some(id),  startDate, endDate, startTime,repeatPerDay,repeatOnDays,duration);
}

object TimeslotPattern {
  implicit val writes: Writes[TimeslotPattern] = Json.writes[TimeslotPattern]
  implicit val reads: Reads[TimeslotPattern] = Json.reads[TimeslotPattern]

}


