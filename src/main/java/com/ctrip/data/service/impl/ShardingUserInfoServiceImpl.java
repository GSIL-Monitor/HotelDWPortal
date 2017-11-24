package com.ctrip.data.service.impl;

import java.sql.SQLException;

import com.ctrip.data.dao.ShardingUserInfoDao;
import com.ctrip.data.service.IShardingUserInfoService;

/** 
*
* @Description:
* @author j_le
* @version 创建时间：2017年4月25日 上午11:22:56 
* 
*/
public class ShardingUserInfoServiceImpl implements IShardingUserInfoService {
	ShardingUserInfoDao suinfoDao = null;

	public ShardingUserInfoServiceImpl() throws SQLException {
		suinfoDao = new ShardingUserInfoDao();
	}

	public boolean isExistUserName(String userName) throws SQLException {
		int count = suinfoDao.countByUserName(userName, null);
		if (count > 0) {
			return true;
		}
		return false;
	}

}
