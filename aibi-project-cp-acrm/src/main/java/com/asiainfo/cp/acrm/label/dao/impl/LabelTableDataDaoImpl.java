package com.asiainfo.cp.acrm.label.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.auth.model.LabelModel;
import com.asiainfo.cp.acrm.auth.model.PageRequestModel;
import com.asiainfo.cp.acrm.base.dao.BaseDaoImpl;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.base.utils.LogUtil;
import com.asiainfo.cp.acrm.label.dao.ILabelTableDataDao;
import com.asiainfo.cp.acrm.label.service.ILabelMetaInfoService;
import com.asiainfo.cp.acrm.label.vo.LabelMetaDataInfo;

@Service
public class LabelTableDataDaoImpl  implements ILabelTableDataDao{
	
	
	@Autowired
    @Qualifier("secondaryJdbcTemplate")
    protected JdbcTemplate secondaryJdbcTemplate;

	
	@Override
	public List<LabelModel> getHorizentalLabelInfoModels(String sql, List<LabelMetaDataInfo> lableMetaDataInfos)
			throws BaseException {
		Map<String,Object> params = new HashMap<>();
		List result=null;
		result=this.findListBySql(sql, params);
		List <LabelModel> resultData=new ArrayList<LabelModel>();
        for(int i=0;i<result.size();i++) {     
        		Map<String ,Object> each=(Map<String, Object>) result.get(i);
        		for (int j=0;j<lableMetaDataInfos.size();j++){
    	            LabelModel labelModel=new LabelModel();
    	            labelModel.setLabelId(lableMetaDataInfos.get(j).getLabelId());
    	            labelModel.setLabelName(lableMetaDataInfos.get(j).getLabelName());
    	        		labelModel.setLabelValue(""+each.get(lableMetaDataInfos.get(j).getColumnName()));
                resultData.add(labelModel);
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
	        	Map<String ,Object> each=(Map<String, Object>) result.get(i);
            for(String key:each.keySet()){  
                	column.put(key, ""+(Object)(each.get(key)==null?"":each.get(key)));
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
		page=this.findPageBySql(page, sql, null);
		return page;
	}
	
	
	@Override
	public  List findVerticalDataList(String sql){
            return this.findListBySql(sql, null);
        } 
	
	public List findListBySql(String sql, Map<String,Object> params) {
		List<Map<String, Object>> dataList = secondaryJdbcTemplate.queryForList(sql);
		List<Map<String, String>> result=getDataListMapString(dataList);
		return result;
	}

	private List<Map<String,String >> getDataListMapString(List<Map<String, Object>> dataList) {
		List<Map<String, String>> result=new ArrayList<>();
		for (Map<String, Object> map : dataList) {
			
			Map<String ,String> column=new HashMap<String,String>();
            for (String key : map.keySet()) {
            		Object value=map.get(key);
	            	if (!key.contains(ILabelMetaInfoService.DIM_COLUMNVALUE_ALIAS_NAME_DEFAULT)) {
	            		column.put(key, ""+(Object)(value==null?"":value));
	            }else {
	                String tmp=key.substring(0,key.indexOf(ILabelMetaInfoService.DIM_COLUMNVALUE_ALIAS_NAME_DEFAULT));
	            		column.put(tmp, ""+(Object)(value==null?"":value));
	            }
            }
            result.add(column);
        }
		return result;
	}
	
	public Page<Object> findPageBySql(Page<Object> page, String sql, Map<String,Object> params) {
		final Map param = params;
		final String hqlF = sql;
		final int start = page.getStart()+1;
		final int max = page.getPageSize();
		
		List dataList = secondaryJdbcTemplate.queryForList(sql+" limit "+start+ ","+max);
		LogUtil.debug("========================================sql:"+sql+" limit "+start+ ","+max);
		List result=getDataListMapString(dataList);
		page.setData(result);
		final String countSql="select count(*) as cnt from ("+sql+") as t";
		
		int count = secondaryJdbcTemplate.execute(new PreparedStatementCreator() {  
		     @Override  
		     public PreparedStatement createPreparedStatement(Connection conn)  
		         throws SQLException {  
		         return conn.prepareStatement(countSql);  
		     }}, new PreparedStatementCallback<Integer>() {  
		     @Override  
		     public Integer doInPreparedStatement(PreparedStatement pstmt)  
		         throws SQLException, DataAccessException {  
		         pstmt.execute();  
		         ResultSet rs = pstmt.getResultSet();  
		         rs.next();  
		         return rs.getInt(1);  
		      }});    
		page.setTotalCount(count);
		
		return page;
	}
	
}
