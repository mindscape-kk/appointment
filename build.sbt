name := "Medicom Appointment Service"

version := "0.1"

scalaVersion := "2.12.7"

lazy val baseFoundation = project in file("base-foundation")

lazy val common = project in file("common")

val dockerinfo = taskKey[Unit]("Docker Task")

lazy val backend = (project in file("backend"))
  .enablePlugins(PlayScala)
  .enablePlugins(DockerPlugin)
  .settings(
    dockerinfo := {
      val name = (packageName in Docker).value
      val path = s"${dockerRepository.value.get}/$name"
      println(s"[VAL] version: ${version.value}")
      println(s"[VAL] dockerImageName: $path")
    }
  )
  .dependsOn(common, baseFoundation)

lazy val client = project in file("jvm-client")

lazy val spec = (project in file("spec"))
  .dependsOn(common, baseFoundation)

