package com.ctrip.data.dao;

import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.ctrip.data.entity.ShardingTBInfo;
import com.ctrip.platform.dal.dao.DalClient;
import com.ctrip.platform.dal.dao.DalClientFactory;
import com.ctrip.platform.dal.dao.DalHints;
import com.ctrip.platform.dal.dao.DalQueryDao;
import com.ctrip.platform.dal.dao.DalRowMapper;
import com.ctrip.platform.dal.dao.StatementParameters;
import com.ctrip.platform.dal.dao.helper.DalDefaultJpaMapper;
import com.ctrip.platform.dal.dao.helper.DalScalarExtractor;

/**
 *
 * @Description:
 * @author j_le E-mail: j_le@Ctrip.com
 * @version 创建时间：2017年4月11日 下午1:31:10
 * 
 */
public class ShardingTBInfoDao_bak {
	private static final String DATA_BASE = "PublicDB";
	private DalScalarExtractor extractor = new DalScalarExtractor();
	private DalQueryDao queryDao = null;
	private DalClient baseClient;
	private DalRowMapper<ShardingTBInfo> shardingTBInfoRowMapper = null;

	public ShardingTBInfoDao_bak() throws SQLException {
		this.shardingTBInfoRowMapper = new DalDefaultJpaMapper(ShardingTBInfo.class);
		this.queryDao = new DalQueryDao(DATA_BASE);
		this.baseClient = DalClientFactory.getClient(DATA_BASE);
	}

	public List<ShardingTBInfo> getShardingTBInfos(Integer sourceTBID, DalHints hints) throws SQLException {
		String sql = "SELECT * from ShardingTBInfo(nolock) where sourceTBID=? ";
		StatementParameters parameters = new StatementParameters();
		parameters.set(1, Types.INTEGER, sourceTBID);
		hints = DalHints.createIfAbsent(hints);
		return (List<ShardingTBInfo>) queryDao.query(sql, parameters, hints, shardingTBInfoRowMapper);
	}
}
