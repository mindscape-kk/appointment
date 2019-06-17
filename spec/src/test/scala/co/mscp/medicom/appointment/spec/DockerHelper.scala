package co.mscp.medicom.appointment.spec

import java.net.URL

import co.mscp.foundation.scala.common.{DockerParams, DockerUtil}
import co.mscp.mmk2.net.NetworkUtil


object DockerHelper {

  private val DB_PORT = 6451
  private val DB = DockerParams(
    "medicom-db",
    toUrl("/docker/db"),
    "database system is ready to accept connections",
    Map(5432 -> DB_PORT),
    Seq("POSTGRES_PASSWORD=medicom"))

  val BACKEND_PORT = 6452
  private val BACKEND = DockerParams(
    "medicom-backend",
    null,
    "play.core.server.AkkaHttpServer - Listening for HTTP on",
    Map(9000 -> BACKEND_PORT),
    Seq(s"MEDICOM_DB_URL=jdbc:postgresql://$getHostAddress:$DB_PORT/medicom"))


  def getHostAddress: String = NetworkUtil.getHostInet.getHostAddress


  private def toUrl(path: String): URL =
    Option(classOf[DockerHelper].getResource(path))
      .getOrElse(throw new Error("Docker file could not found at " + path))


  def runBackend(): Unit = if(!DockerUtil.isRunning(BACKEND)) {
    DockerUtil.insertLogSeparator()
    DockerUtil.run("docker.mscp.co/medicom/appointment:local-test", BACKEND)
    DockerUtil.waitForContainerWithLabel(BACKEND)
  }


  def runDb(): Unit = if(!DockerUtil.isRunning(DB)) {
    DockerUtil.insertLogSeparator()
    DockerUtil.run(DockerUtil.build(DB), DB)
    DockerUtil.waitForContainerWithLabel(DB)
  }


  def runAll(): Unit = {
    runDb()
    runBackend()
  }


  def stopAll(): Unit = {
    DockerUtil.stop(BACKEND)
    DockerUtil.stop(DB)
  }


  def main(args: Array[String]): Unit = stopAll()
}


class DockerHelper