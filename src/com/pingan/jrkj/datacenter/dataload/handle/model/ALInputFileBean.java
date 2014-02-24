package com.pingan.jrkj.datacenter.dataload.handle.model;

import java.util.Date;
import java.util.List;

public class ALInputFileBean {

	private Integer recordId;

	private Integer typeId;

	private String fileName;

	private String filePath;

	private String fileRegExp;

	private String InputSql;

	private char splitSign;

	private Integer enable;

	private Date cTime;

	private Date mTime;

	private List<ALInputSqlFieldBean> alInputSqlFieldBeans;

	public List<ALInputSqlFieldBean> getAlInputSqlFieldBeans() {
		return alInputSqlFieldBeans;
	}

	public void setAlInputSqlFieldBeans(List<ALInputSqlFieldBean> alInputSqlFieldBeans) {
		this.alInputSqlFieldBeans = alInputSqlFieldBeans;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileRegExp() {
		return fileRegExp;
	}

	public void setFileRegExp(String fileRegExp) {
		this.fileRegExp = fileRegExp;
	}

	public String getInputSql() {
		return InputSql;
	}

	public void setInputSql(String inputSql) {
		InputSql = inputSql;
	}

	public char getSplitSign() {
		return splitSign;
	}

	public void setSplitSign(char splitSign) {
		this.splitSign = splitSign;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
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
