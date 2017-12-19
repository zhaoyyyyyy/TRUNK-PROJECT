package com.asiainfo.cp.acrm.base.utils.cache;

import java.util.List;

import com.asiainfo.cp.acrm.base.utils.LogUtil;


//缓存加载异步服务
public class CacheLoadRunnable implements Runnable {
	
	private List<Cacheable> needCacheDataDaos;
	
    public CacheLoadRunnable(List<Cacheable> needCacheDataDaos) {
		super();
		this.needCacheDataDaos = needCacheDataDaos;
	}

	@Override
    public void run() {
        runTask(); 
    }
 
    private void runTask() {
            for (Cacheable each:needCacheDataDaos) {
            		try {
            			each.cacheData();
            		}catch(Exception e) {
            			LogUtil.error("更新缓存数据错误:"+each.getClass().getName(), e);
            			continue;
            		}
            }
    }
 
}
