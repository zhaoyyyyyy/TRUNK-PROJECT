/*
 * @(#)WebResult.java
 *
 * CopyRight (c) 2015 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.base.utils;

import com.asiainfo.cp.acrm.base.exception.BaseException;



/**
 * Title : WebResult
 * <p/>
 * Description : 
 * <p/>
 * CopyRight : CopyRight (c) 2015
 * <p/>
 * Company : 北京亚信智慧数据科技有限公司
 * <p/>
 * JDK Version Used : JDK 5.0 +
 * <p/>
 * Modification History	:
 * <p/>
 * <pre>NO.    Date    Modified By    Why & What is modified</pre>
 * <pre>1    2015-3-12    haozf        Created</pre>
 * <p/>
 *
 * @author  haozf
 * @version 1.0.0.2015-3-12
 */
public class WebResult<T extends Object> {

	private String status ;
	private String msg ;
	private T data;
	
    /**
     * Title : Code 
     * <p/>
     * Description :返回前端状态码 
     * <p/>
     * CopyRight : CopyRight (c) 2015
     * <p/>
     * Company : 北京亚信智慧数据科技有限公司
     * <p/>
     * JDK Version Used : JDK 5.0 +
     * <p/>
     * Modification History	:
     * <p/>
     * <pre>NO.    Date    Modified By    Why & What is modified</pre>
     * <pre>1    2015-3-12    haozf        Created</pre>
     * <p/>
     *
     * @author  haozf
     * @version 1.0.0.2015-3-12
     */
    public interface Code {

        /** 成功 */
        int OK = 200;

        /** 失败 */
        int FAIL = 201;

        /** 已存在 */
        int EXIST = 202;

        /** 不存在 */
        int NOT_EXIST = 203;

        /** 请求错误 */
        int BAD_REQUEST = 400;

        /** 需要认证 */
        int UNAUTHORIZED = 401;

        /** 请求被拒绝 */
        int FORBIDDEN = 403;

        /** 找不到页面 */
        int NOT_FOUND = 404;

        /** 请求超时 */
        int REQUEST_TIMEOUT = 408;

        /** 服务器内部错误 */
        int INTERNAL_SERVER_ERROR = 500;
    }
    
//    /**
//     * 返回数据Key
//     * @author haozf
//     *
//     */
//    public interface Result{
//    	/** 返回状态 */
//        String STATUS = "status";
//
//        /** 返回列表数据 */
//        String LIST = "data";
//        
//        /** 返回分页数据 */
//        String MSG = "message";
//        
//    }

    


	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	/**
     * Description: JSON返回失败
     *
     * @return
     */
    public WebResult<T> fail(String message){
//    	 Map<String, Object> result = new HashMap<String, Object>();
//         result.put(WebResult.Result.STATUS, WebResult.Code.FAIL);
//         result.put(WebResult.Result.MSG, message);
//         result.put(WebResult.Result.LIST, null);
//         return result;
    	
    	 //WebResult<T> webResult = new WebResult<T>();
    	 this.setData(null);
    	 this.setMsg(message);
    	 this.setStatus(WebResult.Code.FAIL+"");
         return this;
    }
    
    /**
     * Description: JSON返回成功
     *
     * @return 成功的JSON map
     */
    public WebResult<T> success(String message,T obj){
//    	 Map<String, Object> result = new HashMap<String, Object>();
//       result.put(WebResult.Result.STATUS, WebResult.Code.OK);
//       result.put(WebResult.Result.MSG, message);
//       result.put(WebResult.Result.LIST, obj);
    	 
    	 //WebResult<T> webResult = new WebResult<>();
    	 this.setData(obj);
    	 this.setStatus(WebResult.Code.OK+"");
    	 this.setMsg(message);
         return this;
    }
    
    /**
     * Description: JSON返回失败
     *
     * @return
     */	
    public WebResult<T> fail(BaseException baseException){
//    	 Map<String, Object> result = new HashMap<String, Object>();
//         result.put(WebResult.Result.STATUS, baseException.getErrorCode());
//         result.put(WebResult.Result.MSG, baseException.getMessage());
//         result.put(WebResult.Result.LIST, null);
//         return result;
    	 //WebResult<T> webResult = new WebResul	t<>();
    	 this.setData(null);
    	 this.setMsg(baseException.getMessage());
    	 this.setStatus(baseException.getErrorCode());
    	 return this;
    }  

}
