package com.ctrip.data.service;

import java.sql.SQLException;
import java.util.List;

import com.ctrip.data.entity.ShardingSourceTBInfo;
import com.ctrip.data.entity.ShardingTBInfo;


/** 
*
* @Description:定义处理ShardingSourceTBInfo表数据接口
* @author j_le E-mail: j_le@Ctrip.com
* @version 创建时间：2017年4月11日 下午1:54:05 
* 
*/
public interface IShardingSourceTBInfoService {
	/**
	 * @param pageSize
	 * @param pageNo
	 * @param srcTB
	 * @return
	 * @throws SQLException
	 * @Description:获取ShardingSourceTBInfo表数据 list
	 */
//	public List<ShardingSourceTBInfo> getShardingSourceTBInfoList(int pageSize, int pageNo,String srcDB,String srcTB)throws SQLException ;
	/**
	 * @param srcDB
	 * @param srcTB
	 * @return
	 */
	public int count(String srcDB,String srcTB);

	public List<ShardingSourceTBInfo> getAll()throws SQLException ;
	/**
	 * @param sourceTBID
	 * @return
	 * @throws SQLException
	 */
	public List<ShardingTBInfo> getShardingTBInfoList(Integer sourceTBID) throws SQLException;
	
	public int insert(ShardingSourceTBInfo obj)throws SQLException;
	
	public int update(ShardingSourceTBInfo obj)throws SQLException;
	
	public ShardingSourceTBInfo getShardingSourceTBById(Integer id)throws SQLException;
	
	public boolean deleteShardingSourceTBInfoById(Integer id);
	
	public boolean insertBatch(List<ShardingTBInfo> list)throws SQLException;
	
	public boolean isExistShardingSourceTB(String sourceTB)throws SQLException;
	
	public boolean deleteShardingTBBySourceDBId(List<ShardingTBInfo> list)throws SQLException;


}
