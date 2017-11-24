/*
 * @(#)LabelVerticalColumnRelServiceImpl.java
 *
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.base.dao.BaseDao;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.exception.ParamRequiredException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.base.service.impl.BaseServiceImpl;
import com.asiainfo.cp.acrm.base.utils.StringUtil;
import com.asiainfo.cp.acrm.label.dao.ILabelVerticalColumnRelDao;
import com.asiainfo.cp.acrm.label.entity.LabelVerticalColumnRel;
import com.asiainfo.cp.acrm.label.service.ILabelVerticalColumnRelService;
import com.asiainfo.cp.acrm.label.vo.LabelVerticalColumnRelVo;

/**
 * Title : LabelVerticalColumnRelServiceImpl
 * <p/>
 * Description : 
 * <p/>
 * CopyRight : CopyRight (c) 2017
 * <p/>
 * Company : 北京亚信智慧数据科技有限公司
 * <p/>
 * JDK Version Used : JDK 5.0 +
 * <p/>
 * Modification History	:
 * <p/>
 * <pre>NO.    Date    Modified By    Why & What is modified</pre>
 * <pre>1    2017年11月21日    wangrd        Created</pre>
 * <p/>
 *
 * @author  wangrd
 * @version 1.0.0.2017年11月21日
 */
@Service
@Transactional
public class LabelVerticalColumnRelServiceImpl extends BaseServiceImpl<LabelVerticalColumnRel, String> implements ILabelVerticalColumnRelService{

    @Autowired
    private ILabelVerticalColumnRelDao iLabelVerticalColumnRelDao;
    
    @Override
    protected BaseDao<LabelVerticalColumnRel, String> getBaseDao() {
        return iLabelVerticalColumnRelDao;
    }

    public Page<LabelVerticalColumnRel> findLabelVerticalColumnRelPageList(Page<LabelVerticalColumnRel> page,
            LabelVerticalColumnRelVo labelVerticalColumnRelVo) throws BaseException {
        return iLabelVerticalColumnRelDao.findLabelVerticalColumnRelPageList(page, labelVerticalColumnRelVo);
    }

    public List<LabelVerticalColumnRel> findLabelVerticalColumnRelList(
            LabelVerticalColumnRelVo labelVerticalColumnRelVo) throws BaseException {
        return iLabelVerticalColumnRelDao.findLabelVerticalColumnRelList(labelVerticalColumnRelVo);
    }

    public LabelVerticalColumnRel getById(String labelId) throws BaseException {
        if(StringUtil.isBlank(labelId)){
            throw new ParamRequiredException("Id不能为空");
        }
        return super.get(labelId);
    }

    public void saveT(LabelVerticalColumnRel labelVerticalColumnRel) throws BaseException {
        super.saveOrUpdate(labelVerticalColumnRel);
    }

    public void updateT(LabelVerticalColumnRel labelVerticalColumnRel) throws BaseException {
        super.saveOrUpdate(labelVerticalColumnRel);
        
    }

    public void deleteById(String labelId) throws BaseException {
        super.delete(labelId);
        
    }

    

}
