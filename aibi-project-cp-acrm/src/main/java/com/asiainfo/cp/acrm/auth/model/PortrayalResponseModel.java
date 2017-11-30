package com.asiainfo.cp.acrm.auth.model;

import java.util.List;

public class PortrayalResponseModel {
	private String amount;
	private List<LabelModel> dataList;
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public List<LabelModel> getDataList() {
		return dataList;
	}
	public void setDataList(List<LabelModel> dataList) {
		this.dataList = dataList;
	}
	
}
