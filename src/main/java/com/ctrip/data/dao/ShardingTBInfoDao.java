package com.ctrip.data.dao;

import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.ctrip.data.entity.ShardingTBInfo;
import com.ctrip.platform.dal.common.enums.DatabaseCategory;
import com.ctrip.platform.dal.dao.DalClient;
import com.ctrip.platform.dal.dao.DalClientFactory;
import com.ctrip.platform.dal.dao.DalHints;
import com.ctrip.platform.dal.dao.DalParser;
import com.ctrip.platform.dal.dao.DalQueryDao;
import com.ctrip.platform.dal.dao.DalTableDao;
import com.ctrip.platform.dal.dao.KeyHolder;
import com.ctrip.platform.dal.dao.StatementParameters;
import com.ctrip.platform.dal.dao.helper.DalDefaultJpaParser;
import com.ctrip.platform.dal.dao.helper.DalScalarExtractor;

/**
 *
 * @Description:
 * @author j_le E-mail: j_le@Ctrip.com
 * @version 创建时间：2017年4月11日 下午1:22:07
 * 
 */
public class ShardingTBInfoDao {
	private static final String DATA_BASE = "PublicDB";
	private static DatabaseCategory dbCategory = null;
	private static final String COUNT_SQL_PATTERN = "SELECT count(1) from ShardingTBInfo WITH (NOLOCK)";
	private static final String ALL_SQL_PATTERN = "SELECT * FROM ShardingTBInfo WITH (NOLOCK)";
	private static final String PAGE_SQL_PATTERN = "WITH CTE AS (select *, row_number() over(order by ID desc ) as rownum"
			+ " from ShardingTBInfo (nolock)) select * from CTE where rownum between ? and ?";
	private DalParser<ShardingTBInfo> parser = null;
	private DalScalarExtractor extractor = new DalScalarExtractor();
	private DalTableDao<ShardingTBInfo> client;
	private DalQueryDao queryDao = null;
	private DalClient baseClient;

	public ShardingTBInfoDao() throws SQLException {
			parser = new DalDefaultJpaParser<ShardingTBInfo>(ShardingTBInfo.class);
			this.client = new DalTableDao<ShardingTBInfo>(parser);
			dbCategory = this.client.getDatabaseCategory();
			this.queryDao = new DalQueryDao(DATA_BASE);
			this.baseClient = DalClientFactory.getClient(DATA_BASE);
		}

	/**
	 * Query ShardingDBInfo by the specified ID The ID must be a number
	 **/
	public ShardingTBInfo queryByPk(Number id, DalHints hints) throws SQLException {
		hints = DalHints.createIfAbsent(hints);
		return client.queryByPk(id, hints);
	}

	/**
	 * Query ShardingDBInfo by ShardingDBInfo instance which the primary key is
	 * set
	 **/
	public ShardingTBInfo queryByPk(ShardingTBInfo pk, DalHints hints) throws SQLException {
		hints = DalHints.createIfAbsent(hints);
		return client.queryByPk(pk, hints);
	}

	/**
	 * Get the records count
	 **/
	public int count(DalHints hints) throws SQLException {
		StatementParameters parameters = new StatementParameters();
		hints = DalHints.createIfAbsent(hints);
		Number result = (Number) this.baseClient.query(COUNT_SQL_PATTERN, parameters, hints, extractor);
		return result.intValue();
	}

	/**
	 * Query ShardingDBInfo with paging function The pageSize and pageNo must be
	 * greater than zero.
	 **/
	public List<ShardingTBInfo> queryByPage(int pageSize, int pageNo, DalHints hints) throws SQLException {
		if (pageNo < 1 || pageSize < 1)
			throw new SQLException("Illigal pagesize or pageNo, pls check");
		StatementParameters parameters = new StatementParameters();
		hints = DalHints.createIfAbsent(hints);
		String sql = PAGE_SQL_PATTERN;
		int fromRownum = (pageNo - 1) * pageSize + 1;
		int endRownum = pageSize * pageNo;
		parameters.set(1, Types.INTEGER, fromRownum);
		parameters.set(2, Types.INTEGER, endRownum);
		return queryDao.query(sql, parameters, hints, parser);
	}

	public List<ShardingTBInfo> getShardingTBInfos(Integer sourceTBID, DalHints hints) throws SQLException {
		String sql = "SELECT * from ShardingTBInfo(nolock) where sourceTBID=? ";
		StatementParameters parameters = new StatementParameters();
		parameters.set(1, Types.INTEGER, sourceTBID);
		hints = DalHints.createIfAbsent(hints);
		return (List<ShardingTBInfo>) queryDao.query(sql, parameters, hints, parser);
	}

	/**
	 * Get all records in the whole table
	 **/
	public List<ShardingTBInfo> getAll(DalHints hints) throws SQLException {
		StatementParameters parameters = new StatementParameters();
		hints = DalHints.createIfAbsent(hints);
		List<ShardingTBInfo> result = null;
		result = queryDao.query(ALL_SQL_PATTERN, parameters, hints, parser);
		return result;
	}

	/**
	 * Insert pojo and get the generated PK back in keyHolder. If the
	 * "set no count on" for MS SqlServer is set(currently set in Ctrip), the
	 * operation may fail. Please don't pass keyholder for MS SqlServer to avoid
	 * the failure.
	 * 
	 * @param hints
	 *            Additional parameters that instruct how DAL Client perform
	 *            database operation.
	 * @param daoPojo
	 *            pojo to be inserted
	 * @return how many rows been affected
	 * @throws SQLException
	 */
	public int insert(DalHints hints, ShardingTBInfo daoPojo) throws SQLException {
		if (null == daoPojo)
			return 0;
		hints = DalHints.createIfAbsent(hints);
		return client.insert(hints, daoPojo);
	}

	/**
	 * Insert pojos one by one. If you want to inert them in the batch mode,
	 * user batchInsert instead. You can also use the combinedInsert.
	 * 
	 * @param hints
	 *            Additional parameters that instruct how DAL Client perform
	 *            database operation. DalHintEnum.continueOnError can be used to
	 *            indicate that the inserting can be go on if there is any
	 *            failure.
	 * @param daoPojos
	 *            list of pojos to be inserted
	 * @return how many rows been affected
	 */
	public int[] insert(DalHints hints, List<ShardingTBInfo> daoPojos) throws SQLException {
		if (null == daoPojos || daoPojos.size() <= 0)
			return new int[0];
		hints = DalHints.createIfAbsent(hints);
		return client.insert(hints, daoPojos);
	}

	/**
	 * Insert pojo and get the generated PK back in keyHolder. If the
	 * "set no count on" for MS SqlServer is set(currently set in Ctrip), the
	 * operation may fail. Please don't pass keyholder for MS SqlServer to avoid
	 * the failure.
	 * 
	 * @param hints
	 *            Additional parameters that instruct how DAL Client perform
	 *            database operation.
	 * @param keyHolder
	 *            holder for generated primary keys
	 * @param daoPojo
	 *            pojo to be inserted
	 * @return how many rows been affected
	 * @throws SQLException
	 */
	public int insert(DalHints hints, KeyHolder keyHolder, ShardingTBInfo daoPojo) throws SQLException {
		if (null == daoPojo)
			return 0;
		hints = DalHints.createIfAbsent(hints);
		return client.insert(hints, keyHolder, daoPojo);
	}

	/**
	 * Insert pojos and get the generated PK back in keyHolder. If the
	 * "set no count on" for MS SqlServer is set(currently set in Ctrip), the
	 * operation may fail. Please don't pass keyholder for MS SqlServer to avoid
	 * the failure.
	 * 
	 * @param hints
	 *            Additional parameters that instruct how DAL Client perform
	 *            database operation. DalHintEnum.continueOnError can be used to
	 *            indicate that the inserting can be go on if there is any
	 *            failure.
	 * @param keyHolder
	 *            holder for generated primary keys
	 * @param daoPojos
	 *            list of pojos to be inserted
	 * @return how many rows been affected
	 * @throws SQLException
	 */
	public int[] insert(DalHints hints, KeyHolder keyHolder, List<ShardingTBInfo> daoPojos) throws SQLException {
		if (null == daoPojos || daoPojos.size() <= 0)
			return new int[0];
		hints = DalHints.createIfAbsent(hints);
		return client.insert(hints, keyHolder, daoPojos);
	}

	/**
	 * Insert pojos in batch mode. The DalDetailResults will be set in hints to
	 * allow client know how the operation performed in each of the shard.
	 * 
	 * @param hints
	 *            Additional parameters that instruct how DAL Client perform
	 *            database operation.
	 * @param daoPojos
	 *            list of pojos to be inserted
	 * @return how many rows been affected for inserting each of the pojo
	 * @throws SQLException
	 */
	public int[] batchInsert(DalHints hints, List<ShardingTBInfo> daoPojos) throws SQLException {
		if (null == daoPojos || daoPojos.size() <= 0)
			return new int[0];
		hints = DalHints.createIfAbsent(hints);
		return client.batchInsert(hints, daoPojos);
	}

	/**
	 * Delete the given pojo.
	 * 
	 * @param hints
	 *            Additional parameters that instruct how DAL Client perform
	 *            database operation.
	 * @param daoPojo
	 *            pojo to be deleted
	 * @return how many rows been affected
	 * @throws SQLException
	 */
	public int delete(DalHints hints, ShardingTBInfo daoPojo) throws SQLException {
		if (null == daoPojo)
			return 0;
		hints = DalHints.createIfAbsent(hints);
		return client.delete(hints, daoPojo);
	}

	/**
	 * Delete the given pojos list one by one.
	 * 
	 * @param hints
	 *            Additional parameters that instruct how DAL Client perform
	 *            database operation.
	 * @param daoPojos
	 *            list of pojos to be deleted
	 * @return how many rows been affected
	 * @throws SQLException
	 */
	public int[] delete(DalHints hints, List<ShardingTBInfo> daoPojos) throws SQLException {
		if (null == daoPojos || daoPojos.size() <= 0)
			return new int[0];
		hints = DalHints.createIfAbsent(hints);
		return client.delete(hints, daoPojos);
	}

	/**
	 * Delete the given pojo list in batch. The DalDetailResults will be set in
	 * hints to allow client know how the operation performed in each of the
	 * shard.
	 * 
	 * @param hints
	 *            Additional parameters that instruct how DAL Client perform
	 *            database operation.
	 * @param daoPojos
	 *            list of pojos to be deleted
	 * @return how many rows been affected for deleting each of the pojo
	 * @throws SQLException
	 */
	public int[] batchDelete(DalHints hints, List<ShardingTBInfo> daoPojos) throws SQLException {
		if (null == daoPojos || daoPojos.size() <= 0)
			return new int[0];
		hints = DalHints.createIfAbsent(hints);
		return client.batchDelete(hints, daoPojos);
	}

	/**
	 * Update the given pojo . By default, if a field of pojo is null value,
	 * that field will be ignored, so that it will not be updated. You can
	 * overwrite this by set updateNullField in hints.
	 * 
	 * @param hints
	 *            Additional parameters that instruct how DAL Client perform
	 *            database operation. DalHintEnum.updateNullField can be used to
	 *            indicate that the field of pojo is null value will be update.
	 * @param daoPojo
	 *            pojo to be updated
	 * @return how many rows been affected
	 * @throws SQLException
	 */
	public int update(DalHints hints, ShardingTBInfo daoPojo) throws SQLException {
		if (null == daoPojo)
			return 0;
		hints = DalHints.createIfAbsent(hints);
		return client.update(hints, daoPojo);
	}

	/**
	 * Update the given pojo list one by one. By default, if a field of pojo is
	 * null value, that field will be ignored, so that it will not be updated.
	 * You can overwrite this by set updateNullField in hints.
	 * 
	 * @param hints
	 *            Additional parameters that instruct how DAL Client perform
	 *            database operation. DalHintEnum.updateNullField can be used to
	 *            indicate that the field of pojo is null value will be update.
	 * @param daoPojos
	 *            list of pojos to be updated
	 * @return how many rows been affected
	 * @throws SQLException
	 */
	public int[] update(DalHints hints, List<ShardingTBInfo> daoPojos) throws SQLException {
		if (null == daoPojos || daoPojos.size() <= 0)
			return new int[0];
		hints = DalHints.createIfAbsent(hints);
		return client.update(hints, daoPojos);
	}
}
