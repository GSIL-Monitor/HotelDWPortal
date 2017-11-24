package com.ctrip.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by j_le on 2017/5/19.
 */
public class SqlServerConnection {
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Connection createConnection() {
        Connection conn = null;
        //本地测试
        String url = "jdbc:sqlserver://XXXX:XXXX;databaseName=TestJavaDB";
        String username = "XXXXX";
        String password = "XXXXX";
        try {
            conn = DriverManager.getConnection(url, username, password);
            //System.out.println("Successfully Connected!");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
    }
}
