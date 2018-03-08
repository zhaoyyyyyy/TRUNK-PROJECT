package com.asiainfo.cp.acrm.auth.model;

import io.swagger.annotations.ApiModelProperty;

public class ViewRequestModel {
	
	private String isCustMastCode;
	private String customerId;
	private String sceneId;
	
	@ApiModelProperty(value = "标签",required = true)
	private String labelId;
	
	@ApiModelProperty(value = "分页信息",required = false)
	private PageRequestModel pageInfo;
	
	private String filter;

	public String getIsCustMastCode() {
		return isCustMastCode;
	}
	public void setIsCustMastCode(String isCustMastCode) {
		this.isCustMastCode = isCustMastCode;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getSceneId() {
		return sceneId;
	}
	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public PageRequestModel getPageInfo() {
		return pageInfo;
	}
	public void setPageInfo(PageRequestModel pageInfo) {
		this.pageInfo = pageInfo;
	}
	@Override
	public String toString() {
		return "ViewRequestModel [customerId=" + customerId + ", sceneId=" + sceneId + ", lableId=" + labelId + ", page=" + pageInfo
				+ ", filter=" + filter + "]";
	}
	
}
