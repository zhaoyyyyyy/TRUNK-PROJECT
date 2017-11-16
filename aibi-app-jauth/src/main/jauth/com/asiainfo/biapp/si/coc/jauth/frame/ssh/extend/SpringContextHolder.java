
package com.asiainfo.biapp.si.coc.jauth.frame.ssh.extend;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhougz
 * @date 2013-5-22
 */
@Configuration
public class SpringContextHolder implements ApplicationContextAware {
    // Spring应用上下文环境  
    private static ApplicationContext applicationContext;
    /** 
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境 
     *  
     * @param applicationContext 
     */  
    public void setApplicationContext(ApplicationContext applicationContext) {  
    	SpringContextHolder.applicationContext = applicationContext;  
    }  
    /** 
     * @return ApplicationContext 
     */  
    public static ApplicationContext getApplicationContext() {  
        return applicationContext;  
    }  
    /** 
     * 获取对象 
     *  
     * @param name 
     * @return Object
     * @throws BeansException
     */  
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);  
    }  
}