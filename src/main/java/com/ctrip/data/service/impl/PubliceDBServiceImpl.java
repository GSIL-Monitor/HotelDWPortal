package com.ctrip.data.service.impl;

import com.ctrip.data.dao.PubliceDBDao;
import com.ctrip.data.entity.ShardingTBInfo;
import com.ctrip.data.service.IPubliceDBService;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by j_le on 2017/5/19.
 */
public class PubliceDBServiceImpl implements IPubliceDBService {
    PubliceDBDao publiceDBDao = new PubliceDBDao();
    public int shardingtbSaveUpdateColumns(List<ShardingTBInfo> insertinfo,Integer updateid, String updateCoulmninfo, String updatePKColumns) throws SQLException {
        String insertsql = "INSERT [dbo].[ShardingTBInfo] (SourceTBID ,ShardingTB ,InsertDT ,UpdateDT ,IsValid ,OperUid) VALUES (? ,? ,GETDATE() ,GETDATE() ,? ,?)";
        String updatesql = "UPDATE [dbo].[ShardingSourceTBInfo] SET Columns = ? , PKColumns = ? ,UpdateDT = GETDATE() WHERE ID = ? ";
        return publiceDBDao.shardingtbSaveUpdateColumns(insertsql, insertinfo, updatesql, updateid, updateCoulmninfo, updatePKColumns);
    }
}
