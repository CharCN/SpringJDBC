package com.pingan.jrkj.datacenter.dataupdate.handle.bean;

import java.util.List;

public class AUPrimary {

	public static final String TB__NAME = "DW_MetaData.AU_Primary";

	private Long recordID;

	private String updateTable;

	private String updateField;

	private String updateVar;

	private String updateKey;

	private String updateWhere;

	private List<AUDepend> auDepends;

	public Long getRecordID() {
		return recordID;
	}

	public void setRecordID(Long recordID) {
		this.recordID = recordID;
	}

	public String getUpdateTable() {
		return updateTable;
	}

	public void setUpdateTable(String updateTable) {
		this.updateTable = updateTable;
	}

	public String getUpdateField() {
		return updateField;
	}

	public void setUpdateField(String updateField) {
		this.updateField = updateField;
	}

	public String getUpdateVar() {
		return updateVar;
	}

	public void setUpdateVar(String updateVar) {
		this.updateVar = updateVar;
	}

	public String getUpdateKey() {
		return updateKey;
	}

	public void setUpdateKey(String updateKey) {
		this.updateKey = updateKey;
	}

	public String getUpdateWhere() {
		return updateWhere;
	}

	public void setUpdateWhere(String updateWhere) {
		this.updateWhere = updateWhere;
	}

	public List<AUDepend> getAuDepends() {
		return auDepends;
	}

	public void setAuDepends(List<AUDepend> auDepends) {
		this.auDepends = auDepends;
	}

}
