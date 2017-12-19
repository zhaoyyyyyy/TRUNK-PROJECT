package com.asiainfo.cp.acrm.authintegre.vo;


import java.io.Serializable;

public class UserInfo implements Serializable{

    private String session_id;
    private String sign;
    private Object cnpost;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Object getCnpost() {
        return cnpost;
    }

    public void setCnpost(Object cnpost) {
        this.cnpost = cnpost;
    }
}
