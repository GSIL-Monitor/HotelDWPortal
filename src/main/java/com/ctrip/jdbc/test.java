package com.ctrip.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by j_le on 2017/5/5.
 */
public class test {
    public List<String> retrieve(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select *  from test limit 10";
        List<String> list = new ArrayList<String>();

        conn = MysqlConnection.createConnection();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
