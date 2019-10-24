import com.typesafe.sbt.packager.docker.{DockerChmodType, DockerPermissionStrategy}

name := "Medicom Appointment Backend"
organization := "co.mscp"

packageName in Docker := "appointment"
dockerBaseImage := "openjdk:13"
dockerRepository := Some("docker.mscp.co/medicom")
dockerExposedPorts := Seq(9000)
dockerChmodType := DockerChmodType.UserGroupWriteExecute
dockerPermissionStrategy := DockerPermissionStrategy.Run

// Guice
libraryDependencies += guice

// Json
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.0"

// Database
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "3.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0",
  "org.postgresql" % "postgresql" % "42.2.5",
  "com.github.tminglei" %% "slick-pg" % "0.17.2",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.15.3")