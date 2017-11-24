/*
 * @(#)LabelExtInfoServiceImpl.java
 * 
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.base.dao.BaseDao;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.exception.ParamRequiredException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.base.service.impl.BaseServiceImpl;
import com.asiainfo.cp.acrm.label.dao.ILabelExtInfoDao;
import com.asiainfo.cp.acrm.label.entity.LabelExtInfo;
import com.asiainfo.cp.acrm.label.service.ILabelExtInfoService;
import com.asiainfo.cp.acrm.label.vo.LabelExtInfoVo;

/**
 * Title : LabelExtInfoServiceImpl
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
@Service
@Transactional
public class LabelExtInfoServiceImpl extends BaseServiceImpl<LabelExtInfo, String> implements ILabelExtInfoService {

    @Autowired
    private ILabelExtInfoDao iLabelExtInfoDao;

    @Override
    protected BaseDao<LabelExtInfo, String> getBaseDao() {
        return iLabelExtInfoDao;
    }

    public Page<LabelExtInfo> findLabelExtInfoPageList(Page<LabelExtInfo> page, LabelExtInfoVo labelExtInfoVo)
            throws BaseException {
        if (labelExtInfoVo.getLabelPrecision() != null) {
            if (labelExtInfoVo.getLabelPrecision() >= 1) {
                throw new ParamRequiredException("标签准确率需要小于1");
            }
        }
        if (labelExtInfoVo.getLabelCoverate() != null) {
            if (labelExtInfoVo.getLabelCoverate() >= 1) {
                throw new ParamRequiredException("标签覆盖率需要小于1");
            }
        }
        return iLabelExtInfoDao.findLabelExtInfoPageList(page, labelExtInfoVo);
    }

    public List<LabelExtInfo> findLabelExtInfoList(LabelExtInfoVo labelExtInfoVo) throws BaseException {
        if (labelExtInfoVo.getLabelPrecision() != null) {
            if (labelExtInfoVo.getLabelPrecision() >= 1) {
                throw new ParamRequiredException("标签准确率需要小于1");
            }
        }
        if (labelExtInfoVo.getLabelCoverate() != null) {
            if (labelExtInfoVo.getLabelCoverate() >= 1) {
                throw new ParamRequiredException("标签覆盖率需要小于1");
            }
        }
        return iLabelExtInfoDao.findLabelExtInfoList(labelExtInfoVo);
    }

    public LabelExtInfo getById(String labelId) throws BaseException {
        if (StringUtils.isBlank(labelId)) {
            throw new ParamRequiredException("ID不能为空");
        }
        return super.get(labelId);
    }

    public void saveT(LabelExtInfo labelExtInfo) throws BaseException {
        if (labelExtInfo.getLabelPrecision() != null) {
            if (labelExtInfo.getLabelPrecision() >= 1) {
                throw new ParamRequiredException("标签准确率需要小于1");
            }
        }
        if (labelExtInfo.getLabelCoverate() != null) {
            if (labelExtInfo.getLabelCoverate() >= 1) {
                throw new ParamRequiredException("标签覆盖率需要小于1");
            }
        }
        super.saveOrUpdate(labelExtInfo);
    }

    public void updateT(LabelExtInfo labelExtInfo) throws BaseException {
        if (labelExtInfo.getLabelPrecision() >= 1) {
            throw new ParamRequiredException("标签准确率需要小于1");
        }
        if (labelExtInfo.getLabelCoverate() >= 1) {
            throw new ParamRequiredException("标签覆盖率需要小于1");
        }
        super.saveOrUpdate(labelExtInfo);
    }

    public void deleteById(String labelId) throws BaseException {
        if (StringUtils.isBlank(labelId)) {
            throw new ParamRequiredException("ID不能为空");
        }
        super.delete(labelId);
    }

}
