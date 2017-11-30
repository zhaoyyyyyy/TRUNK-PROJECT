package com.asiainfo.cp.acrm.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.auth.model.LabelModel;
import com.asiainfo.cp.acrm.auth.model.PageRequestModel;
import com.asiainfo.cp.acrm.auth.model.PortrayalRequestModel;
import com.asiainfo.cp.acrm.auth.model.ViewRequestModel;
import com.asiainfo.cp.acrm.auth.service.ICustomerService;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.exception.ParamRequiredException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.base.utils.StringUtil;
import com.asiainfo.cp.acrm.label.service.ILabelInfoService;
import com.asiainfo.cp.acrm.label.service.ILabelMetaInfoService;
import com.asiainfo.cp.acrm.label.service.ILabelTableDataService;
import com.asiainfo.cp.acrm.label.vo.LabelMetaDataInfo;

@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService{
	
	@Autowired
	private ILabelInfoService labelInfoSvc;
	
    @Autowired
    private ILabelMetaInfoService labelMetaInfoSvc;
    
    @Autowired
	private ILabelTableDataService labelTableDataSvc;

	@Override
	public List<LabelModel> getLabelModels(PortrayalRequestModel reqModel) throws BaseException {
		if(StringUtil.isEmpty(reqModel.getCustomerId())){
			throw new ParamRequiredException("客户Id不能为空");
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
		for (String labelId:reqModel.getLabelId()){
			LabelMetaDataInfo result=labelMetaInfoSvc.getHorizentalLabelMetaInfo(labelId);
			System.out.println(labelMetaInfoSvc.getTableSQL(reqModel,result));
			LabelModel model=labelTableDataSvc.findHorizentalLabelInfoModel(labelMetaInfoSvc.getTableSQL(reqModel,result), result);
			if (model!=null){
				resultData.add(model);
			}
		}
		return resultData;
	}
	
	@Override
	public Page<Object> view(ViewRequestModel reqModel) throws BaseException {
		if(StringUtil.isEmpty(reqModel.getCustomerId())){
			throw new ParamRequiredException("客户Id不能为空");
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
    	String sql=labelMetaInfoSvc.getTableSQL(reqModel,result);
    	Page page=new Page();
    	if (reqModel.getPageInfo()==null){
    		List list=labelTableDataSvc.findVerticalDataList(sql,result);
    		page.setData(list);
    		return page;
    	}
    	page=labelTableDataSvc.findVerticalDataList(reqModel.getPageInfo(), sql,result);
		return page;
	}
	
}
