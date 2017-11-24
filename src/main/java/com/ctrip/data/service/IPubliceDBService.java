package com.ctrip.data.service;

import com.ctrip.data.entity.ShardingTBInfo;
import com.ctrip.data.entity.osg.ColumnInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by j_le on 2017/5/19.
 */
public interface IPubliceDBService {
    public int shardingtbSaveUpdateColumns(List<ShardingTBInfo> insertinfo,Integer updateid, String updateCoulmninfo, String updatePKColumns) throws SQLException;
}
