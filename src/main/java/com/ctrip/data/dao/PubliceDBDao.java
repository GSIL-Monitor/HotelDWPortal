package com.ctrip.data.dao;

import com.ctrip.data.entity.ShardingTBInfo;
import com.ctrip.jdbc.SqlServerConnection;
import net.sf.json.JSONArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by j_le on 2017/5/19.
 */
public class PubliceDBDao {
    public int shardingtbSaveUpdateColumns(String insertsql, List<ShardingTBInfo> insertinfo, String updatesql,Integer updateid, String updateCoulmninfo, String updatePKColumns) throws SQLException {
        Connection conn = SqlServerConnection.createConnection();
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        Integer result = null;
        try {
            //先关闭自动提交
            conn.setAutoCommit(false);
            ps1 = conn.prepareStatement(insertsql);
            ps2 = conn.prepareStatement(updatesql);
            for (ShardingTBInfo shardingTBInfo: insertinfo) {
                ps1.setInt(1, shardingTBInfo.getSourceTBID());
                ps1.setString(2, shardingTBInfo.getShardingTB());
                ps1.setString(3, shardingTBInfo.getIsValid());
                ps1.setString(4, shardingTBInfo.getOperUid());
                ps1.addBatch();
            }
            ps1.executeBatch();
            ps2.setString(1,updateCoulmninfo);
            ps2.setString(2,updatePKColumns);
            ps2.setInt(3,updateid);
            ps2.executeUpdate();
            //提交事务
            conn.commit();
            result = 1;

        } catch (SQLException e) {
            //回滚事务
            conn.rollback();
            result = -1;
            //System.out.println(e);
        }finally{
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
