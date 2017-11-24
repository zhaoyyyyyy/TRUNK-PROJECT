/*
 * @(#)ILabelInfoService.java
 * 
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.service;

import java.util.List;

import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.base.service.BaseService;
import com.asiainfo.cp.acrm.label.entity.LabelInfo;
import com.asiainfo.cp.acrm.label.vo.LabelInfoVo;

/**
 * Title : ILabelInfoService
 * <p/>
 * Description :
 * <p/>
 * CopyRight : CopyRight (c) 2017
 * <p/>
 * Company : 北京亚信智慧数据科技有限公司
 * <p/>
 * JDK Version Used : JDK 1.8
 * <p/>
 * Modification History :
 * <p/>
 * 
 * <pre>
 * NO.    Date    Modified By    Why & What is modified
 * </pre>
 * 
 * <pre>
 * 1    2017年11月16日    zhangnan7        Created
 * </pre>
 * <p/>
 *
 * @author zhangnan7
 * @version 1.0.0.2017年11月16日
 */
public interface ILabelInfoService extends BaseService<LabelInfo, String> {

    /**
     * Description:分页查询标签信息
     *
     * @param page
     * @param labelInfoVo
     * @return
     * @throws BaseException
     */
    public Page<LabelInfo> findLabelInfoPageList(Page<LabelInfo> page, LabelInfoVo labelInfoVo) throws BaseException;

    /**
     * Description:查询标签信息列表
     *
     * @param labelInfoVo
     * @return
     * @throws BaseException
     */
    public List<LabelInfo> findLabelInfoList(LabelInfoVo labelInfoVo) throws BaseException;

    /**
     * Description:通过ID拿到标签信息
     *
     * @param labelId
     * @return
     * @throws BaseException
     */
    public LabelInfo getById(String labelId) throws BaseException;

    /**
     * Description:新增一个标签信息
     *
     * @param labelInfo
     * @throws BaseException
     */
    public void saveT(LabelInfo labelInfo) throws BaseException;

    /**
     * Description:修改标签信息
     *
     * @param labelInfo
     * @throws BaseException
     */
    public void updateT(LabelInfo labelInfo) throws BaseException;

    /**
     * Description:删除标签信息
     *
     * @param labelId
     * @throws BaseException
     */
    public void deleteById(String labelId) throws BaseException;

}
