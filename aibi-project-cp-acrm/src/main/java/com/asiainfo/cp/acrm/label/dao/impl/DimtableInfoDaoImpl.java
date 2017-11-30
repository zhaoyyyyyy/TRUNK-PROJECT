/*
 * @(#)LabelInfoDaoImpl.java
 * 
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.asiainfo.cp.acrm.base.dao.BaseDaoImpl;
import com.asiainfo.cp.acrm.label.dao.IDimtableInfoDao;
import com.asiainfo.cp.acrm.label.entity.DimtableInfo;


@Repository
public class DimtableInfoDaoImpl extends BaseDaoImpl<DimtableInfo, String> implements IDimtableInfoDao {

	private static final Object syncobj = new Object();
	
	/** 所有维表数据 key=dimId **/
	private static Map<String,DimtableInfo> dimTableMap;
	
	@Override
    public DimtableInfo getDimtableInfo(String dimId) {
    	if(dimTableMap == null) loadDimTableValue(false);
    	return dimTableMap.get(dimId);
    }

    private void loadDimTableValue(boolean isReload) {
		synchronized(syncobj) {
			if(dimTableMap != null&&!isReload) return ;
			List<DimtableInfo> list = this.findDimtableInfoList();
			
			Map<String, DimtableInfo> map = new HashMap<String, DimtableInfo>();
			for(int i=0; i<list.size(); i++) {
				DimtableInfo v = list.get(i);
				map.put(v.getDimId(), v);
			}
			dimTableMap = map;
		}
	}
    
    @Override
    public DimtableInfo getDimtableInfoReload(String dimId) {
    	loadDimTableValue(true);
    	return dimTableMap.get(dimId);
    }

	@Override
	public List<DimtableInfo> findDimtableInfoList() {
        Map<String, Object> params = new HashMap<>();
        StringBuffer hql = new StringBuffer("from DimtableInfo l where 1=1 ");
        return super.findListByHql(hql.toString(), params);
	}	
    
}
