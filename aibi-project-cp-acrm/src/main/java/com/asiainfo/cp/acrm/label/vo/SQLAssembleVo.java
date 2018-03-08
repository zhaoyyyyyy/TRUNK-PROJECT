package com.asiainfo.cp.acrm.label.vo;

/**
 * 
 * @author jinbh
 *
 */
public class SQLAssembleVo {
	
	public final static String IS_CUST_MAST_CODE_TRUE="1";
	public final static String IS_CUST_MAST_CODE_FALSE="0";
	private String isCustMastCode;
	private String userId;
	private String filter;
	private String sortCol;
	private String sortOrder;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getSortCol() {
		return sortCol;
	}
	public void setSortCol(String sortCol) {
		this.sortCol = sortCol;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getIsCustMastCode() {
		return isCustMastCode;
	}
	public void setIsCustMastCode(String isCustMastCode) {
		this.isCustMastCode = isCustMastCode;
	}
	
}
