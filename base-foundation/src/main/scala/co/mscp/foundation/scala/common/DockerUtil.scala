package co.mscp.foundation.scala.common

import java.net.URL

import co.mscp.ContainerNetwork

object DockerUtil {
  def build(dockerDir: URL): String = co.mscp.DockerUtil.build(dockerDir)

  def run(label: String, imageName: String, portMap: Map[Int, Int],
      network: ContainerNetwork, environment: Seq[String]): String =
  {
    import collection.JavaConverters._

    val integerPortMap: Map[Integer, Integer]
      = portMap.map(pair => (int2Integer(pair._1), int2Integer(pair._2)))

    co.mscp.DockerUtil.run(label, imageName, integerPortMap.asJava, network,
      environment:_*)
  }

  def isRunning(label: String): Boolean = co.mscp.DockerUtil.isRunning(label)

  def stop(label: String): Unit = co.mscp.DockerUtil.stop(label)

}
