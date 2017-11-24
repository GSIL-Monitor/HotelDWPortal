package com.ctrip.data.dao;

import com.ctrip.data.entity.DataxTitanDBMapping;
import com.ctrip.jdbc.MysqlConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by j_le on 2017/5/12.
 */
public class DataxTitanDBMappingDao {
    public List<DataxTitanDBMapping> getDataxTitanDBMappingList(String sql,String DBType){
        Connection conn = MysqlConnection.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DataxTitanDBMapping> dataxTitanDBMappingList = new ArrayList<DataxTitanDBMapping>();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,DBType);
            rs = ps.executeQuery();
            while(rs.next()){
                DataxTitanDBMapping dataxTitanDBMapping = new DataxTitanDBMapping();
                dataxTitanDBMapping.setId(rs.getInt("Id"));
                dataxTitanDBMapping.setTitankey(rs.getString("Titankey"));
                dataxTitanDBMapping.setMode(rs.getString("Mode"));
                dataxTitanDBMapping.setDbName(rs.getString("DbName"));
                dataxTitanDBMapping.setDbType(rs.getString("DbType"));
                dataxTitanDBMapping.setOwner(rs.getString("Owner"));
                dataxTitanDBMapping.setServer(rs.getString("Server"));
                dataxTitanDBMapping.setPort(rs.getInt("Port"));
                dataxTitanDBMapping.setUid(rs.getString("Uid"));
                dataxTitanDBMapping.setCreate_Time(rs.getTimestamp("Create_Time"));
                dataxTitanDBMapping.setIs_effective(rs.getString("Is_effective"));
                dataxTitanDBMappingList.add(dataxTitanDBMapping);
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
        return  dataxTitanDBMappingList;
    }

    public DataxTitanDBMapping getDataxTitanDBMapping(String sql,String dbName){
        Connection conn = MysqlConnection.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        DataxTitanDBMapping dataxTitanDBMapping = new DataxTitanDBMapping();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,dbName);
            rs = ps.executeQuery();
            while(rs.next()){

                dataxTitanDBMapping.setId(rs.getInt("Id"));
                dataxTitanDBMapping.setTitankey(rs.getString("Titankey"));
                dataxTitanDBMapping.setMode(rs.getString("Mode"));
                dataxTitanDBMapping.setDbName(rs.getString("DbName"));
                dataxTitanDBMapping.setDbType(rs.getString("DbType"));
                dataxTitanDBMapping.setOwner(rs.getString("Owner"));
                dataxTitanDBMapping.setServer(rs.getString("Server"));
                dataxTitanDBMapping.setPort(rs.getInt("Port"));
                dataxTitanDBMapping.setUid(rs.getString("Uid"));
                dataxTitanDBMapping.setCreate_Time(rs.getTimestamp("Create_Time"));
                dataxTitanDBMapping.setIs_effective(rs.getString("Is_effective"));
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
        return  dataxTitanDBMapping;
    }

}
