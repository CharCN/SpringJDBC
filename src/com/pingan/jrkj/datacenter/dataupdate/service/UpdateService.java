package com.pingan.jrkj.datacenter.dataupdate.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.pingan.jrkj.datacenter.dataupdate.handle.bean.AUDepend;
import com.pingan.jrkj.datacenter.dataupdate.handle.bean.AUDependCalc;
import com.pingan.jrkj.datacenter.dataupdate.handle.bean.AUPrimary;
import com.pingan.jrkj.datacenter.dataupdate.handle.bean.AUPrimaryType;
import com.pingan.jrkj.datacenter.dataupdate.util.StringUtil;

public class UpdateService {

	private static final Logger logger = LoggerFactory.getLogger(UpdateService.class);

	private JdbcTemplate dcdsJdbcTemplate;

	public void setDcdsJdbcTemplate(JdbcTemplate dcdsJdbcTemplate) {
		this.dcdsJdbcTemplate = dcdsJdbcTemplate;
//		this.dcdsJdbcTemplate.setLazyInit(false);
	}

	public List<Map<String, Object>> find(String findSql) {
		logger.debug(findSql);
		List<Map<String, Object>> list = dcdsJdbcTemplate.queryForList(findSql);
		return list;
	}

	public int update(String updateSql) {
		logger.debug(updateSql);
		// dcdsJdbcTemplate.execute(updateSql);
		return dcdsJdbcTemplate.update(updateSql);
	}

	public void batchUpdate(String[] updateSql) {
		logger.debug("updateSqlLength:{}", updateSql.length);
		dcdsJdbcTemplate.batchUpdate(updateSql);
	}

	public void insert(String insertSql) {
		logger.debug(insertSql);
		dcdsJdbcTemplate.execute(insertSql);
	}

	public void batchInsert(String[] insertSql) {
		logger.debug("updateSqlLength:{}", insertSql.length);
		dcdsJdbcTemplate.batchUpdate(insertSql);
	}

	public void batchInsert(String insertSql, List<Object[]> batchArgs) {
		dcdsJdbcTemplate.batchUpdate(insertSql, batchArgs);
	}

	public void batchInsert2(final String sql, final List<Object[]> batchArgs) {
		dcdsJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setObject(1, batchArgs.get(i)[0]);
				ps.setObject(2, batchArgs.get(i)[1]);
			}
			public int getBatchSize() {
				return batchArgs.size();
			}
		});
	}

	public List<AUPrimary> getPrimarys() {
		logger.info("开始getPrimaryTypes");
		List<AUPrimary> primaryList = new ArrayList<AUPrimary>();
		// 查询更新的主表类型表
		List<Map<String, Object>> list0 = this.find("select TypeName,RecordID from " + AUPrimaryType.TB__NAME
				+ " where Enable > 0 order by ScanOrder asc");
		logger.info("查询[" + AUPrimaryType.TB__NAME + "]记录:" + (list0 == null ? null : list0.size()));
		for (Map<String, Object> map0 : list0) {
			String typeID = StringUtil.getString(map0.get("RecordID"));
			typeID = typeID == null || typeID.equals("") ? "-99999" : typeID;
			// 查询更新的主表
			List<Map<String, Object>> list1 = this.find("select * from " + AUPrimary.TB__NAME + " where TypeID = " + typeID + " and Enable > 0 Order by UpdateOrder asc");
			logger.info("根据typeID:" + typeID + ",查询所有primaryList,size=" + (list1 == null ? null : list1.size()));
			for (Map<String, Object> map1 : list1) {
				AUPrimary aup = new AUPrimary();
				aup.setRecordID(Long.parseLong(StringUtil.getString(map1.get("RecordID")))); // 主键
				aup.setUpdateTable(StringUtil.getString(map1.get("UpdateTable"))); // 更新表
				aup.setUpdateKey(StringUtil.getString(map1.get("UpdateKey"))); // 更新主键
				aup.setUpdateWhere(StringUtil.getString(map1.get("UpdateWhere"))); // 更新条件
				aup.setUpdateField(StringUtil.getString(map1.get("UpdateField"))); // 更新字段
				aup.setUpdateVar(StringUtil.getString(map1.get("UpdateVar"))); // 取该表变量
				// 查询更新的依赖表
				List<Map<String, Object>> list2 = this.find("select * from " + AUDepend.TB__NAME + " where PrimaryID = " + aup.getRecordID());
				List<AUDepend> dependList = new ArrayList<AUDepend>();
				for (Map<String, Object> map2 : list2) {
					AUDepend aud = new AUDepend();
					aud.setRecordID(Long.parseLong(StringUtil.getString(map2.get("RecordID"))));
					aud.setSelectTable(StringUtil.getString(map2.get("SelectTable")));
					aud.setSelectField(StringUtil.getString(map2.get("SelectField")));
					aud.setSelectWhere(StringUtil.getString(map2.get("SelectWhere")));
					// 查询依赖的数据计算公式表
					List<Map<String, Object>> list3 = this.find("select * from " + AUDependCalc.TB__NAME + " where DependID = " + aud.getRecordID()
							+ " Order by RecordID Asc, CalcOrder Asc");
					List<AUDependCalc> dependCalcList = new ArrayList<AUDependCalc>();
					for (Map<String, Object> map3 : list3) {
						AUDependCalc audc = new AUDependCalc();
						audc.setRecordID(Long.parseLong(StringUtil.getString(map3.get("RecordID"))));
						audc.setDependID(Long.parseLong(StringUtil.getString(map3.get("DependID"))));
						audc.setCalcFormulaID(Long.parseLong(StringUtil.getString(map3.get("CalcFormulaID"))));
						audc.setPlaceholder(StringUtil.getString(map3.get("Placeholder")));
						audc.setParam(StringUtil.getString(map3.get("Param")));
						audc.setRemark(StringUtil.getString(map3.get("Remark")));
						audc.setCalcOrder(Integer.parseInt(StringUtil.getString(map3.get("CalcOrder"))));
						dependCalcList.add(audc);
					}
					aud.setAuDependCalcs(dependCalcList);
					dependList.add(aud);
				}
				aup.setAuDepends(dependList);
				primaryList.add(aup);
			}
		}
		return primaryList;
	}

}
