/*
 * @(#)ILabelInfoDao.java
 * 
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.dao;

import java.util.List;

import com.asiainfo.cp.acrm.base.dao.BaseDao;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.label.entity.DimtableInfo;
import com.asiainfo.cp.acrm.label.entity.LabelInfo;
import com.asiainfo.cp.acrm.label.vo.LabelInfoVo;

/**
 * 维表注册表读取服务
 * @author jinbh
 *
 */
public interface IDimtableInfoDao extends BaseDao<DimtableInfo, String> {

    /**
     * Description: 按条件查询列表
     *
     * @param labelInfo
     * @return
     */
	public DimtableInfo getDimtableInfo(String dimId);
	
	public DimtableInfo getDimtableInfoReload(String dimId);
	
	public List<DimtableInfo> findDimtableInfoList();
	
}
