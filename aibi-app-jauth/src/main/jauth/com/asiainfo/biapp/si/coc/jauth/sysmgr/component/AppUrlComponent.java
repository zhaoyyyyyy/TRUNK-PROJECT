/*
 * @(#)AppUrlComponent.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.sysmgr.component;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import de.codecentric.boot.admin.model.Application;
import de.codecentric.boot.admin.registry.ApplicationRegistry;

/**
 * Title : AppUrlComponent
 * <p/>
 * Description : AppUrlComponent
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
 * <pre>1    2018年1月23日    zhougz        Created</pre>
 * <p/>
 *
 * @author  zhougz
 * @version 1.0.0.2018年1月23日
 */


@Component
public class AppUrlComponent {

	
	private final ApplicationRegistry registry;

	public AppUrlComponent(ApplicationRegistry registry) {
		this.registry = registry;
	}
	
	/**
	 * 
	 * Description: 如果LOC没有启动成功，会返回空（null），此场景下不应该发送请求给loc
	 *
	 * @param appName
	 * @return
	 */
	public String getEuarkeAppUrl(String appName){
		Collection<Application> applications = registry.getApplicationsByName(appName);
		for (Application application : applications) {
			return application.getServiceUrl();
		}
		return null;
	}
	
    
    /**
     *  把url替换成真实的地址
     * @param url   配置中心的地址
     * @return
     */
    public String getRealUrl(String url){
        String realUrl = null;
        
        //${LOC}/api/xxxx/refushCache 等地址替换成 spring boot admin 上面注册的地址
        Pattern r = Pattern.compile("\\{.*\\}");
        Matcher m = r.matcher(url);
        while (m.find()){
        	String appkey = m.group(0);
        	appkey = appkey.substring(1, appkey.length()-1);
        	realUrl = url.replaceAll("\\$\\{[^}]*\\}", this.getEuarkeAppUrl(appkey.toLowerCase()));
        }
        //去掉地址后面的 "/"
        if (realUrl != null && realUrl.endsWith("/")) {
        	realUrl = realUrl.substring(0, realUrl.length() - 1);
        } 
        return realUrl;
    }
    
    
}
