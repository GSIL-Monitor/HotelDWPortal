package com.ctrip.data.dao;

import java.sql.SQLException;
import java.sql.Types;

import com.ctrip.data.entity.ShardingUserInfo;
import com.ctrip.platform.dal.common.enums.DatabaseCategory;
import com.ctrip.platform.dal.dao.DalClient;
import com.ctrip.platform.dal.dao.DalClientFactory;
import com.ctrip.platform.dal.dao.DalHints;
import com.ctrip.platform.dal.dao.DalParser;
import com.ctrip.platform.dal.dao.DalQueryDao;
import com.ctrip.platform.dal.dao.DalTableDao;
import com.ctrip.platform.dal.dao.StatementParameters;
import com.ctrip.platform.dal.dao.helper.DalDefaultJpaParser;
import com.ctrip.platform.dal.dao.helper.DalScalarExtractor;

/** 
*
* @Description:
* @author j_le
* @version 创建时间：2017年4月25日 上午11:15:05 
* 
*/
public class ShardingUserInfoDao {
	private static final String DATA_BASE = "PublicDB";
	private static DatabaseCategory dbCategory = null;
	private DalParser<ShardingUserInfo> parser = null;
	private DalScalarExtractor extractor = new DalScalarExtractor();
	private DalTableDao<ShardingUserInfo> client;
	private DalQueryDao queryDao = null;
	private DalClient baseClient;
	
	public ShardingUserInfoDao() throws SQLException {
		parser = new DalDefaultJpaParser<ShardingUserInfo>(ShardingUserInfo.class);
		this.client = new DalTableDao<ShardingUserInfo>(parser);
		dbCategory = this.client.getDatabaseCategory();
		this.queryDao = new DalQueryDao(DATA_BASE);
		this.baseClient = DalClientFactory.getClient(DATA_BASE);
	}
	
	public int countByUserName(String userName, DalHints hints) throws SQLException {
		StatementParameters parameters = new StatementParameters();
		hints = DalHints.createIfAbsent(hints);
		String sql = "SELECT count(1) from ShardingUserInfo WITH (NOLOCK) where IsValid = 'T' AND UserName = ?";
		parameters.set(1, Types.VARCHAR, userName);
		Number result = (Number) this.baseClient.query(sql, parameters, hints, extractor);
		return result.intValue();
	}

}
