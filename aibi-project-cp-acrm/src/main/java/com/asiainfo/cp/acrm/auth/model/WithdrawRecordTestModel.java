package com.asiainfo.cp.acrm.auth.model;

public class WithdrawRecordTestModel {
	private String c_001;
	private String c_002;
	private String c_003;
	private Integer c_004;
	public String getC_001() {
		return c_001;
	}
	public void setC_001(String c_001) {
		this.c_001 = c_001;
	}
	public String getC_002() {
		return c_002;
	}
	public void setC_002(String c_002) {
		this.c_002 = c_002;
	}
	public String getC_003() {
		return c_003;
	}
	public void setC_003(String c_003) {
		this.c_003 = c_003;
	}

	public Integer getC_004() {
		return c_004;
	}
	public WithdrawRecordTestModel(String c_001, String c_002, String c_003, Integer c_004) {
		super();
		this.c_001 = c_001;
		this.c_002 = c_002;
		this.c_003 = c_003;
		this.c_004 = c_004;
	}
	public void setC_004(Integer c_004) {
		this.c_004 = c_004;
	}
	public WithdrawRecordTestModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "WithdrawRecordTestModel [c_001=" + c_001 + ", c_002=" + c_002 + ", c_003=" + c_003 + ", c_004=" + c_004
				+ "]";
	}
	
}
