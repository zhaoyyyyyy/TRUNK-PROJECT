package com.asiainfo.cp.acrm.auth.service;

import java.util.List;

import com.asiainfo.cp.acrm.auth.model.LabelModel;
import com.asiainfo.cp.acrm.auth.model.PortrayalRequestModel;
import com.asiainfo.cp.acrm.auth.model.ViewRequestModel;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;

public interface ICustomerService {
	public List<LabelModel> getLabelModels(PortrayalRequestModel reqModel) throws BaseException ;
	
	public Page<Object> view(ViewRequestModel reqModel) throws BaseException;
}
