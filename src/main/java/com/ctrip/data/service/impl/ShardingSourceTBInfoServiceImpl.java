package com.ctrip.data.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ctrip.data.dao.ShardingSourceTBInfoDao;
import com.ctrip.data.dao.ShardingTBInfoDao;
import com.ctrip.data.entity.ShardingDBInfo;
import com.ctrip.data.entity.ShardingSourceDBInfo;
import com.ctrip.data.entity.ShardingSourceTBInfo;
import com.ctrip.data.entity.ShardingTBInfo;
import com.ctrip.data.service.IShardingSourceTBInfoService;

/** 
*
* @Description:
* @author j_le E-mail: j_le@Ctrip.com
* @version 创建时间：2017年4月11日 下午2:11:07 
* 
*/
public class ShardingSourceTBInfoServiceImpl implements IShardingSourceTBInfoService {
	ShardingSourceTBInfoDao sstbDao = null;
	ShardingTBInfoDao stbDao = null;
	public ShardingSourceTBInfoServiceImpl() throws SQLException{
		sstbDao = new ShardingSourceTBInfoDao();
		stbDao = new ShardingTBInfoDao();
	}
/*
	public List<ShardingSourceTBInfo> getShardingSourceTBInfoList(int pageSize, int pageNo, String srcDB, String srcTB)
			throws SQLException {
		String sql = "WITH CTE AS (select a.*,b.*, row_number() over(order by ID desc ) as rownum"
				+ " from ShardingSourceDBInfo (nolock) as a left join ShardingSourceTBInfo (nolock) as b on a.ID=b.SourceDBID where 1=1 ";
		List<String> params = new ArrayList<String>();
		if(StringUtils.isNotBlank(srcDB)){
			sql += " and ID = ? ";
			params.add(srcDB);
		}
		if(StringUtils.isNotBlank(srcTB)){
			sql += " and SourceDBID = ? ";
			params.add(srcTB);
		}
		sql += " ) select * from CTE where rownum between ? and ?";
		return sstbDao.getShardingSourceTBInfoList(sql,pageSize,pageNo,params,null);
	}
*/
	public int count(String srcDB, String srcTB) {
		int count = 0;
		try {
			String sql = "SELECT count(1) from ShardingSourceTBInfo(nolock) where 1=1 ";
			List<String> params = new ArrayList<String>();
			if(StringUtils.isNotBlank(srcDB)){
				sql += " and ID = ? ";
				params.add(srcDB);
			}
			if(StringUtils.isNotBlank(srcTB)){
				sql+= " and SourceDBID = ? ";
				params.add(srcTB);
			}
			count = sstbDao.count(sql, params, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public List<ShardingSourceTBInfo> getAll() throws SQLException {
		return sstbDao.getAll(null);
	}

	public List<ShardingTBInfo> getShardingTBInfoList(Integer sourceTBID) throws SQLException {
		return stbDao.getShardingTBInfos(sourceTBID,null);
	}

	public int insert(ShardingSourceTBInfo obj) throws SQLException {
		return sstbDao.insert(null, obj);
	}

	public int update(ShardingSourceTBInfo obj) throws SQLException {
		return sstbDao.update(null, obj);
	}

	public ShardingSourceTBInfo getShardingSourceTBById(Integer id) throws SQLException {
		return sstbDao.queryByPk(id,null);
	}

	public boolean deleteShardingSourceTBInfoById(Integer id) {
		try {
			List<ShardingTBInfo> shardingTBInfoList = getShardingTBInfoList(id);
			stbDao.delete(null, shardingTBInfoList);
			ShardingSourceTBInfo obj = new ShardingSourceTBInfo();
			obj.setId(id);
			int count = sstbDao.delete(null, obj);
			if(count>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean insertBatch(List<ShardingTBInfo> list) throws SQLException {
		int[] insert = stbDao.insert(null,list);
		if(insert.length>0){
			return true;
		}
		return false;
	}

	public boolean isExistShardingSourceTB(String sourceTB) throws SQLException {
		int count = sstbDao.countBySourceTB(sourceTB,null);
		if(count>0){
			return true;
		}
		return false;
	}

	public boolean deleteShardingTBBySourceDBId(List<ShardingTBInfo> list) throws SQLException {
		int[] count = stbDao.delete(null, list);
		if(count.length>0){
			return true;
		}
		return false;
	}

}
