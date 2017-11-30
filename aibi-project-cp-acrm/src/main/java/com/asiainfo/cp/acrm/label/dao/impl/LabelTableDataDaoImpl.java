package com.asiainfo.cp.acrm.label.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.auth.model.LabelModel;
import com.asiainfo.cp.acrm.auth.model.PageRequestModel;
import com.asiainfo.cp.acrm.base.dao.BaseDaoImpl;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.exception.SqlRunException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.label.dao.ILabelTableDataDao;
import com.asiainfo.cp.acrm.label.vo.LabelMetaDataInfo;

@Service
public class LabelTableDataDaoImpl extends BaseDaoImpl<Object, String> implements ILabelTableDataDao{

	@Override
	public LabelModel getHorizentalLabelInfoModel(String sql, LabelMetaDataInfo lableMetaDataInfo)
			throws BaseException {
		Map<String,Object> params = new HashMap<>();
		List result=null;
		try{
			result=this.findListBySql(sql, params);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage()+",sql:"+sql);
		}
		List <LabelModel> resultData=new ArrayList<LabelModel>();
        for(int i=0;i<result.size();i++) {     
            Object obj = (Object) result.get(i);
            LabelModel labelModel=new LabelModel();
            labelModel.setLabelId(lableMetaDataInfo.getLabelId());
            labelModel.setLabelName(lableMetaDataInfo.getLabelName());
            labelModel.setLabelValue((String)obj);
             //使用obj[0],obj[1],obj[2]取出属性
            resultData.add(labelModel);
        }  
        if (result==null ||result.size()==0){
        	return null;
        }
		return resultData.get(0);
	}
	
	@Override
	public List<Map<String,Object>>  getVerticalLabelInfoModel(List result, List<LabelMetaDataInfo> lableMetaDataInfos)
   {
		Map<String,Object> params = new HashMap<>();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        for(int i=0;i<result.size();i++) { 
        	Map<String ,Object> column=new HashMap<String,Object>();
        	if (lableMetaDataInfos.size()==1){
        		Object obj = (Object) result.get(0);
        		column.put(lableMetaDataInfos.get(0).getColumnName(), obj);
        	}else{
        		Object[] obj = (Object[]) result.get(i);
        		for (int j=0;j<lableMetaDataInfos.size();j++){
        			column.put(lableMetaDataInfos.get(j).getColumnName(), obj[j]);
        		}
        	}
             //使用obj[0],obj[1],obj[2]取出属性
            list.add(column);
        }
        
		return list;
	}

	@Override
	public Page<Object> findVerticalDataList(PageRequestModel pageModel,String sql) {
		int pageSize=Integer.parseInt(pageModel.getPageSize());
		int currentPage=Integer.parseInt(pageModel.getCurrentPage());
		Page<Object> page=new Page<Object>();
		page.setAutoCount(true);
		page.setPageSize(pageSize);
		page.setPageStart(currentPage);
		page.setSortCol(pageModel.getSortCol());
		page.setSortCol(pageModel.getSortCol());
		try{
			page=super.findPageBySql(page, sql, null);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage()+",sql:"+sql);
		}
		return page;
	}
	
	
	@Override
	public List findVerticalDataList(String sql) {
		Map params=new HashMap();
		List result=null;
		try{
			result=super.findListBySql(sql, params);
		}catch(Exception e){
			throw new RuntimeException(e.getMessage()+",sql:"+sql);
		}
		return result;
	}
}
