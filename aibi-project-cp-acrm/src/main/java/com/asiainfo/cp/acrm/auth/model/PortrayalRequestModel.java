package com.asiainfo.cp.acrm.auth.model;

import java.util.List;

public class PortrayalRequestModel {
	
	private String isCustMastCode;
	private String customerId;
	private String sceneId;
	private List<String> labelId;
	
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
	public List<String> getLabelId() {
		return labelId;
	}
	public void setLabelId(List<String> labelId) {
		this.labelId = labelId;
	}
	@Override
	public String toString() {
		return "PortrayalRequestModel [customerId=" + customerId + ", sceneId=" + sceneId + ", lableId=" + labelId + "]";
	}
	
}
