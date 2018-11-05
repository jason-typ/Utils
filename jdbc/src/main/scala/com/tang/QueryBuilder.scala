package com.tang

import java.sql.{DriverManager, ResultSet, Statement}

import scala.collection.mutable.ListBuffer

trait QueryBuilder {
  val config = Config
  val conSt = s"jdbc:postgresql://${config.dbHost}/${config.dbName}"

  protected def query(query: String) = {
    val conn = DriverManager.getConnection(conSt, config.dbUser, config.dbPass)
    try {
      val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
      val rs = statement.executeQuery(query)
      convertToList(rs)
    } finally {
      conn.close()
    }
  }

  protected def update(update: String) = {
    val conn = DriverManager.getConnection(conSt, config.dbUser, config.dbPass)
    try {
      val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
      statement.executeUpdate(update)
    } finally {
      conn.close()
    }
  }

  protected def insertAndGetKey(insert: String) = {
    val conn = DriverManager.getConnection(conSt, config.dbUser, config.dbPass)
    try {
      val statement = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
      statement.executeUpdate()
      val resultSet = statement.getGeneratedKeys
      if (resultSet.next()) {
        resultSet.getInt(1)
      } else 0
    } finally {
      conn.close()
    }
  }

  private def convertToList(res: ResultSet) = {
    val result = ListBuffer[List[String]]()
    while (res.next()) {
      val size = res.getMetaData.getColumnCount
      val arr = ListBuffer[String]()
      for (i <- 0 until size) {
        arr += res.getString(i + 1)
      }
      result += arr.toList
    }
    result.toList
  }
}