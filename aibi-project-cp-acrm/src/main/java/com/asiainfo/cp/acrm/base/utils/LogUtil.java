package com.asiainfo.cp.acrm.base.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class LogUtil {

    private static Map<String, Logger> loggerMap = new HashMap<String, Logger>();

    private static final String LEVEL_DEBUG = "0";
    private static final String LEVEL_ERROR = "1";
    private static final String LEVEL_INFO = "2";
    private static final String LEVEL_WARN = "3";

    
    private static String jauthUrl;

    @Value("${jauth-url}")
    public void setJauthUrl(String jauthUrl) {
        this.jauthUrl = jauthUrl;
    }
    private static String nodeName;

    @Value("${spring.application.name}")
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public static void debug(Object message) {
        StackTraceElement ste = getClassName();
        String className = ste.getClassName();
        String method = ste.getMethodName();
        String threadName = Thread.currentThread().getName();
        saveLog(LEVEL_DEBUG, threadName,className, method, message);
        Logger log = getLogger(className);
        if (log.isDebugEnabled()) {
            log.debug(message);
        }
    }

    public static void info(Object message) {
        StackTraceElement ste = getClassName();
        String className = ste.getClassName();
        String method = ste.getMethodName();
        String threadName = Thread.currentThread().getName();
        saveLog(LEVEL_INFO, threadName,className, method, message);
        Logger log = getLogger(className);
        if (log.isInfoEnabled()) {
            log.info(message);
        }
    }

    public static void warn(Object message) {
        StackTraceElement ste = getClassName();
        String className = ste.getClassName();
        String method = ste.getMethodName();
        String threadName = Thread.currentThread().getName();
        saveLog(LEVEL_WARN, threadName,className, method, message);
        Logger log = getLogger(className);
        log.warn(message);
    }

    public static void error(Object message) {
        // 获取堆栈信息
        StackTraceElement ste = getClassName();
        String className = ste.getClassName();
        String method = ste.getMethodName();
        String threadName = Thread.currentThread().getName();
        saveLog(LEVEL_ERROR, threadName,className, method, message);
        Logger log = getLogger(className);
        log.error(message);
    }

    public static void error(Object message, Throwable t) {
        // 获取堆栈信息
        StackTraceElement ste = getClassName();
        String className = ste.getClassName();
        String method = ste.getMethodName();
        String threadName = Thread.currentThread().getName();
        saveLog(LEVEL_ERROR, threadName,className, method, message+":"+t.getMessage());
        Logger log = getLogger(className);
        log.error(message, t);
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
        StackTraceElement ste = stes[2];
        return ste;
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
                log = Logger.getLogger(Class.forName(className));
                loggerMap.put(className, log);
            } catch (ClassNotFoundException e) {
            	LogUtil.error("记录日志信息异常",e);
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
    private static void saveLog(String level,String threadName, String interfaceUrl, String method, Object msg) {
    	 try {
         	
         	HttpServletRequest request = null;
         	String url = "local";
         	String userId = "loc_sys";
         	String token = "loc_sys";
         	try {
         		request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
         		url = request.getRequestURL()+"/"+ request.getRequestURI();
         	} catch (Exception e) {
         		  StackTraceElement ste = getClassName();
         	      String className = ste.getClassName();
         	      Logger log = getLogger(className);
         	      log.info("系统调用无法获取用户信息",e);
 			}
         	
            
         	 // 组装http远程调用
             Map<String, Object> params = new HashMap<>();

             params.put("userId", userId);
             params.put("ipAddr", url);
             params.put("token", token);
             params.put("opTime",DateUtil.date2String(new Date()));
             params.put("sysId", nodeName);
             params.put("nodeName", nodeName);
             params.put("levelId", level);
             params.put("threadName", threadName);
             params.put("interfaceUrl", interfaceUrl + "/" + method);
             params.put("errorMsg", msg);

             HttpUtil.sendPost(jauthUrl + "/api/log/monitor/save", params);
         } catch (Exception e) {
         	StackTraceElement ste = getClassName();
   	      	String className = ste.getClassName();
   	      	Logger log = getLogger(className);
   	      	log.error("给JAUTH同步日志出错",e);
         }
    }

}
