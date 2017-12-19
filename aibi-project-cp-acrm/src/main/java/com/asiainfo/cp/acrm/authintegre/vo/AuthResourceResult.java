package com.asiainfo.cp.acrm.authintegre.vo;


import java.io.Serializable;
import java.util.Map;

public class AuthResourceResult implements Serializable{

    private Integer code;
    private String msg;
    private Map data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
