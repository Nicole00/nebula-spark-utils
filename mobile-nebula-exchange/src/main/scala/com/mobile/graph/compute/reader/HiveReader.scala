/* Copyright (c) 2020 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License,
 * attached with Common Clause Condition 1.0, found in the LICENSES directory.
 */

package com.mobile.graph.compute.reader

import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import com.google.gson.Gson
import com.mobile.graph.compute.HiveConfig
import com.mobile.graph.compute.utils.{EncodeConstants, EncodeDataPath, HdfsUtil}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, dense_rank}

object HiveReader {
  private[this] val LOG = Logger.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    val PROGRAM_NAME = "HiveReader"

    if (args.length < 1) {
      throw new IllegalArgumentException(
        "invalidate parameter, expect for hive connection information.")
    }

    val session = SparkSession
      .builder()
      .master("local")
      .appName(PROGRAM_NAME)
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val sparkConf = new SparkConf()
    sparkConf.registerKryoClasses(Array(classOf[com.facebook.thrift.async.TAsyncClientManager]))

    val gson = new Gson()
    // todo add more check for args
    val hiveConfig = gson.fromJson(args(0), classOf[HiveConfig])

    val hiveConnection = hiveConfig.getConnection
    val sql            = hiveConfig.getSql

    // config hive for sparkSession
    if (hiveConnection == null) {
      LOG.warn("you do not config hive source, so using hive tied with spark.")
    } else {
      sparkConf
        .set("spark.sql.warehouse.dir", hiveConnection.getWaredir)
        .set("javax.jdo.option.ConnectionURL", hiveConnection.getConnectionURL)
        .set("javax.jdo.option.ConnectionDriverName", hiveConnection.getDriverName)
        .set("javax.jdo.option.ConnectionUserName", hiveConnection.getUserName)
        .set("javax.jdo.option.ConnectionPassword", hiveConnection.getPassword)
      session.config(sparkConf)
    }
    session.enableHiveSupport()
    val spark     = session.getOrCreate()
    val dataframe = spark.sql(sql)

    // encode id
    val columns    = hiveConfig.getIdColumns
    val srcIdCol   = columns(0)
    val dstIdCol   = columns(1)
    val hdfsPrefix = hiveConfig.getHdfs

    var weightCol: String = null
    if (columns.length > 2 && columns(2) != null) {
      weightCol = columns(2)
    }

    val idDF = dataframe
      .select(col(srcIdCol).as(EncodeConstants.originalIdCol))
      .union(dataframe.select(col(dstIdCol).as(EncodeConstants.originalIdCol)))
      .distinct()
    val encodeId = idDF.withColumn(
      EncodeConstants.encodeIdCol,
      dense_rank().over(Window.orderBy(EncodeConstants.originalIdCol)) - 1)

    encodeId.write.csv(HdfsUtil.getHdfsPath(hdfsPrefix, EncodeDataPath.encodeIdPath))
    encodeId.cache()

    // replace edge's src id and dst id with encode id
    val srcJoinDF = dataframe
      .join(encodeId)
      .where(col(srcIdCol) === col(EncodeConstants.originalIdCol))
      .drop(col(EncodeConstants.originalIdCol))
      .drop(srcIdCol)
      .withColumnRenamed(EncodeConstants.encodeIdCol, EncodeConstants.encodeSrcCol)
    srcJoinDF.cache()
    val dstJoinDF = srcJoinDF
      .join(encodeId)
      .where(col(dstIdCol) === col(EncodeConstants.originalIdCol))
      .drop(dstIdCol)
      .drop(EncodeConstants.originalIdCol)
      .withColumnRenamed(EncodeConstants.encodeIdCol, EncodeConstants.encodeDstCol)
    val newEdgeDF =
      if (weightCol != null) {
        dstJoinDF.select(EncodeConstants.encodeSrcCol, EncodeConstants.encodeDstCol, weightCol)
      } else {
        dstJoinDF.select(EncodeConstants.encodeSrcCol, EncodeConstants.encodeDstCol)
      }

    // release the memory
    encodeId.unpersist()
    srcJoinDF.unpersist()

    newEdgeDF.write.csv(HdfsUtil.getHdfsPath(hdfsPrefix, EncodeDataPath.encodeEdgePath))
  }
}
