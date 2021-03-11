/* Copyright (c) 2020 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License,
 * attached with Common Clause Condition 1.0, found in the LICENSES directory.
 */

package com.vesoft.nebula.exchange.reader

import com.vesoft.nebula.exchange.config.{HiveSourceConfigEntry, MySQLSourceConfigEntry}

import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
  * ServerBaseReader is the abstract class of
  * It include a spark session and a sentence which will sent to service.
  * @param session
  * @param sentence
  */
abstract class ServerBaseReader(override val session: SparkSession, val sentence: String)
    extends Reader {

  override def close(): Unit = {
    session.close()
  }
}

/**
  * HiveReader extends the @{link ServerBaseReader}.
  * The HiveReader reading data from Apache Hive via sentence.
  * @param session
  * @param hiveConfig
  */
class HiveReader(override val session: SparkSession, hiveConfig: HiveSourceConfigEntry)
    extends ServerBaseReader(session, hiveConfig.sentence) {
  override def read(): DataFrame = {
    session.sql(sentence)
  }
}

/**
  * The MySQLReader extends the ServerBaseReader.
  * The MySQLReader reading data from MySQL via sentence.
  *
  * @param session
  * @param mysqlConfig
  */
class MySQLReader(override val session: SparkSession, mysqlConfig: MySQLSourceConfigEntry)
    extends ServerBaseReader(session, mysqlConfig.sentence) {
  override def read(): DataFrame = {
    val url =
      s"jdbc:mysql://${mysqlConfig.host}:${mysqlConfig.port}/${mysqlConfig.database}?useUnicode=true&characterEncoding=utf-8"
    session.read
      .format("jdbc")
      .option("url", url)
      .option("dbtable", mysqlConfig.table)
      .option("user", mysqlConfig.user)
      .option("password", mysqlConfig.password)
      .load()
  }
}
