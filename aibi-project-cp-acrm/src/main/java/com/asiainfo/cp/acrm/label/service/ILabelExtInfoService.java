/*
 * @(#)ILabelExtInfoService.java
 * 
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.service;

import java.util.List;

import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.base.service.BaseService;
import com.asiainfo.cp.acrm.label.entity.LabelExtInfo;
import com.asiainfo.cp.acrm.label.vo.LabelExtInfoVo;

/**
 * Title : ILabelExtInfoService
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
 * 1    2017年11月21日    zhangnan7        Created
 * </pre>
 * <p/>
 *
 * @author zhangnan7
 * @version 1.0.0.2017年11月21日
 */
public interface ILabelExtInfoService extends BaseService<LabelExtInfo, String> {

    /**
     * Description: 分页查询标签扩展信息
     *
     * @param page
     * @param labelExtInfoVo
     * @return
     * @throws BaseException
     */
    public Page<LabelExtInfo> findLabelExtInfoPageList(Page<LabelExtInfo> page, LabelExtInfoVo labelExtInfoVo)
            throws BaseException;

    /**
     * Description: 查询标签扩展信息列表
     *
     * @param labelExtInfoVo
     * @return
     * @throws BaseException
     */
    public List<LabelExtInfo> findLabelExtInfoList(LabelExtInfoVo labelExtInfoVo) throws BaseException;

    /**
     * Description: 通过ID拿到标签扩展信息
     *
     * @param labelId
     * @return
     * @throws BaseException
     */
    public LabelExtInfo getById(String labelId) throws BaseException;

    /**
     * Description: 新增一个标签扩展信息
     *
     * @param labelExtInfo
     * @throws BaseException
     */
    public void saveT(LabelExtInfo labelExtInfo) throws BaseException;

    /**
     * Description: 修改标签扩展信息
     *
     * @param labelExtInfo
     * @throws BaseException
     */
    public void updateT(LabelExtInfo labelExtInfo) throws BaseException;

    /**
     * Description: 删除标签扩展信息
     *
     * @param labelId
     * @throws BaseException
     */
    public void deleteById(String labelId) throws BaseException;

}
