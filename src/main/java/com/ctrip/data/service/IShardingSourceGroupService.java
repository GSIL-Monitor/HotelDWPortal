package com.ctrip.data.service;

import java.sql.SQLException;
import java.util.List;

import com.ctrip.data.entity.ShardingSourceGroup;

/**
 *
 * @Description:
 * @author j_le
 * @version 创建时间：2017年4月12日 上午10:30:25
 * 
 */
public interface IShardingSourceGroupService {
	public List<ShardingSourceGroup> getShardingSourceGroupList(int pageSize, int pageNo, String srcDB, String srcTB)
			throws SQLException;

	public int count(String srcDB, String srcTB);
	
	public List<ShardingSourceGroup> getShardingSourceGroupByID(Integer id)throws SQLException;
}
