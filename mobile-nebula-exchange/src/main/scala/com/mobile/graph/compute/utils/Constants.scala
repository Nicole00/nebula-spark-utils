/* Copyright (c) 2020 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License,
 * attached with Common Clause Condition 1.0, found in the LICENSES directory.
 */

package com.mobile.graph.compute.utils

object EncodeConstants {
  val originalIdCol: String = "_originalId"
  val encodeIdCol: String   = "_encodeId"

  val encodeSrcCol: String = "_encodeSrc"
  val encodeDstCol: String = "_encodeDst"
}

object EncodeDataPath {
  val encodeIdPath   = "/_plato/encodeId"
  val encodeEdgePath = "/_plato/data"
}
