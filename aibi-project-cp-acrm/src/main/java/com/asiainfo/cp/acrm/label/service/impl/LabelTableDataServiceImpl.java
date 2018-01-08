package com.asiainfo.cp.acrm.label.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.auth.model.LabelModel;
import com.asiainfo.cp.acrm.auth.model.PageRequestModel;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.base.utils.StringUtil;
import com.asiainfo.cp.acrm.label.dao.IDimDataDao;
import com.asiainfo.cp.acrm.label.dao.ILabelTableDataDao;
import com.asiainfo.cp.acrm.label.service.ILabelTableDataService;
import com.asiainfo.cp.acrm.label.vo.LabelMetaDataInfo;

@Service
@Transactional
public class LabelTableDataServiceImpl implements ILabelTableDataService {
	
	@Autowired
	private ILabelTableDataDao dao;
	
	@Autowired
	private IDimDataDao iDimDataDao;
	
	@Value("${cache.isDimtableCached}")
	private Boolean isDimtableCached;
	

	@Override
	public Page<Object> findVerticalDataList(PageRequestModel pageModel, String sql,List<LabelMetaDataInfo> lableMetaDataInfos) {
		Page    page=dao.findVerticalDataList(pageModel, sql);
		return page;
	}

	@Override
	public List findVerticalDataList(String sql,List<LabelMetaDataInfo> lableMetaDataInfos) {
		List result=dao.findVerticalDataList(sql);
		if (result==null||result.size()==0) {
			return new ArrayList();
		}
		if (isDimtableCached) {
			List<LabelMetaDataInfo> lableMetaDataInfosWithDim=new ArrayList<>();
			for (int i=0;i<lableMetaDataInfos.size();i++){
				LabelMetaDataInfo labelMetaDataInfo=lableMetaDataInfos.get(i);
				String dimTableName=labelMetaDataInfo.getDimtableName();
				if (StringUtil.isNotEmpty(dimTableName)) {
					lableMetaDataInfosWithDim.add(labelMetaDataInfo);
				}
			}
			if (lableMetaDataInfosWithDim.size()==0) {
				return result;
			}
			for (int i=0;i<result.size();i++) {
				Map<String,String> eachResult=(Map<String, String>) result.get(i);
				for (int j=0;j<lableMetaDataInfosWithDim.size();j++){
					LabelMetaDataInfo labelMetaDataInfo=lableMetaDataInfosWithDim.get(j);
					String dimColumnName=labelMetaDataInfo.getColumnName();
					String value=(String)iDimDataDao.getDimValue(labelMetaDataInfo.getDimtableId(), eachResult.get(dimColumnName));
					if (StringUtil.isNotEmpty(value)) {
						eachResult.put(dimColumnName, value);
					}
				}
			}
		}
		return result;
	}

	@Override
	public List<LabelModel> findHorizentalLabelInfoModels(String sql, List<LabelMetaDataInfo> lableMetaDataInfos) throws BaseException {
		List<LabelModel> result=dao.getHorizentalLabelInfoModels(sql, lableMetaDataInfos);
		if (isDimtableCached) {
			for (int i=0;i<result.size();i++) {
				LabelModel labelModel=result.get(i);
				LabelMetaDataInfo labelMetaDataInfo=lableMetaDataInfos.get(i);
				String dimTableName=labelMetaDataInfo.getDimtableName();
				if (StringUtil.isNotEmpty(dimTableName)) {
					String dimValue=iDimDataDao.getDimValue(dimTableName, labelModel.getLabelValue());
					if (StringUtil.isNotEmpty(dimValue)) {
						labelModel.setLabelValue(dimValue);
					}
				}
			}
		}
		return result;
	}
}
