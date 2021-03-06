/*
 * @(#)LogUtil.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.frame.util;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.asiainfo.biapp.si.coc.jauth.frame.ssh.extend.SpringContextHolder;
import com.asiainfo.biapp.si.coc.jauth.log.entity.LogMonitorDetail;
import com.asiainfo.biapp.si.coc.jauth.log.service.ILogMonitorDetailService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.utils.ConfigUtil;

/**
 * Title : LogUtil
 * <p/>
 * Description : 本地日志工具类
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
@Component
public class LogUtil {

    private static Map<String, Logger> loggerMap = new HashMap<>();

    private static final String LEVEL_DEBUG = "0";
    private static final String LEVEL_ERROR = "1";
    private static final String LEVEL_INFO = "2";
    private static final String LEVEL_WARN = "3";
    private static final Integer NUM = 2000;

    private LogUtil() {
    }

    
  

    public static void debug(Object message) {
        StackTraceElement ste = getClassName();
        String className = ste.getClassName();
        String method = ste.getMethodName();
        String threadName = Thread.currentThread().getName();
        saveLog(LEVEL_DEBUG, threadName, className, method, message);
        Logger log = getLogger(className);
        if (log.isDebugEnabled()) {
            log.debug(message.toString());
        }
    }

    public static void info(Object message) {
        StackTraceElement ste = getClassName();
        String className = ste.getClassName();
        String method = ste.getMethodName();
        String threadName = Thread.currentThread().getName();
        saveLog(LEVEL_INFO, threadName, className, method, message);
        Logger log = getLogger(className);
        if (log.isInfoEnabled()) {
            log.info(message.toString());
        }
    }
    
   

    public static void warn(Object message) {
        StackTraceElement ste = getClassName();
        String className = ste.getClassName();
        String method = ste.getMethodName();
        String threadName = Thread.currentThread().getName();
        saveLog(LEVEL_WARN, threadName, className, method, message);
        Logger log = getLogger(className);
        log.warn(message.toString());
    }

    public static void error(Object message) {
        // 获取堆栈信息
        StackTraceElement ste = getClassName();
        String className = ste.getClassName();
        String method = ste.getMethodName();
        String threadName = Thread.currentThread().getName();
        saveLog(LEVEL_ERROR, threadName, className, method, message);
        Logger log = getLogger(className);
        log.error(message.toString());
    }

    public static void error(Object message, Throwable t) {
        // 获取堆栈信息
        StackTraceElement ste = getClassName();
        String className = ste.getClassName();
        String method = ste.getMethodName();
        String threadName = Thread.currentThread().getName();
        saveLog(LEVEL_ERROR, threadName, className, method, message + ":" + t.getMessage());
        Logger log = getLogger(className);
        log.error(message.toString(), t);
    }

    /**
     * 获取最开始的调用者所在类
     * 
     * @return
     */
    private static StackTraceElement getClassName() {
        Throwable th = new Throwable();
        StackTraceElement[] stes = th.getStackTrace();
        if (stes == null) {
            return null;
        }
        Integer num = 2;
        StackTraceElement ste = stes[num];
        return ste;
    }

    
    /**
     * 输出信息内部调用
     * Description: 
     *
     * @param message
     */
    private static void errorInner(Object message) {
        StackTraceElement ste = getClassName();
        String className = ste.getClassName();
        Logger log = getLogger(className);
        if (log.isInfoEnabled()) {
            log.info(message.toString());
        }
    }
    /**
     * 根据类名获得logger对象
     * 
     * @param className
     * @return
     */
    private static Logger getLogger(String className) {
    	Logger log = null;
        if (loggerMap.containsKey(className)) {
            log = loggerMap.get(className);
        } else {
            try {
            	
            	log = LoggerFactory.getLogger(Class.forName(className));
                loggerMap.put(className, log);
            } catch (ClassNotFoundException e) {
            	errorInner(e);
            }
        }
        return log;
    }

    /**
     * http远程rest调用
     * 
     * @param interfaceUrl
     * @param method
     * @param msg
     */
    private static void saveLog(String level, String threadName, String interfaceUrl, String method, Object msg) {
        String saveJauthLog = ConfigUtil.getInstance().getValue("LOC_CONFIG_APP_SAVE_JAUTH_LOG");
        if(!"true".equals(saveJauthLog)){
        	return;
        }
        String saveDebugLog = ConfigUtil.getInstance().getValue("LOC_CONFIG_APP_SAVE_ALL_DEBUG_LOG");
        if(LEVEL_DEBUG.equals(level) && !"true".equals(saveDebugLog)){
    		return ;
    	}
    	try {
        	
    		ILogMonitorDetailService logmonitorService = (ILogMonitorDetailService)SpringContextHolder.getBean("logMonitorDetailServiceImpl");
        	String localAddress = InetAddress.getLocalHost().getHostAddress();
        	LogMonitorDetail ld = new LogMonitorDetail();
        	ld.setUserId("admin");
        	ld.setIpAddr(localAddress);
        	ld.setOpTime(new Date());
        	ld.setLevelId(level);
        	ld.setThreadName(threadName);
        	ld.setInterfaceUrl(interfaceUrl + "/" + method);
        	String iMsg = "";
        	if(StringUtils.isNotBlank(msg.toString()) && msg.toString().length()>NUM){
        		iMsg = msg.toString().substring(0,NUM);
        	}else{
        		iMsg = msg.toString();
        	}
        	ld.setErrorMsg(iMsg);
            logmonitorService.saveRightNow(ld);
        } catch (Exception e) {
        	errorInner(e);
        }
    }
}
