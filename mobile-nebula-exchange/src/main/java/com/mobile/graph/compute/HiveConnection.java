package com.mobile.graph.compute;/* Copyright (c) 2020 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License,
 * attached with Common Clause Condition 1.0, found in the LICENSES directory.
 */

public class HiveConnection {
    private String waredir;
    private String connectionURL;
    private String driverName;
    private String userName;
    private String password;

    public String getWaredir() {
        return waredir;
    }

    public void setWaredir(String waredir) {
        this.waredir = waredir;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "com.mobile.graph.compute.HiveConnection{" +
                "waredir='" + waredir + '\'' +
                ", connectionURL='" + connectionURL + '\'' +
                ", driverName='" + driverName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
