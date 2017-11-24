package com.ctrip.data.service;

import java.sql.SQLException;
import java.util.List;

import com.ctrip.data.entity.ShardingDBInfo;
import com.ctrip.data.entity.ShardingSourceDBInfo;

public interface IShardingSourceDBInfoService {
	/**
	 * 
	 * @Description:获取ShardingSourceDBInfo list
	 * @Auther: jy.lu
	 * @Date: 2017年3月21日 下午6:28:05
	 */
	public List<ShardingSourceDBInfo> getShardingSourceDBInfoList(int pageSize, int pageNo,String srcType,String srcDB)throws SQLException ;
	public int count(String srcType,String srcDB);
	/**
	 * 
	 * @Description:获取所有ShardingSourceDBInfo
	 * @Auther: jy.lu
	 * @Date: 2017年3月21日 下午6:33:28
	 */
	public List<ShardingSourceDBInfo> getAll()throws SQLException ;
	/**
	 * 
	 * @throws SQLException 
	 * @Description:获取shardingdb
	 * @Auther: jy.lu
	 * @Date: 2017年3月22日 下午3:09:39
	 */
	public List<ShardingDBInfo> getShardingDBInfoList(Integer sourceDBID) throws SQLException;
	
	public int insert(ShardingSourceDBInfo obj)throws SQLException;
	public int update(ShardingSourceDBInfo obj)throws SQLException;
	/**
	 * 
	 * @Description:根据主键获取数据
	 * @Auther: jy.lu
	 * @Date: 2017年3月22日 下午6:19:04
	 */
	public ShardingSourceDBInfo getShardingSourceDBById(Integer id)throws SQLException;
	/**
	 * 
	 * @Description:根据id删除数据
	 * @Auther: jy.lu
	 * @Date: 2017年3月23日 上午9:28:01
	 */
	public boolean deleteShardingSourceDBInfoById(Integer id);
	/**
	 * 
	 * @Description:批量插入ShardingDBInfo
	 * @Auther: jy.lu
	 * @Date: 2017年3月23日 上午11:53:21
	 */
	public boolean insertBatch(List<ShardingDBInfo> list)throws SQLException;
	/**
	 * 
	 * @Description:校验shardingsourcedb源是否存在，存在返回true
	 * @Auther: jy.lu
	 * @Date: 2017年3月23日 下午3:29:57
	 */
	public boolean isExistShardingSourceDB(String sourceDB)throws SQLException;
	/**
	 * 
	 * @Description:根据源db的id删除对应的shardingdb
	 * @Auther: jy.lu
	 * @Date: 2017年3月23日 下午4:36:55
	 */
	public boolean deleteShardingDBBySourceDBId(List<ShardingDBInfo> list)throws SQLException;
}
