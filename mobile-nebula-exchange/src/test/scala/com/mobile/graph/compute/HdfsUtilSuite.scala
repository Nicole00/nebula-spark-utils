/* Copyright (c) 2020 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License,
 * attached with Common Clause Condition 1.0, found in the LICENSES directory.
 */

package scala.com.mobile.graph.compute

import com.mobile.graph.compute.utils.HdfsUtil

object HdfsUtilSuite {

  def testGetPath(): Unit = {
    val namenode: String = "hdfs://CM-149:9000/"
    val path1: String    = "/temp/"
    val hdfsPath         = HdfsUtil.getHdfsPath(namenode, path1)
    assert(hdfsPath.equals("hdfs://CM-149:9000/temp/"))

    // test wrong path format
    val path2: String = "temp/"
    try {
      HdfsUtil.getHdfsPath(namenode, path2)
    } catch {
      case e: IllegalArgumentException => assert(true)
      case _                           => assert(false)
    }

    // test namenode not end with /
    val newNamenode: String = "hdfs://CM-149:9000"
    val newHdfsPath         = HdfsUtil.getHdfsPath(newNamenode, path1)
    assert(newHdfsPath.equals("hdfs://CM-149:9000/temp/"))
  }

  def main(args: Array[String]): Unit = {
    testGetPath()
  }
}
