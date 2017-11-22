package com.asiainfo.cp.acrm.auth.model;

public class TokenRequestModel {
		private String appkey;

		public String getAppkey() {
			return appkey;
		}

		public void setAppkey(String appkey) {
			this.appkey = appkey;
		}

		public TokenRequestModel(String appkey) {
			super();
			this.appkey = appkey;
		}

		public TokenRequestModel() {
			super();
		}
		

}
