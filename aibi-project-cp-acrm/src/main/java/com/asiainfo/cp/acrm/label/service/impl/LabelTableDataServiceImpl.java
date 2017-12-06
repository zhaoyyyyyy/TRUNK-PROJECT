package com.asiainfo.cp.acrm.label.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.auth.model.LabelModel;
import com.asiainfo.cp.acrm.auth.model.PageRequestModel;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.label.dao.ILabelTableDataDao;
import com.asiainfo.cp.acrm.label.service.ILabelTableDataService;
import com.asiainfo.cp.acrm.label.vo.LabelMetaDataInfo;

@Service
@Transactional
public class LabelTableDataServiceImpl implements ILabelTableDataService {
	
	@Autowired
	private ILabelTableDataDao dao;

	@Override
	public LabelModel findHorizentalLabelInfoModel(String sql, LabelMetaDataInfo lableMetaDataInfo)
			throws BaseException {
		return dao.getHorizentalLabelInfoModel(sql, lableMetaDataInfo);
	}
	


	@Override
	public Page<Object> findVerticalDataList(PageRequestModel pageModel, String sql,List<LabelMetaDataInfo> lableMetaDataInfos) {
		Page    page=dao.findVerticalDataList(pageModel, sql);
		if (page==null||page.getData()==null ||page.getData().size()==0) {
			return new Page();
		}
    		page.setData(dao.getVerticalLabelInfoModel(page.getData(), lableMetaDataInfos));
		return page;
	}

	@Override
	public List findVerticalDataList(String sql,List<LabelMetaDataInfo> lableMetaDataInfos) {
		List list=dao.findVerticalDataList(sql);
		if (list==null||list.size()==0) {
			return new ArrayList();
		}
		return dao.getVerticalLabelInfoModel(list, lableMetaDataInfos);
	}



	@Override
	public List<LabelModel> findHorizentalLabelInfoModels(String sql, List<LabelMetaDataInfo> lableMetaDataInfos) throws BaseException {
		return dao.getHorizentalLabelInfoModels(sql, lableMetaDataInfos);
	}
	
}
