package com.ctrip.data.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

import com.ctrip.data.dao.ShardingSourceGroupDao;
import com.ctrip.data.entity.ShardingSourceGroup;
import com.ctrip.data.service.IShardingSourceGroupService;

/** 
*
* @Description:
* @author j_le
* @version 创建时间：2017年4月12日 上午10:32:02 
* 
*/
public class ShardingSourceGroupServiceImpl implements IShardingSourceGroupService {
	ShardingSourceGroupDao ssgDao = null;
	public ShardingSourceGroupServiceImpl() throws SQLException{
		ssgDao = new ShardingSourceGroupDao();
	}

	public List<ShardingSourceGroup> getShardingSourceGroupList(int pageSize, int pageNo, String srcDB, String srcTB)
			throws SQLException {
		String sql = "WITH CTE AS (select a.sourceDB,a.SourceType,a.BatchNum,b.*, row_number() over(order by a.ID desc ) as rownum"
				+ " from ShardingSourceDBInfo (nolock) as a join ShardingSourceTBInfo (nolock) as b on a.ID=b.SourceDBID where 1=1 ";
		List<String> params = new ArrayList<String>();
		if(StringUtils.isNotBlank(srcDB)){
			sql += " and a.ID = ? ";
			params.add(srcDB);
		}
		if(StringUtils.isNotBlank(srcTB)){
			sql += " and b.ID = ? ";
			params.add(srcTB);
		}
		sql += " ) select * from CTE where rownum between ? and ?";
		return ssgDao.getShardingSourceGroupList(sql,pageSize,pageNo,params,null);
	}

	public int count(String srcDB,String srcTB) {
		int count = 0;
		try {
			String sql = "SELECT count(1) from ShardingSourceDBInfo (nolock) as a join ShardingSourceTBInfo (nolock) as b on a.ID=b.SourceDBID where 1=1";
			List<String> params = new ArrayList<String>();
			if(StringUtils.isNotBlank(srcDB)){
				sql += " and a.ID = ? ";
				params.add(srcDB);
			}
			if(StringUtils.isNotBlank(srcTB)){
				sql+= " and b.ID = ? ";
				params.add(srcTB);
			}
			count = ssgDao.count(sql, params, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public List<ShardingSourceGroup> getShardingSourceGroupByID(Integer id) throws SQLException {
		return ssgDao.getShardingSourceGroupByID(id,null);
	}
	
}
