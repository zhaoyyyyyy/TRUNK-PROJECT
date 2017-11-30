package com.asiainfo.cp.acrm.label.vo;

public class LabelMetaDataInfo {

	private String labelId;
	private String labelName;
	private String tableName;
	private String columnName;
	private String dimtableName;
	private String dimCodeCol;
	private String dimValueCol;
	private String tableShortName;
	private String dimtableShortName;
	private String customerId;
	
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public String getDimtableShortName() {
		return dimtableShortName;
	}
	public void setDimtableShortName(String dimtableShortName) {
		this.dimtableShortName = dimtableShortName;
	}
	public String getTableShortName() {
		return tableShortName;
	}
	public void setTableShortName(String tableShortName) {
		this.tableShortName = tableShortName;
	}
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	@Override
	public String toString() {
		return "LabelMetaDataInfo [labelId=" + labelId + ", tableName=" + tableName + ", columnName=" + columnName
				+ ", dimtableName=" + dimtableName + ", dimCodeCol=" + dimCodeCol + ", dimValueCol=" + dimValueCol
				+ "]";
	}
	public LabelMetaDataInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LabelMetaDataInfo(String labelId, String columnName, String tableName, String dimtableName,
			String dimCodeCol, String dimValueCol) {
		super();
		this.labelId = labelId;
		this.columnName = columnName;
		this.tableName = tableName;
		this.dimtableName = dimtableName;
		this.dimCodeCol = dimCodeCol;
		this.dimValueCol = dimValueCol;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getDimtableName() {
		return dimtableName;
	}
	public void setDimtableName(String dimtableName) {
		this.dimtableName = dimtableName;
	}
	public String getDimCodeCol() {
		return dimCodeCol;
	}
	public void setDimCodeCol(String dimCodeCol) {
		this.dimCodeCol = dimCodeCol;
	}
	public String getDimValueCol() {
		return dimValueCol;
	}
	public void setDimValueCol(String dimValueCol) {
		this.dimValueCol = dimValueCol;
	}
	
	
}
