package com.asiainfo.cp.acrm.auth.model;

public class LabelModel {
	private String labelId;
	private String labelName;
	private String labelValue;
	
	public LabelModel() {
		super();
	}
	public LabelModel(String labelId, String labelName, String labelValue) {
		super();
		this.labelId = labelId;
		this.labelName = labelName;
		this.labelValue = labelValue;
	}
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	public String getLabelName() {
		if (this.labelName==null) return "";
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public String getLabelValue() {
		if (this.labelValue==null) return "";
		return labelValue;
	}
	public void setLabelValue(String labelValue) {
		this.labelValue = labelValue;
	}
	@Override
	public String toString() {
		return "LableModel [labelId=" + labelId + ", labelName=" + labelName + ", labelValue=" + labelValue + "]";
	}
	
	
}
