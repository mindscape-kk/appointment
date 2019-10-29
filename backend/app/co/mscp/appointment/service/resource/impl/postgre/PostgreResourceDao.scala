package co.mscp.appointment.service.resource.impl.postgre

import co.mscp.appointment.entity.Resource
import co.mscp.appointment.service.resource.impl.ResourceDao
import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig, HasDatabaseConfigProvider}
import play.api.libs.json.JsValue
import util.MSCPPostgresProfile

import scala.concurrent.{ExecutionContext, Future}

trait ResourcesComponent {
  self: HasDatabaseConfig[MSCPPostgresProfile] =>

  import profile.api._

  class Resources(tag: Tag) extends Table[Resource](tag, "RESOURCE") {
    /** The ID column, which is the primary key */
    def id = column[String]("ID", O.PrimaryKey,O.AutoInc)

    /** The type column */
    def `type` = column[String]("TYPE")

    /** The name column */
    def name = column[String]("NAME")

    /** The description column */
    def description = column[String]("DESCRIPTION")

    /** The user defined properties column */
    def userDefinedProperties = column[JsValue]("USER_DEFINED_PROPERTIES")

    def * = (id?, `type`, name, description?, userDefinedProperties?) <> ((Resource.apply _).tupled, Resource.unapply _)
  }

}



class PostgreResourceDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends ResourcesComponent with ResourceDao with HasDatabaseConfigProvider[MSCPPostgresProfile] {

  import profile.api._

  val resources :TableQuery[Resources] = TableQuery[Resources]

  val insertQuery = resources returning resources.map(_.id) into ((resource, id) => resource.copy(id = Some(id)))



  /** Insert a new resource */
  def insert(resource: Resource): Future[Resource] =
    db.run(insertQuery += resource)

  /** Update given resource */
  def update(resource: Resource): Future[Resource] =
    db.run(resources.filter(_.id === resource.id).update(resource))
      .map(numOfRecords => if (numOfRecords==1) resource else null)


  /** get  resource by id */
  def get(id: String): Future[Option[Resource]] =
    db.run(resources.filter(_.id === id).result.headOption)


  /** Insert new resource */
  def insert(resources: Seq[Resource]): Future[Unit] =
    db.run(this.resources ++= resources).map(_ => ())

  override def create(resource: Resource): Future[Resource] = insert(resource )

}
