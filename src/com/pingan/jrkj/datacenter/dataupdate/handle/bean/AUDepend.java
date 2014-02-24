package com.pingan.jrkj.datacenter.dataupdate.handle.bean;

import java.util.List;

public class AUDepend {

	public static final String TB__NAME = "DW_MetaData.AU_Depend";

	private Long recordID;

	private String selectTable;

	private String selectField;

	private String selectWhere;

	private List<AUDependCalc> auDependCalcs;

	public List<AUDependCalc> getAuDependCalcs() {
		return auDependCalcs;
	}

	public void setAuDependCalcs(List<AUDependCalc> auDependCalcs) {
		this.auDependCalcs = auDependCalcs;
	}

	public Long getRecordID() {
		return recordID;
	}

	public void setRecordID(Long recordID) {
		this.recordID = recordID;
	}

	public String getSelectTable() {
		return selectTable;
	}

	public void setSelectTable(String selectTable) {
		this.selectTable = selectTable;
	}

	public String getSelectField() {
		return selectField;
	}

	public void setSelectField(String selectField) {
		this.selectField = selectField;
	}

	public String getSelectWhere() {
		return selectWhere;
	}

	public void setSelectWhere(String selectWhere) {
		this.selectWhere = selectWhere;
	}

}
