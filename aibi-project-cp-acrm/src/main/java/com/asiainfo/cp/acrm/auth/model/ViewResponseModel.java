package com.asiainfo.cp.acrm.auth.model;

import java.util.List;

public class ViewResponseModel<T> {
	private String amount;
	private List<T> dataList;
	private PageResponseModel pageInfo;
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public List<T> getDataList() {
		return dataList;
	}
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}
	public PageResponseModel getPageInfo() {
		return pageInfo;
	}
	public void setPageInfo(PageResponseModel pageInfo) {
		this.pageInfo = pageInfo;
	}

}
