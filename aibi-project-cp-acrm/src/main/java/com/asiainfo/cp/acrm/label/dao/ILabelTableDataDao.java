package com.asiainfo.cp.acrm.label.dao;

import java.util.List;
import java.util.Map;

import com.asiainfo.cp.acrm.auth.model.LabelModel;
import com.asiainfo.cp.acrm.auth.model.PageRequestModel;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.label.vo.LabelMetaDataInfo;

/**
 * 标签对应数据访问服务
 * @author jinbh
 *
 */
public interface ILabelTableDataDao {


    /**
     * Description: 获取宽表标签数据
     *
     * @param sql	sql语句
     * @param lableMetaDataInfos	标签元数据
     * @return
     */
	public List<LabelModel> getHorizentalLabelInfoModels(String sql, List<LabelMetaDataInfo> lableMetaDataInfos)
			throws BaseException;
	
    /**
     * Description: 装配纵表标签数据
     *
     * @param result		查询结果集
     * @param lableMetaDataInfos	标签元数据
     * @return
     */
	public List<Map<String,String>> getVerticalLabelInfoModel(List result, List<LabelMetaDataInfo> lableMetaDataInfos);
	
    /**
     * Description: 获取纵表标签数据
     *
     * @param page	请求分页信息
     * @param sql	sql语句
     * @return
     */	
	public Page<Object> findVerticalDataList(PageRequestModel page,String sql);
	
	public List findVerticalDataList(String sql) ;
}
