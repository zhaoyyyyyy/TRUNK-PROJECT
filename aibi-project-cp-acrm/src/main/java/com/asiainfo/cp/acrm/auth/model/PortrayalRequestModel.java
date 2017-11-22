package com.asiainfo.cp.acrm.auth.model;

import java.util.List;

public class PortrayalRequestModel {
	private String userId;
	private String sceneId;
	private List<String> labelId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
		return "PortrayalRequestModel [userId=" + userId + ", sceneId=" + sceneId + ", lableId=" + labelId + "]";
	}
	
}
