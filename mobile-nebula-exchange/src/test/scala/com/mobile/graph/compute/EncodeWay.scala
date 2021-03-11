/* Copyright (c) 2020 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License,
 * attached with Common Clause Condition 1.0, found in the LICENSES directory.
 */

package scala.com.mobile.graph.compute

import com.mobile.graph.compute.utils.EncodeConstants
import org.apache.spark.sql.{DataFrame, Encoders, Row, SparkSession}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, dense_rank}
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}

class EncodeWay {
  // 通过zipWithUniqueId编码
  def encode(spark: SparkSession): Unit = {
    val dataframe: DataFrame = null;
    val srcIdCol: String     = "src"
    val dstIdCol: String     = "dst"

    val idDF =
      dataframe
        .select(col(srcIdCol))
        .union(dataframe.select(col(dstIdCol)))
        .distinct()

    val idDFWithIndex = idDF
      .map(row => (row.get(0).toString))(Encoders.STRING)
      .rdd
      .repartition(1)
      .zipWithUniqueId()
      .map(row => Row(row._1, row._2))

    val schema = StructType(
      List(
        StructField(EncodeConstants.originalIdCol, StringType, nullable = false),
        StructField(EncodeConstants.encodeIdCol, LongType, nullable = true)
      ))
    val encodeId = spark.createDataFrame(idDFWithIndex, schema)
  }

  // 通过dense_rank编码
  def encode1(spark: SparkSession): Unit = {
    val dataframe: DataFrame = null;
    val srcIdCol: String     = "src"
    val dstIdCol: String     = "dst"

    val idDF =
      dataframe
        .select(col(srcIdCol).as(EncodeConstants.originalIdCol))
        .union(dataframe.select(col(dstIdCol).as(EncodeConstants.originalIdCol)))
        .distinct()
    val encodeId = idDF.withColumn(EncodeConstants.encodeIdCol,
                                   dense_rank().over(Window.orderBy(EncodeConstants.originalIdCol)))
  }
}
