package co.mscp.appointment.service.timeslot.impl.postgre

import java.time.LocalDateTime

import co.mscp.appointment.entity.Timeslot
import co.mscp.appointment.service.timeslot.impl.TimeslotDao
import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig, HasDatabaseConfigProvider}
import util.MSCPPostgresProfile

import scala.concurrent.{ExecutionContext, Future}

trait TimeslotComponent {
  self: HasDatabaseConfig[MSCPPostgresProfile] =>

  import profile.api._

  class TimeslotTable(tag: Tag) extends Table[Timeslot](tag, "TIMESLOT") {
    /** The ID column, which is the primary key */
    def id = column[String]("ID", O.PrimaryKey,O.AutoInc)


    def begin = column[LocalDateTime]("BEGIN")


    def duration = column[Int]("DURATION")


    def assignee = column[String]("ASSIGNEE")

    def confirmed = column[Boolean]("CONFIRMED")

    def cancelRequest = column[Boolean]("CANCEL_REQUEST")


    def * = (id?, begin, duration, assignee?, confirmed,cancelRequest) <> ((Timeslot.apply _).tupled, Timeslot.unapply _)
  }

}



class PostgreTimeslotDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends TimeslotComponent with TimeslotDao with HasDatabaseConfigProvider[MSCPPostgresProfile] {

  import profile.api._

  val timeslots :TableQuery[TimeslotTable] = TableQuery[TimeslotTable]

  val insertQuery = timeslots returning timeslots.map(_.id) into ((timeslot, id) => timeslot.copy(id = Some(id)))



  /** Insert a new timeslot */
  def insert(timeslot: Timeslot): Future[Timeslot] =
    db.run(insertQuery += timeslot)

  /** Update given timeslot */
  def update(timeslot: Timeslot): Future[Timeslot] =
    db.run(timeslots.filter(_.id === timeslot.id).update(timeslot))
      .map(numOfRecords => if (numOfRecords==1) timeslot else null)

  /** delete given timeslot */
  def delete(timeslot: Timeslot): Future[Timeslot] =
    db.run(timeslots.filter(_.id === timeslot.id).delete
      .map(numOfRecords => if (numOfRecords==1) timeslot else null))


  /** get  timeslot by id */
  def get(id: String): Future[Option[Timeslot]] =
    db.run(timeslots.filter(_.id === id).result.headOption)


  /** Insert new timeslot */
  def insert(timeslot: Seq[Timeslot]): Future[Unit] =
    db.run(this.timeslots ++= timeslot).map(_ => ())

  override def create(resource: Timeslot): Future[Timeslot] = insert(resource )

}
