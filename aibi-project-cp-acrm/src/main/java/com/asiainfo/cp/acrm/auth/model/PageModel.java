package com.asiainfo.cp.acrm.auth.model;

public class PageModel {
	private Integer startRow;
	private Integer stopRow;
	private String sortCol;
	private String sortOrder;
	public Integer getStartRow() {
		return startRow;
	}
	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}
	public Integer getStopRow() {
		return stopRow;
	}
	public void setStopRow(Integer stopRow) {
		this.stopRow = stopRow;
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
	
}
