/*
 * @(#)LabelInfoServiceImpl.java
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
import com.asiainfo.cp.acrm.label.dao.ILabelInfoDao;
import com.asiainfo.cp.acrm.label.entity.LabelInfo;
import com.asiainfo.cp.acrm.label.service.ILabelInfoService;
import com.asiainfo.cp.acrm.label.vo.LabelInfoVo;

/**
 * Title : LabelInfoServiceImpl
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
@Service
@Transactional
public class LabelInfoServiceImpl extends BaseServiceImpl<LabelInfo, String> implements ILabelInfoService {

    @Autowired
    private ILabelInfoDao iLabelInfoDao;

    @Override
    protected BaseDao<LabelInfo, String> getBaseDao() {
        return iLabelInfoDao;
    }

    public Page<LabelInfo> findLabelInfoPageList(Page<LabelInfo> page, LabelInfoVo labelInfoVo) {
        return iLabelInfoDao.findLabelInfoPageList(page, labelInfoVo);
    }

    public List<LabelInfo> findLabelInfoList(LabelInfoVo labelInfoVo) {
        return iLabelInfoDao.findLabelInfoList(labelInfoVo);
    }

    public LabelInfo getById(String labelId) throws BaseException {
        if (StringUtils.isBlank(labelId)) {
            throw new ParamRequiredException("ID不能为空");
        }
        return super.get(labelId);
    }

    public void saveT(LabelInfo labelInfo) throws BaseException {
        super.saveOrUpdate(labelInfo);
    }

    public void updateT(LabelInfo labelInfo) throws BaseException {
        super.saveOrUpdate(labelInfo);
    }

    public void deleteById(String labelId) throws BaseException {
        if (StringUtils.isBlank(labelId)) {
            throw new ParamRequiredException("ID不能为空");
        }
        super.delete(labelId);
    }

}
