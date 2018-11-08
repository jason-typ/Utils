ThisBuild / scalaVersion := "2.12.7"
ThisBuild / organization := "com.akka-learning"

// val akka_config = "com.typesafe" % "config" % "1.3.2"
val akka = "com.typesafe.akka" %% "akka-actor" % "2.5.17"
val slf4j = "com.typesafe.akka" %% "akka-slf4j" % "2.5.17"
val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"


lazy val loggingPrj = (project in file("."))
  .settings(
    name := "akka_router",
    scalaVersion := "2.12.7",
    version := "0.1",
    libraryDependencies ++= Seq(akka, slf4j, logback)
  )