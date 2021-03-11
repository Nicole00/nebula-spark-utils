/* Copyright (c) 2020 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License,
 * attached with Common Clause Condition 1.0, found in the LICENSES directory.
 */

package com.mobile.graph.compute.writer

object HiveWriter {

  def main(args: Array[String]): Unit = {
    val a =
      "{\"hdfs\":\"hdfs://192.168.8.149:9000/\",\"connection\":{\"waredir\":\"hdfs://CM-149:9000/user/vesoft/hive/warehouse\",\"connectionURL\":\"jdbc:mysql://192.168.8.149:3306/metastore?createDatabaseIfNotExist=TRUE\",\"driverName\":\"com.mysql.cj.jdbc.Driver\",\"userName\":\"nebula\",\"password\":\"Nebula@123\"},\"sql\":\"select id,name from default.person\"}"
  }
}
