package com.asiainfo.cp.acrm.auth.service;

import java.util.List;

import com.asiainfo.cp.acrm.auth.model.DicData;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;


public interface IDicDataService {

	
	/**
	 * 通过字典编码拿到字典数据集合
	 * @return
	 */
	public List<DicData> queryDataListByCode(String code) throws BaseException;
	
	
	/**
	 * 通过字典编码拿到字典数据集合(分页)
	 * @return
	 */
	
	public Page<DicData> findDicDataList(Page<DicData> page, String dicCode) throws BaseException;
}
