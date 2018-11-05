package com.tang

import com.typesafe.config.{ConfigFactory}

object Config extends Config {
  val conf = ConfigFactory.load

  val dbHost = conf.getString("host")
  val dbPort = conf.getString("port")
  val dbName = conf.getString("name")
  val dbUser = conf.getString("username")
  val dbPass = conf.getString("password")
}

trait Config {
  val dbHost: String
  val dbPort: String
  val dbName: String
  val dbUser: String
  val dbPass: String
}
