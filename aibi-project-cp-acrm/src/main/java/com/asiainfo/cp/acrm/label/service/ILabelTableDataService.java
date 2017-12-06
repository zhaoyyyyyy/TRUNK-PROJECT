package com.asiainfo.cp.acrm.label.service;

import java.util.List;

import com.asiainfo.cp.acrm.auth.model.LabelModel;
import com.asiainfo.cp.acrm.auth.model.PageRequestModel;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.label.vo.LabelMetaDataInfo;

/**
 * 获取标签对应数据服务
 * @author jinbh
 *
 */
public interface ILabelTableDataService {

    /**
     * Description:获取宽表数据
     *
     * @param sql	获取宽表数据sql
     * @param lableMetaDataInfo		标签元数据信息
     * @return
     * @throws BaseException
     */	
	public LabelModel findHorizentalLabelInfoModel(String sql, LabelMetaDataInfo lableMetaDataInfo)
			throws BaseException ;
	
    /**
     * Description:获取纵表数据
     *
     * @param sql	获取纵表数据sql
     * @param lableMetaDataInfo		标签元数据信息
     * @return
     * @throws BaseException
     */	
	public List findVerticalDataList(String sql,List<LabelMetaDataInfo> lableMetaDataInfos) ;

	public Page<Object> findVerticalDataList(PageRequestModel page,String sql,List<LabelMetaDataInfo> lableMetaDataInfos);

	public List<LabelModel> findHorizentalLabelInfoModels(String sql, List<LabelMetaDataInfo> lableMetaDataInfos) throws BaseException;
}
