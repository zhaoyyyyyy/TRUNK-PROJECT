package com.asiainfo.cp.acrm.base.utils.cache;

import java.io.Serializable;

/**
 * 缓存数据加载接口
 * @author jinbh
 *
 * @param <K>
 * @param <V>
 */
public interface DataLoader<K extends Serializable, V extends Serializable>{
	/**
	 * 加载缓存数据
	 * @param key 缓存名称
	 * @return 缓存数据对象
	 */

	
	V load(K key);
}
