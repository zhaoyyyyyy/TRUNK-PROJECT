package com.asiainfo.cp.acrm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.asiainfo.cp.acrm.base.utils.StringUtil;
import com.asiainfo.cp.acrm.base.utils.cache.CacheLoadRunnable;
import com.asiainfo.cp.acrm.base.utils.cache.Cacheable;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent>{
	
    public void onApplicationEvent(ContextRefreshedEvent event){
    	
    		String isDimtableCached=event.getApplicationContext().getEnvironment().getProperty("cache.isDimtableCached");
    		String cacheScope=event.getApplicationContext().getEnvironment().getProperty("cache.loadDataScope");
    		if(StringUtil.isNotEmpty(isDimtableCached)&& isDimtableCached.trim().equals("true")||cacheScope!=null) {
    			cacheUpdate(event,isDimtableCached,cacheScope);
    		}
    }

	private void cacheUpdate(ContextRefreshedEvent event,String isDimtableCached,String scope) {
		List <Cacheable> cacheList=new ArrayList<>();
		String[] scopes=null;
		if (scope!=null) {
			scopes=scope.split(",");
			for (String each:scopes) {
				cacheList.add((Cacheable)event.getApplicationContext().getBean(each));
			}
		}
		if (isDimtableCached.equals("true")) {
			cacheList.add((Cacheable)event.getApplicationContext().getBean("DimTableDataDaoImpl"));
		}
		CacheLoadRunnable cacheUpdate=new CacheLoadRunnable(cacheList);
		Thread thread = new Thread(cacheUpdate);
		thread.start();
	}
}

