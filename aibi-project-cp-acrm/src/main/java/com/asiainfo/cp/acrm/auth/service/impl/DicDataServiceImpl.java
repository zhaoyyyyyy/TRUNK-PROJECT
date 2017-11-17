package com.asiainfo.cp.acrm.auth.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.auth.dao.IDicDataDao;
import com.asiainfo.cp.acrm.auth.model.DicData;
import com.asiainfo.cp.acrm.auth.service.IDicDataService;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.exception.ParamRequiredException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.base.utils.StringUtil;


@Service
@Transactional
public class DicDataServiceImpl implements IDicDataService{

	@Autowired
	private IDicDataDao dicDataDao;
	
	@Override
	public List<DicData> queryDataListByCode(String code) throws BaseException {
		if(StringUtil.isEmpty(code)){
			throw new ParamRequiredException("数据字典编码必须填写");
		}
		return dicDataDao.selectDataBycode(code);
	}


	public Page<DicData> findDicDataList(Page<DicData> page,String dicCode) {
		return dicDataDao.findDicDataList(page, dicCode);
	}
}
