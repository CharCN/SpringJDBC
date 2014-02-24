package com.pingan.jrkj.datacenter.dataupdate.handle.bean;

public class AUCalcFormula {

	public static final String TB__NAME = "DW_MetaData.AU_CalcFormula";

	private Long recordID;

	private Long formulaTitle;

	private String formulaInterface;

	private String formulaRemark;

	public Long getRecordID() {
		return recordID;
	}

	public void setRecordID(Long recordID) {
		this.recordID = recordID;
	}

	public Long getFormulaTitle() {
		return formulaTitle;
	}

	public void setFormulaTitle(Long formulaTitle) {
		this.formulaTitle = formulaTitle;
	}

	public String getFormulaInterface() {
		return formulaInterface;
	}

	public void setFormulaInterface(String formulaInterface) {
		this.formulaInterface = formulaInterface;
	}

	public String getFormulaRemark() {
		return formulaRemark;
	}

	public void setFormulaRemark(String formulaRemark) {
		this.formulaRemark = formulaRemark;
	}

}
