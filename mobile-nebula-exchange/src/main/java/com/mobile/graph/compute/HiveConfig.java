/* Copyright (c) 2020 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License,
 * attached with Common Clause Condition 1.0, found in the LICENSES directory.
 */

package com.mobile.graph.compute;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HiveConfig {
    private String hdfs;
    private HiveConnection connection;
    private String sql;

    public String getHdfs() {
        return hdfs;
    }

    public void setHdfs(String hdfs) {
        this.hdfs = hdfs;
    }

    public HiveConnection getConnection() {
        return connection;
    }

    public void setConnection(HiveConnection connection) {
        this.connection = connection;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String[] getIdColumns() {
        String regex = "(?<=select ).*?(?=from .*?)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sql.trim().toLowerCase());
        String columns = null;
        if (matcher.find()) {
            columns = matcher.group();
        } else {
            throw new IllegalArgumentException("hive sql is invalidate, expect for \"select col1," +
                    " col2,col3 from database.table\"");
        }

        if (!columns.contains(",")) {
            throw new IllegalArgumentException("invalidate hive sql, expect for at least two " +
                    "columns");
        }

        String[] columnArray = columns.split(",");
        String[] cols = new String[3];
        if (columnArray[0].contains(" as ")) {
            cols[0] = columnArray[0].substring(columnArray[0].indexOf(" as ") + 4).trim();
        } else {
            cols[0] = columnArray[0].trim();
        }
        if (columnArray[1].contains(" as ")) {
            cols[1] = columnArray[1].substring(columnArray[1].indexOf(" as ") + 4).trim();
        } else {
            cols[1] = columnArray[1].trim();
        }
        if(columnArray.length >2){
            if(columnArray[2].contains(" as ")){
                cols[2] = columnArray[2].substring(columnArray[2].indexOf(" as ") + 4).trim();
            }else{
                cols[2] = columnArray[2].trim();
            }
        }
        return cols;
    }

    @Override
    public String toString() {
        return "HiveConfig{" +
                "hdfs='" + hdfs + '\'' +
                ", connection=" + connection +
                ", sql='" + sql + '\'' +
                '}';
    }
}
