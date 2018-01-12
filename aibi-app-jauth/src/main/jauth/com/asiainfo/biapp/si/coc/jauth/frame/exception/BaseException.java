/*
 * @(#)BaseException.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.frame.exception;

/**
 * Title : BaseException
 * <p/>
 * Description : 异常基类
 * <p/>
 * CopyRight : CopyRight (c) 2018
 * <p/>
 * Company : 北京亚信智慧数据科技有限公司
 * <p/>
 * JDK Version Used : JDK 7.0 +
 * <p/>
 * Modification History :
 * <p/>
 * <pre>NO.    Date    Modified By    Why & What is modified</pre>
 * <pre>1    2018年1月6日    hongfb        Created</pre>
 * <p/>
 *
 * @author  hongfb
 * @version 1.0.0.2018年1月6日
 */

public class BaseException extends Exception{

    private static final long serialVersionUID = 1L;

	private String errorCode = "unknown.error";

    //用户权限异常
    public static final String USER_AUTH_CODE = "50000";
	
	
    public BaseException() {
        super();
    }


    public String getErrorCode() {
	    return this.errorCode;
	}
	
	
	public BaseException(String errorCode) {
	    this.errorCode = errorCode;
	  }
	  
	public String getMessage() {
		// 否则用errorCode查询Properties文件获得出错信息
		return this.getErrorCode();
	}
	
    
    private String msg ;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setErrorCode(String code) {
        this.errorCode = code;
    }
      
    public String getMsg(){
        return msg;
    }
	
    
    
}
