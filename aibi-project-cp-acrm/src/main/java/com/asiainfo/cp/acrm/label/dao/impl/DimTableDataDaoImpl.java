package com.asiainfo.cp.acrm.label.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.asiainfo.cp.acrm.base.dao.BaseDaoImpl;
import com.asiainfo.cp.acrm.base.utils.StringUtil;
import com.asiainfo.cp.acrm.base.utils.cache.Cacheable;
import com.asiainfo.cp.acrm.base.utils.cache.DataLoader;
import com.asiainfo.cp.acrm.label.dao.IDimDataDao;
import com.asiainfo.cp.acrm.label.dao.IDimtableInfoDao;
import com.asiainfo.cp.acrm.label.entity.DimtableInfo;

@Repository("DimTableDataDaoImpl")
public class DimTableDataDaoImpl extends BaseDaoImpl implements IDimDataDao,Cacheable{
	
	private static final String CACHE_PREFIX="DIM";
	
	private static final String SPLIT="_";
	
	@Autowired
	private IDimtableInfoDao dimTableInfoDAO;
	
    private String getKey(String dimTableId,String columnKey) {
    		return CACHE_PREFIX+SPLIT+dimTableId+SPLIT+columnKey;
    }
    
    private List<Object[]> findDimTableData(DimtableInfo dimTableInfo) {
    		return findDimTableData( dimTableInfo,null);
    }
    
    
    private List<Object[]> findDimTableData(DimtableInfo dimTableInfo,String key) {
    		StringBuffer hql=new StringBuffer();
		hql.append(" select ")
			.append(" ").append("t.").append(dimTableInfo.getDimCodeCol())
			.append(",").append("t.").append(dimTableInfo.getDimValueCol())
			.append(" from ").append(dimTableInfo.getDimTablename()).append(" t ");
		if (StringUtil.isNotEmpty(key)) {
			hql.append(" where t.").append(dimTableInfo.getDimCodeCol()).append("='").append(key).append("'");
		}
		List<Object[]> dimTableDatas=findListBySql(hql.toString(), new HashMap());
    		return dimTableDatas;
    }
    
    private String findDimColumnValue(String dimTableId,String key) {
		DimtableInfo dimTableInfo=dimTableInfoDAO.get(dimTableId);
		List<Object[]> result=this.findDimTableData(dimTableInfo, key);
		if (result==null ||result.size()<1) {
			return null;
		}
		Object[] dimValue=(Object[]) result.get(0);
		return ""+dimValue[1];
}
    public String getDimValue(final String dimTableId,final String columnKey) {
    		String value=(String) iCacheClient.get(this.getKey(dimTableId, columnKey));
    		if (value==null) {
    			value=(String) iCacheClient.get(this.getKey(dimTableId, columnKey),new DataLoader<String, String>() {
				public String load(String key) {
					return findDimColumnValue(dimTableId, columnKey);
				}
    			});
    		}                               
    		return value;
    }

	@Override
	public void cacheData() {
		List<DimtableInfo> list=this.dimTableInfoDAO.findDimtableInfoList();
		for (DimtableInfo each:list) {
			List<Object[]> datas=this.findDimTableData(each);
			for (Object[] eachData:datas) {
				iCacheClient.put(this.getKey(each.getDimId(), ""+eachData[0]),""+eachData[1]);
			}
		}
		
	}
}
