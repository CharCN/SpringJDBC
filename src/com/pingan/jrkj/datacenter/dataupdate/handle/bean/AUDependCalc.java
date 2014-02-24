package com.pingan.jrkj.datacenter.dataupdate.handle.bean;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pingan.jrkj.datacenter.dataupdate.handle.calcformula.CalcFormula;
import com.pingan.jrkj.datacenter.dataupdate.service.UpdateService;
import com.pingan.jrkj.datacenter.dataupdate.util.ApplicationUtil;

public class AUDependCalc {

	public static final String TB__NAME = "DW_MetaData.AU_DependCalc";

	private static final Logger logger = LoggerFactory.getLogger(AUDependCalc.class);

	private Long recordID;

	private Long dependID;

	private Long calcFormulaID;

	private String placeholder;

	private String param;

	private String remark;

	private Integer calcOrder;

	public Long getDependID() {
		return dependID;
	}

	public void setDependID(Long dependID) {
		this.dependID = dependID;
	}

	public Long getRecordID() {
		return recordID;
	}

	public void setRecordID(Long recordID) {
		this.recordID = recordID;
	}

	public Long getCalcFormulaID() {
		return calcFormulaID;
	}

	public void setCalcFormulaID(Long calcFormulaID) {
		this.calcFormulaID = calcFormulaID;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCalcOrder() {
		return calcOrder;
	}

	public void setCalcOrder(Integer calcOrder) {
		this.calcOrder = calcOrder;
	}

	private static Map<String, Method> methodMap = new HashMap<String, Method>();

	private static Map<String, Class<CalcFormula>> classMap = new HashMap<String, Class<CalcFormula>>();

	public static void main(String[] args) throws Exception {
		// Class<CalcFormula> c = classMap.get("1");
		// Method m = methodMap.get("1");
		// BigDecimal bd = (BigDecimal) m.invoke(c.newInstance(), "222.123",
		// "2.321");
		// System.out.println(bd);
		BigDecimal bd1 = new BigDecimal("10");
		BigDecimal bd2 = new BigDecimal("100");
		System.out.println(bd1.divide(bd2));
	}

	public void init() {
		UpdateService updateService = (UpdateService) ApplicationUtil.getBean("updateService");
		methodMap.clear();
		classMap.clear();
		List<Map<String, Object>> list = updateService.find("select * from " + AUCalcFormula.TB__NAME);
		for (Map<String, Object> map : list) {
			try {
				@SuppressWarnings("unchecked")
				Class<CalcFormula> c = (Class<CalcFormula>) Class.forName(map.get("FormulaInterface").toString());
				Method m = c.getDeclaredMethod("execute", String.class, String.class);
				classMap.put(map.get("RecordID").toString(), c);
				methodMap.put(map.get("RecordID").toString(), m);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				logger.error("AUDependCalc init ClassNotFoundException ERROR[{}], DETAIL{}", e.getMessage(), e);
			} catch (SecurityException e) {
				e.printStackTrace();
				logger.error("AUDependCalc init SecurityException ERROR[{}], DETAIL{}", e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				logger.error("AUDependCalc init NoSuchMethodException ERROR[{}], DETAIL{}", e.getMessage(), e);
			}
		}
	}

	/**
	 * 计算
	 * 
	 * @param formulaID
	 * @param val1
	 * @param val2
	 * @return
	 */
	public static String calc(Long formulaID, String val1, String val2) {
		Class<CalcFormula> c = classMap.get(formulaID.toString());
		Method m = methodMap.get(formulaID.toString());
		String calc = null;
		try {
			calc = (String) m.invoke(c.newInstance(), val1, val2);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AUDependCalc calc ERROR[{}], DETAIL{}", e.getMessage(), e);
		}
		if (calc == null) {
			calc = val1;
		}
		return calc;
	}

}
