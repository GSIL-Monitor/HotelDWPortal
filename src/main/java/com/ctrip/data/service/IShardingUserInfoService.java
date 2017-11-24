package com.ctrip.data.service;

import java.sql.SQLException;

/** 
*
* @Description:
* @author j_le
* @version 创建时间：2017年4月25日 上午11:21:08 
* 
*/
public interface IShardingUserInfoService {
	public boolean isExistUserName(String userName)throws SQLException;

}
