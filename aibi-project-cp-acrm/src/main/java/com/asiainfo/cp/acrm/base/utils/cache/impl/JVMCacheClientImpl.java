package com.asiainfo.cp.acrm.base.utils.cache.impl;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.utils.cache.DataLoader;
import com.asiainfo.cp.acrm.base.utils.cache.ICacheClient;

@Service("jvmCacheClient")
public class JVMCacheClientImpl implements ICacheClient{
	
	private Map<String,Serializable> map=new ConcurrentHashMap();

	@Override
	public Serializable get(String key) {
		if (key == null) {
			return null;
		}
		return (Serializable) map.get(key);
	}

	@Override
	public Serializable get(String key, DataLoader dataLoader) {
		if (key == null) {
			return null;
		}
		Serializable value = get(key);
		if (value == null) {
			value = dataLoader.load(key);
			put(key, value);
		}
		return value;
	}

	@Override
	public boolean expire(String key, int seconds) {
		return false;
	}

	@Override
	public void put(String key, Serializable value) {
		if (key!=null && value!=null) {
			map.put(key, value);
		}
	}

	@Override
	public void put(String key, Serializable value, int timeToLiveSeconds) {
		this.put(key, value);
	}

	@Override
	public boolean delete(String key) {
		map.remove(key);
		return true;
	}
}
