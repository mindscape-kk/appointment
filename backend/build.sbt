name := "Medicom Appointment Backend"
organization := "co.mscp"

libraryDependencies += guice
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.0"
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "3.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0"
)

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
libraryDependencies += "com.github.tminglei" %% "slick-pg" % "0.17.2"
libraryDependencies += "com.github.tminglei" %% "slick-pg_play-json" % "0.15.3"