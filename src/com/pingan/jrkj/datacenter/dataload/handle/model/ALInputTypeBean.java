package com.pingan.jrkj.datacenter.dataload.handle.model;

import java.util.Date;

public class ALInputTypeBean {

	private Integer recordId;

	private String typeName;

	private String typeExt;

	private String JobName;

	private Date cTime;

	private Date mTime;

	private Integer enable;

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeExt() {
		return typeExt;
	}

	public void setTypeExt(String typeExt) {
		this.typeExt = typeExt;
	}

	public String getJobName() {
		return JobName;
	}

	public void setJobName(String jobName) {
		JobName = jobName;
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

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

}
