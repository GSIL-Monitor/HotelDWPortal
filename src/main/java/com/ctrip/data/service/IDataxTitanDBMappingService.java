package com.ctrip.data.service;

import com.ctrip.data.entity.DataxTitanDBMapping;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by j_le on 2017/5/12.
 */
public interface IDataxTitanDBMappingService {
    public List<DataxTitanDBMapping> getShardingDBInfoList(String DBType)throws SQLException;

    public DataxTitanDBMapping getShardingDBInfoByDBName(String dbName)throws SQLException;
}
