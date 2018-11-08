ThisBuild / scalaVersion := "2.12.7"
ThisBuild / organization := "com.akka-learning"

// val akka_config = "com.typesafe" % "config" % "1.3.2"
val akka = "com.typesafe.akka" %% "akka-actor" % "2.5.17"

lazy val projectName = (project in file("."))
  .settings(
    name := "akka_router",
    scalaVersion := "2.12.7",
    version := "0.1",
    libraryDependencies += akka
  )