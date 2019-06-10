package co.mscp.medicom.appointment.spec

import co.mscp.{ContainerNetwork, NetworkUtil}
import co.mscp.foundation.scala.common.DockerUtil



object DockerHelper {
  private case class DockerParams(label: String, dir: String,
    portMap: Map[Int, Int])

  private val DB_ENV = Seq("POSTGRES_PASSWORD=medicom")
  private val DB_LOAD_FLAG = "database system is ready to accept connections"
  private val DB_PORT = 6432
  private val POSTGRES_PORT = 5432
  private val BACKEND_LABEL="appointment-backend"
  private val BACKEND_LOAD_FLAG="play.core.server.AkkaHttpServer - Listening for HTTP on"
  private val DB = DockerParams("medicom-db", "/docker/db",
    Map(POSTGRES_PORT -> DB_PORT))


  private val BACKEND = DockerParams("medicom-backend", null,
    Map(9000 -> 9000))


  def getHostAddress: String = NetworkUtil.getHostInet.getHostAddress

  def runBackend(): Unit = if(!DockerUtil.isRunning(BACKEND_LABEL)){
    DockerUtil.run(BACKEND_LABEL, "docker.mscp.co/medicom/appointment:local-test",
      Map(9000 -> 9000), ContainerNetwork.bridge(),
      Seq(s"MEDICOM_DB_URL=jdbc:postgresql://$getHostAddress:$DB_PORT/medicom"))

    co.mscp.DockerUtil.waitForContainerWithLabel(BACKEND_LABEL, BACKEND_LOAD_FLAG)
  }


  def runDocker(p: DockerParams): Unit = {

    if(!co.mscp.DockerUtil.isRunning(p.label)) {
      co.mscp.DockerUtil.insertLogSeparator()

      val dockerFile = classOf[DockerHelper].getResource(p.dir)
      if(dockerFile == null) {
        throw new Error("Docker file could not found at " + p.dir)
      }
      val imageId = co.mscp.DockerUtil.build(dockerFile)

      DockerUtil.run(p.label, imageId, p.portMap, ContainerNetwork.bridge(), DB_ENV)

      co.mscp.DockerUtil.waitForContainerWithLabel(p.label,DB_LOAD_FLAG)
    }
  }


  def stopDocker(p: String): Unit =
    if(DockerUtil.isRunning(p)) {
      DockerUtil.stop(p)
    }


  def runAll(): Unit = {
    runDocker(DB)
    runBackend()
  }


  def stopAll(): Unit = {
    stopDocker(BACKEND_LABEL)
    stopDocker(DB.label)
  }


  def main(args: Array[String]): Unit = runAll() //stopAll()

}


class DockerHelper