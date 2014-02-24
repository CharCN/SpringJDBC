package com.pingan.jrkj.datacenter.dataload.handle.model;

import java.util.Date;

public class ALInputSqlFieldBean {

	private Integer recordId;

	private Integer fileId;

	private Integer synch;

	private String fieldPointName;

	private String defaultValue;

	private String limitLength;

	private String oldFormat;

	private String newFormat;

	private String sqlKey;

	private String sqlValue;

	private String TableName;

	private Date cTime;

	private Date mTime;

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public Integer getSynch() {
		return synch;
	}

	public void setSynch(Integer synch) {
		this.synch = synch;
	}

	public String getFieldPointName() {
		return fieldPointName;
	}

	public void setFieldPointName(String fieldPointName) {
		this.fieldPointName = fieldPointName;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getLimitLength() {
		return limitLength;
	}

	public void setLimitLength(String limitLength) {
		this.limitLength = limitLength;
	}

	public String getOldFormat() {
		return oldFormat;
	}

	public void setOldFormat(String oldFormat) {
		this.oldFormat = oldFormat;
	}

	public String getNewFormat() {
		return newFormat;
	}

	public void setNewFormat(String newFormat) {
		this.newFormat = newFormat;
	}

	public String getSqlKey() {
		return sqlKey;
	}

	public void setSqlKey(String sqlKey) {
		this.sqlKey = sqlKey;
	}

	public String getSqlValue() {
		return sqlValue;
	}

	public void setSqlValue(String sqlValue) {
		this.sqlValue = sqlValue;
	}

	public String getTableName() {
		return TableName;
	}

	public void setTableName(String tableName) {
		TableName = tableName;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public Date getmTime() {
		return mTime;
	}

	public void setmTime(Date mTime) {
		this.mTime = mTime;
	}

}
