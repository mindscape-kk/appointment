package co.mscp.mmk2.scala

import java.net.URL

import co.mscp.mmk2.docker.ContainerNetwork


case class DockerParams(label: String, dir: URL, flag: String,
  portMap: Map[Int, Int], env: Seq[String])
{
  def this(label: String, dir: URL, flag: String, portMap: Map[Int, Int]) =
    this(label, dir, flag, portMap, Seq())
}


object DockerUtil {
  import collection.JavaConverters._

  def insertLogSeparator(): Unit =
    co.mscp.mmk2.docker.DockerUtil.insertLogSeparator()

  def waitForContainerWithLabel(p: DockerParams): Unit =
    co.mscp.mmk2.docker.DockerUtil.waitForContainerWithLabel(p.label, p.flag)

  def build(p: DockerParams): String =
    co.mscp.mmk2.docker.DockerUtil.build(p.dir)

  def run(id: String, p: DockerParams): String =
    co.mscp.mmk2.docker.DockerUtil.run(
      p.label,
      id,
      p.portMap
        .map(pair => (int2Integer(pair._1), int2Integer(pair._2)))
        .asJava,
      ContainerNetwork.bridge(),
      p.env:_*)

  def isRunning(p: DockerParams): Boolean =
    co.mscp.mmk2.docker.DockerUtil.isRunning(p.label)

  def stop(p: DockerParams): Unit = co.mscp.mmk2.docker.DockerUtil.stop(p.label)
}