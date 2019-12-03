package co.mscp.appointment.service.timeslot.pattern.impl.postgre

import java.time.{LocalDate, LocalDateTime, LocalTime}

import co.mscp.appointment.entity.TimeslotPattern
import co.mscp.appointment.service.timeslot.pattern.impl.TimeslotPatternDao
import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig, HasDatabaseConfigProvider}
import util.MSCPPostgresProfile

import scala.concurrent.{ExecutionContext, Future}

trait TimeslotComponent {
  self: HasDatabaseConfig[MSCPPostgresProfile] =>

  import profile.api._

  class TimeslotPatternTable(tag: Tag) extends Table[TimeslotPattern](tag, "TIMESLOT_PATTERN") {
    /** The ID column, which is the primary key */
    def id = column[String]("ID", O.PrimaryKey,O.AutoInc)

    def startDate = column[LocalDate]("START_DATE")

    def endDate = column[LocalDate]("END_DATE")

    def startTime = column[LocalTime]("START_TIME")

    def repeatPerDay = column[Int]("REPEAT_PER_DAY")

    def repeatOnDays = column[String]("REPEAT_ON_DAYS")

    def duration = column[Int]("DURATION")

    def * = (id?, startDate, endDate, startTime,repeatPerDay,repeatOnDays,duration) <> ((TimeslotPattern.apply _).tupled, TimeslotPattern.unapply _)
  }

}



class PostgreTimeslotDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends TimeslotComponent with TimeslotPatternDao with HasDatabaseConfigProvider[MSCPPostgresProfile] {

  import profile.api._

  val timeslotPatterns :TableQuery[TimeslotPatternTable] = TableQuery[TimeslotPatternTable]

  val insertQuery = timeslotPatterns returning timeslotPatterns.map(_.id) into ((timeslotPattern, id) => timeslotPattern.copy(id = Some(id)))



  /** Insert a new timeslotPattern */
  def insert(timeslotPattern: TimeslotPattern): Future[TimeslotPattern] =
    db.run(insertQuery += timeslotPattern)

  /** Update given timeslotPattern */
  def update(timeslotPattern: TimeslotPattern): Future[TimeslotPattern] =
    db.run(timeslotPatterns.filter(_.id === timeslotPattern.id).update(timeslotPattern))
      .map(numOfRecords => if (numOfRecords==1) timeslotPattern else null)

  /** delete given timeslotPattern */
  def delete(timeslotPattern: TimeslotPattern): Future[TimeslotPattern] =
    db.run(timeslotPatterns.filter(_.id === timeslotPattern.id).delete
      .map(numOfRecords => if (numOfRecords==1) timeslotPattern else null))


  /** get  timeslotPattern by id */
  def get(id: String): Future[Option[TimeslotPattern]] =
    db.run(timeslotPatterns.filter(_.id === id).result.headOption)


  /** Insert new timeslotPattern */
  def insert(timeslotPattern: Seq[TimeslotPattern]): Future[Unit] =
    db.run(this.timeslotPatterns ++= timeslotPattern).map(_ => ())

  override def create(resource: TimeslotPattern): Future[TimeslotPattern] = insert(resource )

}
