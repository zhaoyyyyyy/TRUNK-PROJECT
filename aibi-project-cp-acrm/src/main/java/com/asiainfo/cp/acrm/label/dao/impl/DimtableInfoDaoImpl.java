/*
 * @(#)LabelInfoDaoImpl.java
 * 
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.asiainfo.cp.acrm.base.dao.BaseDaoImpl;
import com.asiainfo.cp.acrm.base.utils.cache.Cacheable;
import com.asiainfo.cp.acrm.base.utils.cache.DataLoader;
import com.asiainfo.cp.acrm.base.utils.cache.ICacheClient;
import com.asiainfo.cp.acrm.label.dao.IDimtableInfoDao;
import com.asiainfo.cp.acrm.label.entity.DimtableInfo;


@Repository("DimtableInfoDaoImpl")
public class DimtableInfoDaoImpl extends BaseDaoImpl<DimtableInfo, String> implements IDimtableInfoDao ,Cacheable{

	
	private static final String CACHE_PREFIX="DIMTable_info";
	
	private static final String SPLIT="_";
	
	@Autowired
	@Qualifier("jvmCacheClient")
	private ICacheClient  iCacheClient;
	
	@Override
	public List<DimtableInfo> findDimtableInfoList() {
        Map<String, Object> params = new HashMap<>();
        StringBuffer hql = new StringBuffer("from DimtableInfo l where 1=1 ");
        return super.findListByHql(hql.toString(), params);
	}	
	
    public DimtableInfo getDimtableInfo(final String dimId) {
//    		DimtableInfo value=(DimtableInfo) iCacheClient.get(this.getKey(dimId));
//		if (value==null) {
//			value=(DimtableInfo) iCacheClient.get(this.getKey(dimId),new DataLoader<String, DimtableInfo>() {
//				public DimtableInfo load(String key) {
//					return DimtableInfoDaoImpl.this.get(dimId);
//				}
//			});
//		}
//		return value;
		return DimtableInfoDaoImpl.this.get(dimId);
    }
    
    private String getKey(String dimId) {
		return CACHE_PREFIX+SPLIT+dimId;
    }

	@Override
	public void cacheData() {
		List<DimtableInfo> result=this.findDimtableInfoList();
		for (DimtableInfo each:result) {
			iCacheClient.put(this.getKey(each.getDimId()), each);
		}
		
	}
    
}
