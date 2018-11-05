name := "jdbc"

version := "0.1"

scalaVersion := "2.12.6"

val typesafeConfig = "com.typesafe" % "config" % "1.3.3"
val postgresql = "org.postgresql" % "postgresql" % "42.2.5"
lazy val root = (project in file("."))
  .settings(
    name := "jdbc-util",
    libraryDependencies ++= Seq(typesafeConfig, postgresql)
  )