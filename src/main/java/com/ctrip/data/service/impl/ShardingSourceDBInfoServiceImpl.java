package com.ctrip.data.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import com.ctrip.data.dao.ShardingDBInfoDao;
import com.ctrip.data.dao.ShardingSourceDBInfoDao;
import com.ctrip.data.entity.ShardingDBInfo;
import com.ctrip.data.entity.ShardingSourceDBInfo;
import com.ctrip.data.service.IShardingSourceDBInfoService;

public class ShardingSourceDBInfoServiceImpl implements IShardingSourceDBInfoService {
	ShardingSourceDBInfoDao ssdbDao = null;
	ShardingDBInfoDao sdbDao = null;

	public ShardingSourceDBInfoServiceImpl() throws SQLException {
		ssdbDao = new ShardingSourceDBInfoDao();
		sdbDao = new ShardingDBInfoDao();
	}

	public List<ShardingSourceDBInfo> getShardingSourceDBInfoList(int pageSize, int pageNo, String srcType,
			String srcDB) throws SQLException {
		String sql = "WITH CTE AS (select *, row_number() over(order by ID desc ) as rownum"
				+ " from ShardingSourceDBInfo (nolock) where 1=1 ";
		List<String> params = new ArrayList<String>();
		if (StringUtils.isNotBlank(srcDB)) {
			sql += " and ID = ? ";
			params.add(srcDB);
		}
		if (StringUtils.isNotBlank(srcType)) {
			sql += " and SourceType = ? ";
			params.add(srcType);
		}
		sql += " ) select * from CTE where rownum between ? and ?";
		return ssdbDao.getShardingSourceDBInfoList(sql, pageSize, pageNo, params, null);
	}

	public List<ShardingSourceDBInfo> getAll() throws SQLException {
		return ssdbDao.getAll(null);
	}

	public int count(String srcType, String srcDB) {
		int count = 0;
		try {
			String sql = "SELECT count(1) from ShardingSourceDBInfo(nolock) where 1=1 ";
			List<String> params = new ArrayList<String>();
			if (StringUtils.isNotBlank(srcDB)) {
				sql += " and ID = ? ";
				params.add(srcDB);
			}
			if (StringUtils.isNotBlank(srcType)) {
				sql += " and SourceType = ? ";
				params.add(srcType);
			}
			count = ssdbDao.count(sql, params, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public List<ShardingDBInfo> getShardingDBInfoList(Integer sourceDBID) throws SQLException {
		return sdbDao.getShardingDBInfos(sourceDBID, null);
	}

	public int insert(ShardingSourceDBInfo obj) throws SQLException {
		return ssdbDao.insert(null, obj);
	}

	public int update(ShardingSourceDBInfo obj) throws SQLException {
		return ssdbDao.update(null, obj);
	}

	public ShardingSourceDBInfo getShardingSourceDBById(Integer id) throws SQLException {
		return ssdbDao.queryByPk(id, null);
	}

	public boolean deleteShardingSourceDBInfoById(Integer id) {
		try {
			List<ShardingDBInfo> shardingDBInfoList = getShardingDBInfoList(id);
			sdbDao.delete(null, shardingDBInfoList);
			ShardingSourceDBInfo obj = new ShardingSourceDBInfo();
			obj.setId(id);
			int count = ssdbDao.delete(null, obj);
			if (count > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean insertBatch(List<ShardingDBInfo> list) throws SQLException {
		int[] insert = sdbDao.insert(null, list);
		if (insert.length > 0) {
			return true;
		}
		return false;
	}

	public boolean isExistShardingSourceDB(String sourceDB) throws SQLException {
		int count = ssdbDao.countBySourceDB(sourceDB, null);
		if (count > 0) {
			return true;
		}
		return false;
	}

	public boolean deleteShardingDBBySourceDBId(List<ShardingDBInfo> list) throws SQLException {
		int[] count = sdbDao.delete(null, list);
		if (count.length > 0) {
			return true;
		}
		return false;
	}

}
