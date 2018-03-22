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

	public static final Map<String,String>  CONFIG_LOG_TASK_IDS = new HashMap<>();
	
    @Autowired
    private ILogTaskExecuteDetailService logTaskExecuteDetailService;
    @Autowired
    private SessionInfoHolder sessionInfoHolder;
    @Autowired
    private CoconfigService coconfigService;
    
	public LocTaskExeInfoServiceImpl() {
		CONFIG_LOG_TASK_IDS.put("LOC_CONFIG_SYS_TIMED_TASK_LOG_TASK_SAVE", "interface");
		CONFIG_LOG_TASK_IDS.put("LOC_CONFIG_SYS_TIMED_TASK_MONITOR_LOG_TASK_SAVE", "monitor");
	}


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

                        if (this.isValidExpression(locTask.getTaskExeTime())) {
                            DynamicTaskExeInfoImpl task = new DynamicTaskExeInfoImpl(map);
                            
                            if (isSchedule) {   //调度任务
                                //启动调度任务
                                String localAddress = null;
	                    			try {
	                    				localAddress = InetAddress.getLocalHost().getHostAddress();
	                    			} catch (UnknownHostException e) {
	                    				LogUtil.error("get localhost ipv4 error!", e);
	                    			}
	                    			
                        			Coconfig ipConfig = coconfigService.getCoconfigByKey("LOC_CONFIG_SYS_JAUTH_MASTER_IP");
                    				LogUtil.debug("localhost ip is :"+localAddress+" | JAUTH host Ip is :"+ipConfig.getConfigVal());
                    				
	                    			//JAUTH是单机吗？true:单机，false:多机
                            		if (null == ipConfig || (null!=ipConfig.getConfigVal()&&"127.0.0.1".equals(ipConfig.getConfigVal()))) {	

	                    				LogUtil.debug("JAUTH is simple,start all task。。。");
	                    			
                                    //启动调度任务
                                    DynamicTaskComponent dSTaskUtil = (DynamicTaskComponent)SpringContextHolder.getBean("dynamicTaskComponent");
                                    dSTaskUtil.startTask(String.valueOf(locTask.getTaskExeId()), task, locTask.getTaskExeTime().trim());
                                    res = true;
                            		} else {		//JAUTH是多机部署

	                    				LogUtil.debug("JAUTH is Computer cluster。。。");
    	                    			
                            			if (ipConfig.getConfigVal().equals(localAddress)) {	//本机是主机
                                        //启动调度任务
                                        DynamicTaskComponent dSTaskUtil = (DynamicTaskComponent)SpringContextHolder.getBean("dynamicTaskComponent");
                                        dSTaskUtil.startTask(String.valueOf(locTask.getTaskExeId()), task, locTask.getTaskExeTime().trim());
                                        res = true;
                            			} else {		//本机是备份机
                            				//自启动日志任务
                            				if (CONFIG_LOG_TASK_IDS.containsKey(locTask.getTaskId())) {	
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
                            LogUtil.error("Execute parameters ["+locTask.getTaskExeTime()+"] is not Cron!");
                        }
                    } else {
                        LogUtil.error(coconfig.getConfigVal() +"getRealUrl is error!");
                    }
                } else {
                    LogUtil.error("ConfigVal of "+locTask.getTaskId()+" is null!");
                }
            } else {
                LogUtil.error("not find task, taskId is "+locTask.getTaskId());
            }
        } else {
            LogUtil.error("LocTaskExeInfo is null!");
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
