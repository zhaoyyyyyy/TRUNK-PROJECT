package com.asiainfo.cp.acrm.label.service;

import java.util.List;

import com.asiainfo.cp.acrm.auth.model.PortrayalRequestModel;
import com.asiainfo.cp.acrm.auth.model.ViewRequestModel;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.label.vo.LabelMetaDataInfo;

/**
 * 标签对应的表列元数据服务
 * @author jinbh
 *
 */
public interface ILabelMetaInfoService {
	
	public final String DIM_COLUMNVALUE_ALIAS_NAME_DEFAULT = "cqwerty_dim_asdfghc";
	
    /**
     * Description:获取宽表元数据
     * 
     * @param labelId  标签Id
     * @return
     * @throws BaseException
     */	
	public LabelMetaDataInfo getHorizentalLabelMetaInfo(String labelId) throws BaseException;
	
    /**
     * Description:获取宽表元数据
     * 
     * @param labelIds  标签Ids
     * @return
     * @throws BaseException
     */	
	public List<LabelMetaDataInfo> getHorizentalLabelMetaInfos(List<String> labelIds) throws BaseException;
	
    /**
     * Description:获取纵表元数据
     *
     * @param labelId  标签Id
     * @return
     * @throws BaseException
     */	
	public List<LabelMetaDataInfo> getVerticalLabelMetaInfo(String labelId) throws BaseException;
	
	
    /**
     * Description:获取宽表数据SQL
     *
     * @param reqModel	360视图1:1服务请求参数模型
     * @param lableMetaDataInfo		标签元数据信息
     * @return
     * @throws BaseException
     */	
	public String getTableSQL(PortrayalRequestModel reqModel,LabelMetaDataInfo lableMetaDataInfo);
	
	public String getTableSQL(PortrayalRequestModel reqModel,List<LabelMetaDataInfo> lableMetaDataInfos);
	
    /**
     * Description:获取纵表数据SQL
     *
     * @param reqModel	360视图1:N服务请求参数模型
     * @param lableMetaDataInfo		标签元数据信息
     * @return
     * @throws BaseException
     */	
	public String getTableSQL(ViewRequestModel reqModel,List<LabelMetaDataInfo> lableMetaDataInfos);
	
}
