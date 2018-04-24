/*
 * @(#)DynamicTaskExeInfoImpl.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.sysmgr.task.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.asiainfo.biapp.si.coc.jauth.frame.exception.BaseException;
import com.asiainfo.biapp.si.coc.jauth.frame.exception.UserAuthException;
import com.asiainfo.biapp.si.coc.jauth.frame.ssh.extend.SpringContextHolder;
import com.asiainfo.biapp.si.coc.jauth.frame.util.HttpUtil;
import com.asiainfo.biapp.si.coc.jauth.frame.util.LogUtil;
import com.asiainfo.biapp.si.coc.jauth.frame.util.StringUtil;
import com.asiainfo.biapp.si.coc.jauth.frame.util.WebResult;
import com.asiainfo.biapp.si.coc.jauth.log.entity.LogTaskExecuteDetail;
import com.asiainfo.biapp.si.coc.jauth.log.service.ILogTaskExecuteDetailService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.component.AppUrlComponent;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.LocTaskExeInfo;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.task.IDynamicTask;

import net.sf.json.JSONObject;

/**
 * Title : DynamicTaskExeInfoImpl
 * <p/>
 * Description : 线程池任务调度工具类里的任务接口实现类
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

@Service
@Configuration
@Scope("prototype")
public class DynamicTaskExeInfoImpl implements IDynamicTask {
    
    private static final int LOG_FIELD_DEFAULT_LEN = 2000;
    
    @Value("${demo.security.jwt.tokenSigningKey}")
    private String tokenSigningKey;
    @Value("${spring.application.name}")
    private String AppName;

    //传入参数
    private String url;
    private String token;
    private String userId;
    private LocTaskExeInfo taskExeInfo;
    
    //内部变量
    private AppUrlComponent appUrlCom;
    
    private ILogTaskExecuteDetailService logTaskExecuteDetailService;
    
    /**
     * @Description:传入参数
     * @param Map<String, Object> parameters
     */
    public void initParameters(Map<String, Object> parameters) {
        this.url = String.valueOf(parameters.get("url"));
        this.token = String.valueOf(parameters.get("token"));
        this.userId = String.valueOf(parameters.get("userId"));
        this.taskExeInfo = (LocTaskExeInfo) parameters.get("taskExeInfo");
        this.logTaskExecuteDetailService = (ILogTaskExecuteDetailService)SpringContextHolder.getBean("logTaskExecuteDetailServiceImpl");

        appUrlCom = (AppUrlComponent) SpringContextHolder.getBean("appUrlComponent");
        
        LogUtil.debug("url="+url+"token="+token+"userId="+userId+"taskExeInfo="+taskExeInfo);
    }
    
    @Override
    public void run() {
        long s = System.currentTimeMillis();
        LogUtil.debug("任务("+Thread.currentThread().getName()+")正在执行。。。"+new Date().toLocaleString());
        
        // 发送请求
        if (StringUtil.isNotEmpty(url)) {
        		if (url.contains("{") && url.contains("}")) { //有必要就自己获取真实的url
        			String realUrl = appUrlCom.getRealUrl(url);
	                if (StringUtil.isNotBlank(realUrl)) {
	            		url = realUrl;
	                } else {
	                	LogUtil.error("暂时无法调用此接口:"+url);
        				return ;
	                }
        		}
            if (null != taskExeInfo) {
                //记录日志
                LogTaskExecuteDetail log = new LogTaskExecuteDetail();
                log.setUserId(userId);
                log.setStartTime(new Date());
                log.setExeType(taskExeInfo.getExeType());
                log.setSysId(taskExeInfo.getParentExeId());
                log.setTaskExtId(String.valueOf(taskExeInfo.getTaskExeId()));
                try {
                    Map<String,Object> map = new HashMap<String, Object>();
                    if (StringUtil.isNoneBlank(taskExeInfo.getSysId())) {
                        JSONObject sysId = JSONObject.fromObject(taskExeInfo.getSysId());
                        map = (Map) JSONObject.toBean(sysId, Map.class);
                    }
    
                    if (StringUtil.isNotBlank(token) || "null".equalsIgnoreCase(token)) {
                        //有必要就自己获取token
                        LogUtil.debug("AppName:"+AppName+",tokenSigningKey:"+tokenSigningKey);
                        Map<String, Object> autoLoginUserMap = new HashMap<>();
                        autoLoginUserMap.put("username","sys_"+AppName);
                        autoLoginUserMap.put("password",tokenSigningKey);
                        
                        String autoToken = this.getTokenByUsernamePassword(autoLoginUserMap);
                        LogUtil.debug("autoToken:"+autoToken);
                        if (StringUtil.isNotBlank(autoToken)) {
                            token = autoToken;
                        } else {
                            LogUtil.warn("token不能为空");
                        }
                    }
                    map.put("token", token);
                    
                    log.setExeParams(map.toString());
                    log.setReturnMsg(this.toLen(HttpUtil.sendPost(url, map)));
                    log.setEndTime(new Date());
                    log.setStatus(String.valueOf(WebResult.Code.OK));
                } catch (Exception e) {
                    log.setReturnMsg(this.toLen(e.getMessage()));
                    log.setStatus(String.valueOf(WebResult.Code.FAIL));
                    
                    LogUtil.error("请求"+url+"出错", e);
                } finally {
                    logTaskExecuteDetailService.save(log);
                }
            } else {
                LogUtil.error("taskExeInfo不能为空");
            }
        } else {
            LogUtil.error("url不能为空");
        }
        
        LogUtil.debug("DynamicTaskExeInfoImpl.run() end.cost："+(System.currentTimeMillis()-s)+" ms。");
    }

    /** 按日志的最大字符长度截断字符串
     * @param 原始字符串
     * @return 截断字符串
     */
    private String toLen(String str) {
        if(str.length() > LOG_FIELD_DEFAULT_LEN){
            str = str.substring(0, LOG_FIELD_DEFAULT_LEN);
        }
        return str;
    }

    /**
     * 获取token
     * @param userPwdMap 必须有user和password两个key，形如：
     * <pre>
     * Map<String,Object> map = new HashMap<>();
     * map.put("username", username);
     * map.put("password", password);</pre>
     * @return tokenStr
     * @throws BaseException
     */
    private String getTokenByUsernamePassword(Map<String,Object> userPwdMap) throws BaseException{
        if(!userPwdMap.containsKey("username")){
            throw new BaseException("用户名不能为空");
        }
        if(!userPwdMap.containsKey("password")){
            throw new BaseException("密码不能为空");
        }
        
        try{
            String tokenStr = HttpUtil.sendPost(appUrlCom.getJauthAppUrl()+"/api/auth/login", userPwdMap);
            return JSONObject.fromObject(tokenStr).getString("token");
        }catch(Exception e){
            throw new UserAuthException("错误的用户名/密码");
        }
    }
    
    
}
