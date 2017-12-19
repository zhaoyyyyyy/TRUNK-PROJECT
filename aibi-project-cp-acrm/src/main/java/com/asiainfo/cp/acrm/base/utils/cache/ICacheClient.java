package com.asiainfo.cp.acrm.base.utils.cache;

import java.io.Serializable;
import java.util.Map;


/**
 * 缓存客户端接口
 * @author jinbh
 *
 */
public interface ICacheClient {

	public Serializable get(String key);
	
	public Serializable get(String key, @SuppressWarnings("rawtypes") DataLoader dataLoader);
	
	boolean expire(String key, int seconds);
	
	void put(String key, Serializable value);
	
	void put(String key, Serializable value, int timeToLiveSeconds);
	
	boolean delete(String key);
	
}
