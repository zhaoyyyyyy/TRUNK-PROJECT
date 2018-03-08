package com.asiainfo.cp.acrm.auth.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.auth.model.LabelModel;
import com.asiainfo.cp.acrm.auth.model.PageRequestModel;
import com.asiainfo.cp.acrm.auth.model.PortrayalRequestModel;
import com.asiainfo.cp.acrm.auth.model.ViewRequestModel;
import com.asiainfo.cp.acrm.auth.service.ICustomerService;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.exception.ParamRequiredException;
import com.asiainfo.cp.acrm.base.exception.SqlRunException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.base.utils.LogUtil;
import com.asiainfo.cp.acrm.base.utils.StringUtil;
import com.asiainfo.cp.acrm.label.service.ILabelMetaInfoService;
import com.asiainfo.cp.acrm.label.service.ILabelTableDataService;
import com.asiainfo.cp.acrm.label.vo.LabelMetaDataInfo;
import com.asiainfo.cp.acrm.label.vo.SQLAssembleVo;

@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService{
	
	@Autowired
    private ILabelMetaInfoService labelMetaInfoSvc;
    
    @Autowired
	private ILabelTableDataService labelTableDataSvc;

	@Override
	public List<LabelModel> getLabelModels(PortrayalRequestModel reqModel) throws BaseException {
		if(StringUtil.isEmpty(reqModel.getCustomerId())){
			throw new ParamRequiredException("客户Id不能为空");
		}
		String isCustMastId=reqModel.getIsCustMastCode();
		if (StringUtil.isNotEmpty(isCustMastId)) {
			if (!isCustMastId.equals(SQLAssembleVo.IS_CUST_MAST_CODE_FALSE)
					&& !isCustMastId.equals(SQLAssembleVo.IS_CUST_MAST_CODE_TRUE)) {
				throw new ParamRequiredException("是否为客户主码参数值错误，当前值："+isCustMastId);
			}
		}
		if(reqModel.getLabelId()==null ||reqModel.getLabelId().size()==0){
			throw new ParamRequiredException("标签Id不能为空");
		}
		for(String labelId:reqModel.getLabelId()){
			if(StringUtil.isEmpty(labelId)){
				throw new ParamRequiredException("标签Id不能为空");
			}
		}
		List<LabelModel> resultData=new ArrayList<LabelModel>();
		List<LabelMetaDataInfo> labelMetaDataInfos=labelMetaInfoSvc.getHorizentalLabelMetaInfos(reqModel.getLabelId());
		Map<String, List<LabelMetaDataInfo>> tableToLabelMetaDataInfos = getTableToLabelMetaDataInfosMap(
				labelMetaDataInfos);
		Set<Entry<String,List<LabelMetaDataInfo>>> s = tableToLabelMetaDataInfos.entrySet();
		for(Entry<String,List<LabelMetaDataInfo>> e : s) {
			String sql=labelMetaInfoSvc.getTableSQL(reqModel,e.getValue());
			List<LabelModel> models=null;
			try {
				models=labelTableDataSvc.findHorizentalLabelInfoModels(sql, e.getValue());
			}catch(Exception ex) {
				String errorMsg="获取宽表数据错误"+ex.getMessage()+",sql:"+sql;
				LogUtil.error(errorMsg,ex);
				throw new RuntimeException(errorMsg);
			}
			resultData.addAll(models);
		}
		return resultData;
	}

	private Map<String, List<LabelMetaDataInfo>> getTableToLabelMetaDataInfosMap(
			List<LabelMetaDataInfo> labelMetaDataInfos) {
		Map <String,List<LabelMetaDataInfo>> tableToLabelMetaDataInfos=new HashMap<String,List<LabelMetaDataInfo>>();
		for (LabelMetaDataInfo each:labelMetaDataInfos) {
			String tableName=each.getTableName();
			if (tableToLabelMetaDataInfos.containsKey(tableName)) {
				tableToLabelMetaDataInfos.get(tableName).add(each);
			}else {
				List<LabelMetaDataInfo> list=new ArrayList<>();
				list.add(each);
				tableToLabelMetaDataInfos.put(tableName, list);
			}
		}
		return tableToLabelMetaDataInfos;
	}
	
	@Override
	public Page<Object> view(ViewRequestModel reqModel) throws BaseException {
		if(StringUtil.isEmpty(reqModel.getCustomerId())){
			throw new ParamRequiredException("客户Id不能为空");
		}
		String isCustMastId=reqModel.getIsCustMastCode();
		if (StringUtil.isNotEmpty(isCustMastId)) {
			if (!isCustMastId.equals(SQLAssembleVo.IS_CUST_MAST_CODE_FALSE)
					&& !isCustMastId.equals(SQLAssembleVo.IS_CUST_MAST_CODE_TRUE)) {
				throw new ParamRequiredException("是否为客户主码参数值错误，当前值："+isCustMastId);
			}
		}
		if(StringUtil.isEmpty(reqModel.getLabelId())){
			throw new ParamRequiredException("标签Id不能为空");
		}
		PageRequestModel pageModel=reqModel.getPageInfo();
		if (pageModel!=null){
			if(!StringUtil.isNumeric(pageModel.getCurrentPage())){
				throw new ParamRequiredException("pageInfo的currentPage必须为数字");
			}
			if(!StringUtil.isNumeric(pageModel.getPageSize())){
				throw new ParamRequiredException("pageInfo的pageSize必须为数字");
			}
		}
		String labelId=reqModel.getLabelId();
	    	List<LabelMetaDataInfo> result=labelMetaInfoSvc.getVerticalLabelMetaInfo(labelId);
	    	Page page=new Page();
	    	if (result==null||result.size()==0) {
	    		return page;
	    	}
	    	String sql=labelMetaInfoSvc.getTableSQL(reqModel,result);
	    	try {
		    	if (reqModel.getPageInfo()==null){
		    		List list=labelTableDataSvc.findVerticalDataList(sql,result);
		    		page.setData(list);
		    	}else {
		    		page=labelTableDataSvc.findVerticalDataList(reqModel.getPageInfo(), sql,result);
		    	}
	    	}catch(Exception e) {
	    		String errorMsg="获取纵表数据错误"+e.getMessage()+",sql:"+sql;
			LogUtil.error(errorMsg,e);
			throw new RuntimeException(errorMsg);
	    	}
	    	return page;
	}
	
}
