package com.asiainfo.biapp.si.coc.jauth.sysmgr.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.asiainfo.biapp.si.coc.jauth.frame.dao.BaseDao;
import com.asiainfo.biapp.si.coc.jauth.frame.page.JQGridPage;
import com.asiainfo.biapp.si.coc.jauth.frame.service.impl.BaseServiceImpl;
import com.asiainfo.biapp.si.coc.jauth.frame.ssh.extend.SpringContextHolder;
import com.asiainfo.biapp.si.coc.jauth.frame.util.LogUtil;
import com.asiainfo.biapp.si.coc.jauth.frame.util.StringUtil;
import com.asiainfo.biapp.si.coc.jauth.log.service.ILogTaskExecuteDetailService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.component.AppUrlComponent;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.component.DynamicTaskComponent;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.dao.LocTaskExeInfoDao;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Coconfig;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.LocTaskExeInfo;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.CoconfigService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.LocTaskExeInfoService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.task.impl.DynamicTaskExeInfoImpl;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.utils.SessionInfoHolder;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.vo.LocTaskExeInfoVo;

/**
 * @describe
 * @author zhangnan
 * @date 2017年10月23日 下午1:58:53
 */
@Service
@Transactional
public class LocTaskExeInfoServiceImpl extends BaseServiceImpl<LocTaskExeInfo, String> implements LocTaskExeInfoService {

	public static final String CONFIG_LOG_TASK_ID = "LOC_CONFIG_SYS_TIMED_TASK_LOG_TASK_SAVE";
	
    @Autowired
    private ILogTaskExecuteDetailService logTaskExecuteDetailService;
    @Autowired
    private SessionInfoHolder sessionInfoHolder;
    @Autowired
    private CoconfigService coconfigService;
    
	@Autowired
	private LocTaskExeInfoDao locTaskExeInfoDao;
	@Override
	protected BaseDao<LocTaskExeInfo, String> getBaseDao() {
		return locTaskExeInfoDao;
	}

	public LocTaskExeInfo findOneByName(String taskExeName) {
		return locTaskExeInfoDao.findOneByName(taskExeName);
	}
	
	public JQGridPage<LocTaskExeInfo> findLocTaskExeInfoList(JQGridPage<LocTaskExeInfo> page, LocTaskExeInfoVo locTaskExeInfoVo){
		return locTaskExeInfoDao.findLocTaskExeInfoList(page, locTaskExeInfoVo);
	}


    public boolean taskExeInfoSchedule(String token,boolean isSchedule, LocTaskExeInfo locTask, Long ms) {
        boolean res = false;

        if (null != locTask) {
            Coconfig coconfig = coconfigService.getCoconfigByKey(locTask.getTaskId());
            if (null != coconfig) {
                String url = coconfig.getConfigVal();
                if (null != url) {
                    AppUrlComponent appUrlCom = (AppUrlComponent) SpringContextHolder.getBean("appUrlComponent");
                    url = appUrlCom.getRealUrl(url);
                    Map<String, Object> map = new HashMap<>();
                    if (null != url) {
                        map.put("url",url);
                        map.put("token",token);
                        map.put("taskExeInfo",locTask);
                        if (null != sessionInfoHolder.getLoginUser()) {
                            map.put("userId", sessionInfoHolder.getLoginUser().getUserName());
                        }
                        map.put("logTaskExecuteDetailService",logTaskExecuteDetailService);

                        if (this.isValidExpression(locTask.getTaskExeTime())) {
                            DynamicTaskExeInfoImpl task = new DynamicTaskExeInfoImpl(map);
                            
                            if (isSchedule) {   //调度任务
                                //启动调度任务
                                String localAddress = null;
	                    			try {
	                    				localAddress = InetAddress.getLocalHost().getHostAddress();
	                    			} catch (UnknownHostException e) {
	                    				LogUtil.error("获取本机ipv4错误！", e);
	                    			}
	                    			
                        			Coconfig ipConfig = coconfigService.getCoconfigByKey("LOC_CONFIG_APP_JAUTH_MASTER_IP");
                    				LogUtil.debug("本机ip:"+localAddress+" | JAUTH主机ip:"+ipConfig.getConfigVal());
                    				
	                    			//JAUTH是单机吗？true:单机，false:多机
                            		if (null == ipConfig || (null!=ipConfig.getConfigVal()&&"127.0.0.1".equals(ipConfig.getConfigVal()))) {	

	                    				LogUtil.debug("JAUTH是单机部署,启动所有任务。。。");
	                    			
                                    //启动调度任务
                                    DynamicTaskComponent dSTaskUtil = (DynamicTaskComponent)SpringContextHolder.getBean("dynamicTaskComponent");
                                    dSTaskUtil.startTask(String.valueOf(locTask.getTaskExeId()), task, locTask.getTaskExeTime().trim());
                                    res = true;
                            		} else {		//JAUTH是多机部署

	                    				LogUtil.debug("JAUTH是多机部署。。。");
    	                    			
                            			if (ipConfig.getConfigVal().equals(localAddress)) {	//本机是主机
                                        //启动调度任务
                                        DynamicTaskComponent dSTaskUtil = (DynamicTaskComponent)SpringContextHolder.getBean("dynamicTaskComponent");
                                        dSTaskUtil.startTask(String.valueOf(locTask.getTaskExeId()), task, locTask.getTaskExeTime().trim());
                                        res = true;
                            			} else {		//本机是备份机
                            				if (CONFIG_LOG_TASK_ID.equals(locTask.getTaskId())) {	//自启动日志任务
                                            //启动调度任务
                                            DynamicTaskComponent dSTaskUtil = (DynamicTaskComponent)SpringContextHolder.getBean("dynamicTaskComponent");
                                            dSTaskUtil.startTask(String.valueOf(locTask.getTaskExeId()), task, locTask.getTaskExeTime().trim());
                                            res = true;
                            				}
                            			}
                            		}
                            } else {    //立即/延迟执行任务
                                if (ms > 0) {
                                    try {
                                        Thread.sleep(ms);
                                    } catch (InterruptedException e) {
                                        LogUtil.error("error in sleep!");
                                    }
                                }
                                new Thread(task).start();
                                res = true;
                            }
                        } else {
                            LogUtil.error("执行参数 ["+locTask.getTaskExeTime()+"] 不是Cron表达式！");
                        }
                    } else {
                        LogUtil.error("url不能个为空！");
                    }
                } else {
                    LogUtil.error(locTask.getTaskId()+"的配置项的ConfigVal不能为空");
                }
            } else {
                LogUtil.error(locTask.getTaskId()+"的配置项不能为空");
            }
        } else {
            LogUtil.error("LocTaskExeInfo不能为空");
        }
        
        return res;
    }

    
    /**
     *  判断是否是有效的cron表达式
     * @param cronExpression    cron表达式
     * @return
     */
    private boolean isValidExpression(final String cronExpression){
        boolean res = true;
        
        if (StringUtil.isNotBlank(cronExpression)) {
            try {
                new CronTrigger(cronExpression);
            } catch (Exception e) {
                res = false;
            } 
        } else {
            res = false;
        }
        
        return res;
    }
    
    
}
