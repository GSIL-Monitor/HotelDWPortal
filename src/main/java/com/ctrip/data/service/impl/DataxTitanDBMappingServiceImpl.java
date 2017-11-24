package com.ctrip.data.service.impl;

import com.ctrip.data.dao.DataxTitanDBMappingDao;
import com.ctrip.data.entity.DataxTitanDBMapping;
import com.ctrip.data.service.IDataxTitanDBMappingService;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by j_le on 2017/5/12.
 */
public class DataxTitanDBMappingServiceImpl implements IDataxTitanDBMappingService {
    DataxTitanDBMappingDao dataxDao = null;

    public DataxTitanDBMappingServiceImpl() throws SQLException {
        dataxDao = new DataxTitanDBMappingDao();
    }

    public List<DataxTitanDBMapping> getShardingDBInfoList(String DBType)throws SQLException{
        String sql = "select *  from zeus.datax_titan_db_mapping where owner = 'htldatadev' and mode= 'R' and DBType = ? order by dbname";
        return dataxDao.getDataxTitanDBMappingList(sql,DBType);
    };

    public DataxTitanDBMapping getShardingDBInfoByDBName(String dbName)throws SQLException{
        String sql = "select *  from zeus.datax_titan_db_mapping where owner = 'htldatadev' and mode= 'R' and dbName = ? ";
        return dataxDao.getDataxTitanDBMapping(sql,dbName);
    };
}
