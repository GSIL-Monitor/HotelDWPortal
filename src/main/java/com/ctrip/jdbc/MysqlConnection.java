package com.ctrip.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by j_le on 2017/5/5.
 */
public class MysqlConnection {
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Connection createConnection() {
        Connection conn = null;
        String url = "jdbc:mysql://XXXXX:XXX/zeus";
        try {
            conn = DriverManager.getConnection(url, "XXXX", "XXXXXXX");
            //System.out.println("Successfully Connected!");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
    }
}
