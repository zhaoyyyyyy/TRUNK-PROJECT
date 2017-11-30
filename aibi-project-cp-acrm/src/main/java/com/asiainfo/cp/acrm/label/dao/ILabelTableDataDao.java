package com.asiainfo.cp.acrm.label.dao;

import java.util.List;
import java.util.Map;

import com.asiainfo.cp.acrm.auth.model.LabelModel;
import com.asiainfo.cp.acrm.auth.model.PageRequestModel;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.label.vo.LabelMetaDataInfo;

public interface ILabelTableDataDao {

	public LabelModel getHorizentalLabelInfoModel(String sql,LabelMetaDataInfo lableMetaDataInfo)  throws BaseException;

	public List<Map<String,Object>> getVerticalLabelInfoModel(List result, List<LabelMetaDataInfo> lableMetaDataInfos);
	
	public Page<Object> findVerticalDataList(PageRequestModel page,String sql);
	
	public List findVerticalDataList(String sql) ;
}
