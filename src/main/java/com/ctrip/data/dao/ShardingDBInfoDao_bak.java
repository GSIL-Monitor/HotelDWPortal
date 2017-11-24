package com.ctrip.data.dao;

import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.ctrip.data.entity.ShardingDBInfo;
import com.ctrip.platform.dal.dao.DalClient;
import com.ctrip.platform.dal.dao.DalClientFactory;
import com.ctrip.platform.dal.dao.DalHints;
import com.ctrip.platform.dal.dao.DalQueryDao;
import com.ctrip.platform.dal.dao.DalRowMapper;
import com.ctrip.platform.dal.dao.StatementParameters;
import com.ctrip.platform.dal.dao.helper.DalDefaultJpaMapper;
import com.ctrip.platform.dal.dao.helper.DalScalarExtractor;

public class ShardingDBInfoDao_bak {

	private static final String DATA_BASE = "PublicDB";
	private DalScalarExtractor extractor = new DalScalarExtractor();
	private DalQueryDao queryDao = null;
	private DalClient baseClient;
	private DalRowMapper<ShardingDBInfo> shardingDBInfoRowMapper = null;
	public ShardingDBInfoDao_bak() throws SQLException {
		this.shardingDBInfoRowMapper = new DalDefaultJpaMapper(ShardingDBInfo.class);
		this.queryDao = new DalQueryDao(DATA_BASE);
		this.baseClient = DalClientFactory.getClient(DATA_BASE);
	}
	public List<ShardingDBInfo> getShardingDBInfos(Integer sourceDBID,DalHints hints) throws SQLException {
		String sql = "SELECT * from ShardingDBInfo(nolock) where sourceDBID=? ";
		StatementParameters parameters = new StatementParameters();
		parameters.set(1, Types.INTEGER, sourceDBID);
		hints = DalHints.createIfAbsent(hints);
		return (List<ShardingDBInfo>)queryDao.query(sql, parameters, hints, shardingDBInfoRowMapper);
	}
}
