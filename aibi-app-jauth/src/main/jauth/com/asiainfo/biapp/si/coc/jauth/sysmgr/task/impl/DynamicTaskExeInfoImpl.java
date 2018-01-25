/*
 * @(#)DynamicTaskExeInfoImpl.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.sysmgr.task.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.support.CronTrigger;

import com.asiainfo.biapp.si.coc.jauth.frame.util.HttpUtil;
import com.asiainfo.biapp.si.coc.jauth.frame.util.LogUtil;
import com.asiainfo.biapp.si.coc.jauth.frame.util.StringUtil;
import com.asiainfo.biapp.si.coc.jauth.frame.util.WebResult;
import com.asiainfo.biapp.si.coc.jauth.log.entity.LogTaskExecuteDetail;
import com.asiainfo.biapp.si.coc.jauth.log.service.ILogTaskExecuteDetailService;
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
 
public class DynamicTaskExeInfoImpl implements IDynamicTask {

    private String url;
    private String token;
    private String userId;
    private LocTaskExeInfo taskExeInfo;
    
    private ILogTaskExecuteDetailService logTaskExecuteDetailService;
    
    /**
     * @Description:传入参数
     * @param Map<String, Object> parameters
     */
    public void initParameters(Map<String, Object> parameters) {}
    
    public DynamicTaskExeInfoImpl() {}
    public DynamicTaskExeInfoImpl(Map<String, Object> parameters) {
        this.url = String.valueOf(parameters.get("url"));
        this.token = String.valueOf(parameters.get("token"));
        this.userId = String.valueOf(parameters.get("userId"));
        this.taskExeInfo = (LocTaskExeInfo) parameters.get("taskExeInfo");

        this.logTaskExecuteDetailService = (ILogTaskExecuteDetailService) parameters.get("logTaskExecuteDetailService");
        LogUtil.debug("url="+url+"token="+token+"userId="+userId
            +"taskExeInfo="+taskExeInfo+"logTaskExecuteDetailService="+logTaskExecuteDetailService);
    }
    
    @Override
    public void run() {
        long s = System.currentTimeMillis();
        LogUtil.debug("任务("+Thread.currentThread().getName()+")正在执行。。。"+new Date().toLocaleString());
        
        // 发送请求
        if (null != url) {
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
    
                    if (null != token) {
                        map.put("token", token);
                    } else {
                        LogUtil.warn("token不能为空");
                    }
                    
                    log.setExeParams(map.toString());
                    
                    String httpRes = HttpUtil.sendPost(url, map);
                    
                    log.setReturnMsg(httpRes);
                    log.setEndTime(new Date());
                    log.setStatus(String.valueOf(WebResult.Code.OK));
                } catch (Exception e) {
                    log.setReturnMsg(e.getMessage());
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
        
        LogUtil.debug("DynamicTaskExeInfoImpl.run() end.耗时："+((System.currentTimeMillis()-s)/1000.0)+"秒。");
    }
    
    
    
}
