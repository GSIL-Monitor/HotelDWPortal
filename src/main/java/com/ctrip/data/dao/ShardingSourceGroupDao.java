package com.ctrip.data.dao;

import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.ctrip.data.entity.ShardingSourceGroup;
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
 * @version 创建时间：2017年4月12日 上午10:25:34
 * 
 */
public class ShardingSourceGroupDao {
	private static final String DATA_BASE = "PublicDB";
	private static DatabaseCategory dbCategory = null;
	private DalParser<ShardingSourceGroup> parser = null;
	private DalScalarExtractor extractor = new DalScalarExtractor();
	private DalTableDao<ShardingSourceGroup> client;
	private DalQueryDao queryDao = null;
	private DalClient baseClient;

	public ShardingSourceGroupDao() throws SQLException {
		parser = new DalDefaultJpaParser<ShardingSourceGroup>(ShardingSourceGroup.class);
		this.client = new DalTableDao<ShardingSourceGroup>(parser);
		dbCategory = this.client.getDatabaseCategory();
		this.queryDao = new DalQueryDao(DATA_BASE);
		this.baseClient = DalClientFactory.getClient(DATA_BASE);
	}

	public List<ShardingSourceGroup> getShardingSourceGroupList(String sql, int pageSize, int pageNo, List params,
			DalHints hints) throws SQLException {
		StatementParameters parameters = new StatementParameters();
		hints = DalHints.createIfAbsent(hints);
		int fromRownum = (pageNo - 1) * pageSize + 1;
		int endRownum = pageSize * pageNo;
		int i = 1;
		if (params.size() > 0) {
			for (Object p : params) {
				parameters.set(i++, p);
			}
		}
		parameters.set(i++, Types.INTEGER, fromRownum);
		parameters.set(i++, Types.INTEGER, endRownum);
		return (List<ShardingSourceGroup>) queryDao.query(sql, parameters, hints, parser);
	}

	public int count(String sql, List params, DalHints hints) throws SQLException {
		StatementParameters parameters = new StatementParameters();
		hints = DalHints.createIfAbsent(hints);
		int i = 1;
		if (params.size() > 0) {
			for (Object p : params) {
				parameters.set(i++, p);
			}
		}
		Number result = (Number) this.baseClient.query(sql, parameters, hints, extractor);
		return result.intValue();
	}

	public List<ShardingSourceGroup> getShardingSourceGroupByID(int id, DalHints hints) throws SQLException {
		StatementParameters parameters = new StatementParameters();
		hints = DalHints.createIfAbsent(hints);
		String sql = "SELECT a.sourceDB,a.SourceType,a.BatchNum,b.*  FROM ShardingSourceDBInfo (nolock) as a "
				+ "JOIN ShardingSourceTBInfo (nolock) as b on a.ID=b.SourceDBID  WHERE b.ID = ?";
		parameters.set(1, Types.INTEGER, id);
		return (List<ShardingSourceGroup>) queryDao.query(sql, parameters, hints, parser);
	}
}
