package com.pingan.jrkj.datacenter.dataupdate.handle;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pingan.jrkj.datacenter.common.dao.JDBCDao;
import com.pingan.jrkj.datacenter.common.util.DateUtil;
import com.pingan.jrkj.datacenter.common.util.IntegerUtil;
import com.pingan.jrkj.datacenter.common.util.StringUtil;
import com.pingan.jrkj.datacenter.dataupdate.util.ApplicationUtil;

public class RefDataItemReadinessHandle {

	private static final Logger logger = LoggerFactory.getLogger(RefDataItemReadinessHandle.class);

	public static void main(String[] args) throws Exception {
		long beginTime = System.currentTimeMillis();
		logger.info("RefDataItemReadinessHandle Begin");
		Integer number = 1000;
		try {
			number = Integer.parseInt(args[0]);
		} catch (Exception e) {
			// do nothing
			logger.info("Have no args");
		}
		logger.info("每次刷新的记录数:" + number);
		// 加载spring
		ApplicationUtil.init();
		RefDataItemReadinessHandle handle = new RefDataItemReadinessHandle();
		// 一次刷新1000条记录
		handle.refReadiness(number);
		logger.info("RefDataItemReadinessHandle End | 消耗时间:" + (System.currentTimeMillis() - beginTime));
	}

	/**
	 * 分次数刷新
	 * 
	 * @param refreshCount
	 *            每次刷新的条数
	 * @throws Exception
	 */
	public void refReadiness(final Integer refreshCount) throws Exception {
		JDBCDao jdbcDao = (JDBCDao) ApplicationUtil.getBean("JDBCDao");
		// 查询需要更新的记录数
		Map<String, Object> m = jdbcDao.findByUnique(FIND_COUNT_SQL.toString());
		Integer count = IntegerUtil.getInteger((m.get("count")));
		Integer current = 1;
		Integer totalPage = count % refreshCount == 0 ? count / refreshCount : count / refreshCount + 1;
		do {
			Integer beginLimit = (current - 1) * refreshCount;
			refReadiness(beginLimit, refreshCount);
			current++;
		} while (current <= totalPage);
	}

	/**
	 * 根据Java分页刷新Readiness
	 * 
	 * @param beginLimit
	 * @param endLimit
	 * @throws Exception
	 */
	public void refReadiness(final Integer beginLimit, final Integer endLimit) throws Exception {
		JDBCDao jdbcDao = (JDBCDao) ApplicationUtil.getBean("JDBCDao");
		// 查询所有要更新的记录
		List<Map<String, Object>> list = jdbcDao.find(FIND_SQL.toString(), beginLimit, endLimit);

		for (Map<String, Object> map : list) {
			StringBuffer findMaxSql = new StringBuffer();
			findMaxSql.append("SELECT MAX(" + map.get("PeriodDateExp") + ") as MaxDate");
			findMaxSql.append(" FROM " + map.get("SQLFrom"));
			findMaxSql.append(" WHERE 1=1 " + (map.get("SQLWhere") == null ? "" : map.get("SQLWhere")));
			findMaxSql.append("    AND " + map.get("SQLPeriodTypeExp") + "='" + map.get("PeriodTypeValue") + "'");
//			updateByJava(map, findMaxSql);
			updateByMysql(map, findMaxSql);
		}

	}

	public void updateByMysql(final Map<String, Object> map, final StringBuffer findMaxSql) {
		JDBCDao jdbcDao = (JDBCDao) ApplicationUtil.getBean("JDBCDao");
		String recordID = StringUtil.getString(map.get("RecordID"));
		try {
			Integer PeriodCount = IntegerUtil.getInteger(map.get("PeriodCount"));
			String startDate = null;
			String lastDate = "IFNULL(DATE_FORMAT((" + findMaxSql.toString() + "),'%Y-%m-%d'), LastPeriod)";
			if ("1d".equalsIgnoreCase(StringUtil.getString(map.get("OptionAttrib")))) {
				startDate = "IFNULL(DATE_SUB((" + findMaxSql.toString() + "), INTERVAL " + ((PeriodCount * 1) - 1) + " DAY), StartPeriod)";
			}
			if ("1m".equalsIgnoreCase(StringUtil.getString(map.get("OptionAttrib")))) {
				startDate = "IFNULL(DATE_ADD(LAST_DAY(DATE_SUB((" + findMaxSql.toString() + "), INTERVAL " + (PeriodCount - 0) + " MONTH)), INTERVAL 1 DAY), StartPeriod)";
			}
			if (startDate == null) {
				logger.info("Readiness RecordID = [" + recordID + "], startDate is null, The readiness update continue");
				return;
			}
			// 更新Readiness
			String UPDATE_SQL_BYMYSQL = "UPDATE RC_DataItemReadiness SET LastPeriod=("+lastDate+"),StartPeriod="+startDate+" WHERE 1 = 1 AND RecordID=?";
			logger.info("Update Parameter recordID=[" + recordID + "]");
			jdbcDao.update(UPDATE_SQL_BYMYSQL, recordID);
		} catch (Exception e) {
			// do nothing
			logger.info("Readiness RecordID = [" + recordID + "] Exception[" + e + "], The readiness update continue");
		}
	}

	public void updateByJava(final Map<String, Object> map, final StringBuffer findMaxSql) {
		JDBCDao jdbcDao = (JDBCDao) ApplicationUtil.getBean("JDBCDao");
		String recordID = StringUtil.getString(map.get("RecordID"));
		try {
			Integer PeriodCount = IntegerUtil.getInteger(map.get("PeriodCount"));
			// 查询最大日期
			Map<String, Object> maxDateMap = jdbcDao.findByUnique(findMaxSql.toString());
			String maxDate = StringUtil.getString(maxDateMap.get("MaxDate"));
			if (maxDate == null) {
				logger.info("Readiness RecordID = [" + recordID + "], MaxDate is null, The readiness update continue");
				return;
			}
			String startDate = null;
			String lastDate = maxDate;
			Integer number = PeriodCount * 1;
			if ("1d".equalsIgnoreCase(StringUtil.getString(map.get("OptionAttrib")))) {
				startDate = DateUtil.subDay(lastDate, number);
			}
			if ("1m".equalsIgnoreCase(StringUtil.getString(map.get("OptionAttrib")))) {
				startDate = DateUtil.subMonth(lastDate, number);
			}
			if (startDate == null) {
				logger.info("Readiness RecordID = [" + recordID + "], startDate is null, The readiness update continue");
				return;
			}
			// 更新Readiness
			logger.info("Update Parameter lastDate=[" + lastDate + "], startDate=[" + startDate + "], recordID=[" + recordID + "]");
			jdbcDao.update(UPDATE_SQL.toString(), lastDate, startDate, recordID);
		} catch (Exception e) {
			// do nothing
			logger.info("Readiness RecordID = [" + recordID + "] Exception[" + e + "], The readiness update continue");
		}
	}

	private static final StringBuffer FIND_SQL = new StringBuffer();

	private static final StringBuffer UPDATE_SQL = new StringBuffer();

	private static final StringBuffer FIND_COUNT_SQL = new StringBuffer();

	static {

		FIND_COUNT_SQL.append("SELECT");
		FIND_COUNT_SQL.append("    COUNT(*) as count");
		FIND_COUNT_SQL.append("  FROM RC_DataItem rc_item,");
		FIND_COUNT_SQL.append("    RC_DataItemReadiness rc_itemread,");
		FIND_COUNT_SQL.append("    DT_EnumOptions dt_option");
		FIND_COUNT_SQL.append("  WHERE rc_item.RecordID = rc_itemread.ItemRID");
		FIND_COUNT_SQL.append("    AND LOG(2,rc_itemread.PeriodType) = dt_option.OptionValue");
		FIND_COUNT_SQL.append("    AND dt_option.EnumType = 'PeriodTypes'");

		FIND_SQL.append("SELECT");
		FIND_SQL.append("    rc_itemread.RecordID as RecordID,");
		FIND_SQL.append("    dt_option.OptionAttrib as OptionAttrib,");
		FIND_SQL.append("    rc_itemread.PeriodCount as PeriodCount,");
		FIND_SQL.append("    rc_itemread.PeriodDateExp as PeriodDateExp,");
		FIND_SQL.append("    rc_item.SQLFrom as SQLFrom,");
		FIND_SQL.append("    rc_item.SQLWhere as SQLWhere,");
		FIND_SQL.append("    rc_item.SQLPeriodTypeExp as SQLPeriodTypeExp,");
		FIND_SQL.append("    rc_itemread.PeriodTypeName as PeriodTypeValue");
		FIND_SQL.append("  FROM RC_DataItem rc_item,");
		FIND_SQL.append("    RC_DataItemReadiness rc_itemread,");
		FIND_SQL.append("    DT_EnumOptions dt_option");
		FIND_SQL.append("  WHERE rc_item.RecordID = rc_itemread.ItemRID");
		FIND_SQL.append("    AND LOG(2,rc_itemread.PeriodType) = dt_option.OptionValue");
		FIND_SQL.append("    AND dt_option.EnumType = 'PeriodTypes'");
		FIND_SQL.append("  ORDER BY rc_itemread.RecordID");
		FIND_SQL.append("  LIMIT ?,?");

		UPDATE_SQL.append("UPDATE RC_DataItemReadiness SET LastPeriod=?,StartPeriod=? WHERE 1 = 1 AND RecordID=?");

	}

}
