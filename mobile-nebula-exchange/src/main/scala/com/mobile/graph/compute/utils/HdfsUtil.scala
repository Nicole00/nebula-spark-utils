/* Copyright (c) 2020 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License,
 * attached with Common Clause Condition 1.0, found in the LICENSES directory.
 */

package com.mobile.graph.compute.utils

object HdfsUtil {
  def getHdfsPath(namenode: String, path: String): String = {
    if (!path.startsWith("/")) {
      throw new IllegalArgumentException("HDFS path must be start with \"/\"")
    }
    if (namenode.endsWith("/")) {
      namenode.substring(0, namenode.length - 1) + path
    } else {
      namenode + path
    }
  }

  def createHdfsPath(namenode: String, path: String): Unit = {
    // todo check if path exists

    // todo if not exists, create hdfsPath
  }

  def chmodHdfsPath(namenode: String, path: String): Unit = {
    // todo add read and write right for users
  }

}
