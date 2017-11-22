package com.asiainfo.cp.acrm.auth.model;

public class ViewRequestModel {
	private String userId;
	private String sceneId;
	private String labelId;
	private PageModel page;
	private String filter;
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
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	public PageModel getPage() {
		return page;
	}
	public void setPage(PageModel page) {
		this.page = page;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	@Override
	public String toString() {
		return "ViewRequestModel [userId=" + userId + ", sceneId=" + sceneId + ", lableId=" + labelId + ", page=" + page
				+ ", filter=" + filter + "]";
	}
	
}
