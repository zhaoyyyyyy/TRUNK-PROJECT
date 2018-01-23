/*
 * @(#)AppUrlComponent.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.sysmgr.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import com.netflix.eureka.resources.StatusResource;
import com.netflix.eureka.util.StatusInfo;

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

	@Value("${jauth-url}")  
    private String jauthUrl; 
	
	/**
	 * 
	 * Description: 如果LOC没有启动成功，会返回空（null），此场景下不应该发送请求给loc
	 *
	 * @param appName
	 * @return
	 */
	public String getEuarkeAppUrl(String appName){
		if("JAUTH".equals(appName)){
			return getJauthAppUrl();
		}
		PeerAwareInstanceRegistry PeerAwareInstanceRegistry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
		List<Application> sortedApplications = PeerAwareInstanceRegistry.getSortedApplications();
		for (Application app : sortedApplications) {
			List<InstanceInfo> list = app.getInstances();
			for (InstanceInfo instan : list) {
				if(appName.equals(instan.getAppName())){
					return instan.getHomePageUrl();
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * Description: 
	 *
	 * @param appName
	 * @return
	 */
	public String getLocAppUrl(){
		return getEuarkeAppUrl("LOC");
	}
	
	/**
	 * 
	 * Description: 如果LOC没有启动成功，会返回空（null），此场景下不应该发送请求给loc
	 *
	 * @param appName
	 * @return
	 */
	public String getJauthAppUrl(){
		StatusInfo statusInfo;
		try {
			statusInfo = new StatusResource().getStatusInfo();
		}catch (Exception e) {
			statusInfo = StatusInfo.Builder.newBuilder().isHealthy(false).build();
		}
		return statusInfo.getInstanceInfo().getHomePageUrl()+statusInfo.getInstanceInfo().getVIPAddress();
	}
}
