package com.asiainfo.cp.acrm.authintegre.vo;


import java.io.Serializable;

public class ReqStaff implements Serializable{

    private long staffid;
    private long operId;
    private long[] entId;
    private long[] privId;
    private long orgId;
    private String districtId;

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getStaffid() {
        return staffid;
    }

    public void setStaffid(long staffid) {
        this.staffid = staffid;
    }

    public long getOperId() {
        return operId;
    }

    public void setOperId(long operId) {
        this.operId = operId;
    }

    public long[] getEntId() {
        return entId;
    }

    public void setEntId(long[] entId) {
        this.entId = entId;
    }

    public long[] getPrivId() {
        return privId;
    }

    public void setPrivId(long[] privId) {
        this.privId = privId;
    }
}
