package com.asiainfo.cp.acrm.label.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.auth.model.LabelModel;
import com.asiainfo.cp.acrm.auth.model.PageRequestModel;
import com.asiainfo.cp.acrm.base.dao.BaseDaoImpl;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.label.dao.ILabelTableDataDao;
import com.asiainfo.cp.acrm.label.vo.LabelMetaDataInfo;

@Service
public class LabelTableDataDaoImpl extends BaseDaoImpl<Object, String> implements ILabelTableDataDao{
	
	private static Logger logger = LoggerFactory.getLogger(LabelTableDataDaoImpl.class);
	
	@Override
	public LabelModel getHorizentalLabelInfoModel(String sql, LabelMetaDataInfo lableMetaDataInfo)
			throws BaseException {
		Map<String,Object> params = new HashMap<>();
		List result=null;
		try{
			result=this.findListBySql(sql, params);
		}catch(Exception e){
			String errorMsg="获取纵表数据错误"+e.getMessage();
			logger.error(errorMsg,e);
			//throw new RuntimeException(errorMsg+",sql:"+sql);
			return null;
		}
		List <LabelModel> resultData=new ArrayList<LabelModel>();
        for(int i=0;i<result.size();i++) {     
            Object obj = (Object) result.get(i);
            LabelModel labelModel=new LabelModel();
            labelModel.setLabelId(lableMetaDataInfo.getLabelId());
            labelModel.setLabelName(lableMetaDataInfo.getLabelName());
            if (obj==null) {
            	labelModel.setLabelValue("");
            }else {
            		labelModel.setLabelValue(""+obj);
            }
            resultData.add(labelModel);
        }  
        if (result==null ||result.size()==0){
        		return null;
        }
		return resultData.get(0);
	}
	
	@Override
	public List<LabelModel> getHorizentalLabelInfoModels(String sql, List<LabelMetaDataInfo> lableMetaDataInfos)
			throws BaseException {
		Map<String,Object> params = new HashMap<>();
		List result=null;
		result=this.findListBySql(sql, params);
		List <LabelModel> resultData=new ArrayList<LabelModel>();
        for(int i=0;i<result.size();i++) {     
	        	if (lableMetaDataInfos.size()==1){
	            Object obj = (Object) result.get(i);
	            LabelModel labelModel=new LabelModel();
	            labelModel.setLabelId(lableMetaDataInfos.get(0).getLabelId());
	            labelModel.setLabelName(lableMetaDataInfos.get(0).getLabelName());
	            	labelModel.setLabelValue(obj==null?"":""+obj);
	            resultData.add(labelModel);
	        	}else {
	        		Object[] obj = (Object[]) result.get(i);
	        		for (int j=0;j<lableMetaDataInfos.size();j++){
	    	            LabelModel labelModel=new LabelModel();
	    	            labelModel.setLabelId(lableMetaDataInfos.get(j).getLabelId());
	    	            labelModel.setLabelName(lableMetaDataInfos.get(j).getLabelName());
	    	        		labelModel.setLabelValue(obj[j]==null?"":""+obj[j]);
                    resultData.add(labelModel);
	        		}
	        	}
        }  
		return resultData;
	}
	
	@Override
	public List<Map<String,String>>  getVerticalLabelInfoModel(List result, List<LabelMetaDataInfo> lableMetaDataInfos)
   {
		Map<String,Object> params = new HashMap<>();
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
        for(int i=0;i<result.size();i++) { 
	        	Map<String ,String> column=new HashMap<String,String>();
	        	if (lableMetaDataInfos.size()==1){
	        		Object obj = (Object) result.get(0);
	        		if (obj==null){
	        			column.put(lableMetaDataInfos.get(0).getColumnName(), "");
	        		}else{
	        			column.put(lableMetaDataInfos.get(0).getColumnName(), ""+obj);
	        		}
	        	}else{
	        		Object[] obj = (Object[]) result.get(i);
	        		for (int j=0;j<lableMetaDataInfos.size();j++){
	        			if (obj[j]==null){
	        				column.put(lableMetaDataInfos.get(j).getColumnName(), "");
	        			}else{
	        				column.put(lableMetaDataInfos.get(j).getColumnName(), ""+obj[j]);
	        			}
	        		}
	        	}
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
		page=super.findPageBySql(page, sql, null);
		return page;
	}
	
	
	@Override
	public List findVerticalDataList(String sql) {
		Map params=new HashMap();
		return super.findListBySql(sql, params);
	}
}
