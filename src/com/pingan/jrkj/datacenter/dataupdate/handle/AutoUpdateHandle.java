package com.pingan.jrkj.datacenter.dataupdate.handle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pingan.jrkj.datacenter.dataupdate.handle.bean.AUDepend;
import com.pingan.jrkj.datacenter.dataupdate.handle.bean.AUDependCalc;
import com.pingan.jrkj.datacenter.dataupdate.handle.bean.AUPrimary;
import com.pingan.jrkj.datacenter.dataupdate.service.UpdateService;
import com.pingan.jrkj.datacenter.dataupdate.util.ApplicationUtil;
import com.pingan.jrkj.datacenter.dataupdate.util.StringUtil;

public class AutoUpdateHandle {

	private static final Logger logger = LoggerFactory.getLogger(AutoUpdateHandle.class);

	private static final String SPACE = " ";

	private static final String PRIMARY_UPDATE_KEY_SPLIT = "###";

	private static final String PRIMARY_UPDATE_VAR_SPLIT = "###";

	private static final String PRIMARY_UPDATE_FIELD_SPLIT = "###";

	private static final String DEPEND_SELECT_FIELD_SPLIT = "###";

	public static void AutoUpdate() {
		long l1 = System.currentTimeMillis();
		// 加载计算公式
		new AUDependCalc().init();
		logger.info("Init time:{}", (System.currentTimeMillis() - l1));
		UpdateService updateService = (UpdateService) ApplicationUtil.getBean("updateService");
		List<AUPrimary> list = updateService.getPrimarys();
		for (AUPrimary aup : list) {
			// Depend查询需要计算的sql
			List<String> selectSqls = new ArrayList<String>();
			Map<String, Object> selectFieldMap = new HashMap<String, Object>();
			for (AUDepend aud : aup.getAuDepends()) {
				StringBuffer calcSql = new StringBuffer();
				calcSql.append("Select ");
				// field:$B$ = sum(t.age) * 1000 ### $A$ = sum(t.age)
				for (String field : aud.getSelectField().split(DEPEND_SELECT_FIELD_SPLIT)) {
					String[] fields = field.split("=");
					calcSql.append(fields[1].trim() + " as " + fields[0].trim() + ",");
					selectFieldMap.put(fields[0].trim(), null);
				}
				calcSql.delete(calcSql.length() - 1, calcSql.length());
				calcSql.append(" from ");
				calcSql.append(aud.getSelectTable());
				calcSql.append(SPACE);
				calcSql.append(aud.getSelectWhere());
				selectSqls.add(calcSql.toString());
				calcSql = null;
			}
			// 查询需要更新的记录sql
			StringBuffer selectSql = new StringBuffer();
			selectSql.append("Select ");
			// 更新主键
			String[] updateKeys = aup.getUpdateKey().split(PRIMARY_UPDATE_KEY_SPLIT);
			for (String key : updateKeys) {
				selectSql.append(key + ",");
			}
			// 取表变量 #ID# = id ### #NUMBER# = number
			String[] updateVars = aup.getUpdateVar().split(PRIMARY_UPDATE_VAR_SPLIT);
			for (String var : updateVars) {
				String[] vars = var.split("=");
				selectSql.append(vars[1].trim() + " as " + vars[0].trim() + ",");
			}
			selectSql.delete(selectSql.length() - 1, selectSql.length());
			selectSql.append(" from ").append(aup.getUpdateTable()).append(SPACE).append(aup.getUpdateWhere() == null ? SPACE : aup.getUpdateWhere());
			// 查询需要更新的结果集
			List<Map<String, Object>> findList = updateService.find(selectSql.toString());
			selectSql = null;
			// String[] updateSqls = new String[findList.size()];
			int updateCount = 0;
			for (Map<String, Object> map : findList) {
				// 将查询出的存为key-value形式
				Map<String, String> varKeyValue = new HashMap<String, String>();
				for (String var : updateVars) {
					String[] vars = var.split("=");
					// System.out.println(vars[0]);
					varKeyValue.put(vars[0].trim(), map.get(vars[0].trim()) == null ? "" : map.get(vars[0].trim()).toString());
					vars = null;
				}
				// System.out.println(findList);
				StringBuffer updateSql = new StringBuffer();
				updateSql.append("Update ").append(aup.getUpdateTable()).append(" set ");
				// 更新字段
				String[] updateFields = aup.getUpdateField().split(PRIMARY_UPDATE_FIELD_SPLIT);
				for (String field : updateFields) {
					updateSql.append(field + ",");
				}
				updateSql.delete(updateSql.length() - 1, updateSql.length());
				updateSql.append(" where 1=1 ");

				for (String key : updateKeys) {
					String val = map.get(key) == null || map.get(key).toString().equals("") ? "null" : map.get(key).toString();
					val = val.equals("null") ? val : "'" + val + "'";
					updateSql.append(" and " + key + "=" + val);
				}
				Map<String, Object> calcMap = new HashMap<String, Object>();
				// 取出所有变量的变量名
				for (String key : selectFieldMap.keySet()) {
					calcMap.put(key, null);
				}
				// 循环查询每一条需要计算的sql
				for (String sql : selectSqls) {
					// 循环取得变量替换需要查询的sql
					for (String key : varKeyValue.keySet()) {
						sql = StringUtil.replaceAll(sql, key, varKeyValue.get(key));
					}
					// 查询计算sql
					List<Map<String, Object>> calcList = updateService.find(sql);
					// System.out.println("calcList + " + calcList);
					if (calcList != null && calcList.size() != 0) {
						for (String mKey : calcList.get(0).keySet()) {
							// 赋值能查询出来的变量名
							calcMap.put(mKey, calcList.get(0).get(mKey));
						}
					}
					calcList = null;
				}
				// 循环替换变量
				for (AUDepend aud : aup.getAuDepends()) {
					// field:$B$ = sum(t.age) * 1000 ### $A$ = sum(t.age)
					for (String field : aud.getSelectField().split(DEPEND_SELECT_FIELD_SPLIT)) {
						String[] fields = field.split("=");
						if (calcMap.containsKey(fields[0].trim())) {
							// 计算要替换的变量
							String v = StringUtil.getString(calcMap.get(fields[0].trim()));
							String replaceStr = "";
							if (v == null && aud.getAuDependCalcs().size() == 0) {
								replaceStr = "null";
							} else {
								String calcStr = v == null ? "0" : v;
								for (AUDependCalc audc : aud.getAuDependCalcs()) {
									// 判断占位符
									if (audc.getPlaceholder().equals(fields[0].trim())) {
										String param = audc.getParam().trim();
										// 该步为判断param是否填的变量,如果param的值为$A$
										if (calcMap.containsKey(param)) {
											param = StringUtil.getString(calcMap.get(param));
										}
										calcStr = AUDependCalc.calc(audc.getCalcFormulaID(), calcStr, param);
									}
								}
								replaceStr = calcStr;
								calcStr = null;
							}
							replaceStr = replaceStr.equals("null") ? replaceStr : "'" + replaceStr + "'";
							// 替换需要更新字段的变量
							String us = StringUtil.replaceAll(updateSql.toString(), fields[0].trim(), replaceStr);
							updateSql.delete(0, updateSql.length());
							updateSql.append(us);
							replaceStr = null;
							v = null;
							us = null;
						}
						fields = null;
					}
				}
				int updateSuccessCount = updateService.update(updateSql.toString());
				updateCount += updateSuccessCount;
				logger.debug("更新[{}]条!", updateSuccessCount);
				// updateSqls[i++] = updateSql.toString();
				calcMap = null;
				updateFields = null;
				updateSql = null;
				varKeyValue = null;
			}
			logger.info("累计更新[{}]条!", updateCount);
			// updateService.batchUpdate(updateSqls);
			selectFieldMap = null;
			findList = null;
			updateKeys = null;
			updateVars = null;
			selectSqls = null;
			selectSql = null;
		}
		list = null;
	}

	public synchronized static void main(String[] args) throws Exception {
		System.out.println("||||||||||||||||||||||||"+ApplicationUtil.class.getResourceAsStream(""));
		System.out.println("||||||||||||||||||||||||"+System.getProperty("user.dir"));
		// 加载spring
		ApplicationUtil.init();
		logger.info("args:" + args);
		long l1 = System.currentTimeMillis();
		try {
			AutoUpdateHandle.AutoUpdate();
		} catch (Exception e) {
			logger.error("Execute AutoUpdateHandle ERROR[{}] E[{}]", e.getMessage(), e);
		}
		logger.info("总共使用时间:[{}ms]", (System.currentTimeMillis() - l1));
	}

}
