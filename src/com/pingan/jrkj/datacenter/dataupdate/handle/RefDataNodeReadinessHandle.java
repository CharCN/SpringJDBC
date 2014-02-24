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

public class RefDataNodeReadinessHandle {

	private static final Logger logger = LoggerFactory.getLogger(RefDataNodeReadinessHandle.class);

	/**
	 * 分次数刷新
	 * 
	 * @param refreshCount
	 *            每次刷新的条数
	 * @throws Exception
	 */
	public void refReadiness(Integer refreshCount) throws Exception {
		JDBCDao jdbcDao = (JDBCDao) ApplicationUtil.getBean("JDBCDao");
		StringBuffer findCountSql = new StringBuffer();
		findCountSql.append("SELECT");
		findCountSql.append("    COUNT(*) as count");
		findCountSql.append("  FROM RCK_DataNode rck_node,");
		findCountSql.append("    RCK_DataNodeReadiness rck_noderead,");
		findCountSql.append("    DT_EnumOptions dt_option");
		findCountSql.append("  WHERE rck_node.RecordID = rck_noderead.DNodeRID");
		findCountSql.append("    AND LOG(2,rck_noderead.PeriodEnum) = dt_option.OptionValue");
		findCountSql.append("    AND dt_option.EnumType = 'PeriodTypes'");
		// 查询需要更新的记录数
		Map<String, Object> m = jdbcDao.findByUnique(findCountSql.toString());
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
	 * 分页刷新Readiness
	 * 
	 * @param beginLimit
	 * @param endLimit
	 * @throws Exception
	 */
	public void refReadiness(Integer beginLimit, Integer endLimit) throws Exception {
		JDBCDao jdbcDao = (JDBCDao) ApplicationUtil.getBean("JDBCDao");
		StringBuffer findSql = new StringBuffer();
		findSql.append("SELECT");
		findSql.append("    rck_noderead.RecordID as RecordID,");
		findSql.append("    dt_option.OptionAttrib as OptionAttrib,");
		findSql.append("    rck_noderead.PeriodCount as PeriodCount,");
		findSql.append("    rck_noderead.PeriodDateExp as PeriodDateExp,");
		findSql.append("    rck_node.SQLFromWhere as SQLFromWhere,");
		findSql.append("    rck_node.SQLPeriodTypeExp as SQLPeriodTypeExp,");
		findSql.append("    rck_noderead.PeriodEnumValue as PeriodEnumValue");
		findSql.append("  FROM RCK_DataNode rck_node,");
		findSql.append("    RCK_DataNodeReadiness rck_noderead,");
		findSql.append("    DT_EnumOptions dt_option");
		findSql.append("  WHERE rck_node.RecordID = rck_noderead.DNodeRID");
		findSql.append("    AND LOG(2,rck_noderead.PeriodEnum) = dt_option.OptionValue");
		findSql.append("    AND dt_option.EnumType = 'PeriodTypes'");
		findSql.append("  ORDER BY rck_noderead.RecordID");
		findSql.append("  LIMIT " + beginLimit + "," + endLimit);
		// 查询所有要更新的记录
		List<Map<String, Object>> list = jdbcDao.find(findSql.toString());

		for (Map<String, Object> map : list) {
			StringBuffer findMaxSql = new StringBuffer();
			findMaxSql.append("SELECT DATE_FORMAT(MAX(" + map.get("PeriodDateExp") + "),'%Y-%m-%d') as MaxDate");
			findMaxSql.append(" " + map.get("SQLFromWhere"));
			findMaxSql.append("    AND " + map.get("SQLPeriodTypeExp") + "='" + map.get("PeriodEnumValue") + "'");
			String recordID = StringUtil.getString(map.get("RecordID"));
			try {
				Integer PeriodCount = IntegerUtil.getInteger(map.get("PeriodCount"));
				// 查询最大日期
				Map<String, Object> maxDateMap = jdbcDao.findByUnique(findMaxSql.toString());
				String maxDate = StringUtil.getString(maxDateMap.get("MaxDate"));
				if (maxDate == null) {
					logger.info("Readiness RecordID = [" + recordID + "], MaxDate is null, The readiness update continue");
					continue;
				}
				String startDate = null;
				String lastDate = maxDate;
				Integer number = PeriodCount * 1;
				if ("1d".equalsIgnoreCase(StringUtil.getString(map.get("OptionAttrib")))) {
					startDate = DateUtil.subDay(lastDate, number);
				}
				if ("1m".equalsIgnoreCase(StringUtil.getString(map.get("OptionAttrib")))) {
					startDate = DateUtil.subDay(lastDate, number);
				}
				if (startDate == null) {
					logger.info("Readiness RecordID = [" + recordID + "], startDate is null, The readiness update continue");
					continue;
				}
				StringBuffer updateSql = new StringBuffer("UPDATE RCK_DataNodeReadiness SET LastPeriod=?,StartPeriod=? WHERE 1 = 1 AND RecordID=?");
				// 更新Readiness
				jdbcDao.update(updateSql.toString(), lastDate, startDate, recordID);
			} catch (Exception e) {
				// do nothing
				logger.info("Readiness RecordID = [" + recordID + "] Exception[" + e + "], The readiness update continue");
				continue;
			}
		}

	}

	public static void main(String[] args) throws Exception {
		// 加载spring
		ApplicationUtil.init();
		RefDataNodeReadinessHandle handle = new RefDataNodeReadinessHandle();
		handle.refReadiness(1000);
		//
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// System.out.println(sdf.parse("20131212"));
	}

}
