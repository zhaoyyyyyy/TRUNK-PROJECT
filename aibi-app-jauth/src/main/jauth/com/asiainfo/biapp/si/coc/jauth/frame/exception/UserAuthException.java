package com.asiainfo.biapp.si.coc.jauth.frame.exception;

import com.asiainfo.biapp.si.coc.jauth.frame.util.LogUtil;

/**
 * 用户权限异常
 * @author zhougz3
 *
 */
public class UserAuthException extends BaseException{
	
    private static final long serialVersionUID = 1L;
    private final String errorCode = super.USER_AUTH_CODE;
	
	public UserAuthException(String message){
		super.setMsg(message);
		super.setErrorCode(errorCode);
	}
	
	public UserAuthException(String message,Exception e){
		LogUtil.error("获取用户权限异常",e);
		super.setMsg(message);
		super.setErrorCode(errorCode);
	}
	
	public UserAuthException(){
		super.setErrorCode(errorCode);
	}
}
