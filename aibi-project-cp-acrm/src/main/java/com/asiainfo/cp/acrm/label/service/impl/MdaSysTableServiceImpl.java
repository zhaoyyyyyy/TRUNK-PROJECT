/*
 * @(#)MdaSysTableServiceImpl.java
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
import com.asiainfo.cp.acrm.label.dao.IMdaSysTableDao;
import com.asiainfo.cp.acrm.label.entity.MdaSysTable;
import com.asiainfo.cp.acrm.label.service.IMdaSysTableService;
import com.asiainfo.cp.acrm.label.vo.MdaSysTableVo;

/**
 * Title : MdaSysTableServiceImpl
 * <p/>
 * Description :
 * <p/>
 * CopyRight : CopyRight (c) 2017
 * <p/>
 * Company : 北京亚信智慧数据科技有限公司
 * <p/>
 * JDK Version Used : JDK 5.0 +
 * <p/>
 * Modification History :
 * <p/>
 * 
 * <pre>
 * NO.    Date    Modified By    Why & What is modified
 * </pre>
 * 
 * <pre>
 * 1    2017年11月20日    lilin7        Created
 * </pre>
 * <p/>
 *
 * @author lilin7
 * @version 1.0.0.2017年11月20日
 */
@Service
@Transactional
public class MdaSysTableServiceImpl extends BaseServiceImpl<MdaSysTable, String> implements IMdaSysTableService {

    @Autowired
    private IMdaSysTableDao iMdaSysTableDao;

    @Override
    protected BaseDao<MdaSysTable, String> getBaseDao() {
        return iMdaSysTableDao;
    }

    @Override
    public Page<MdaSysTable> findMdaSysTablePageList(Page<MdaSysTable> page, MdaSysTableVo mdaSysTableVo)
            throws BaseException {
        return iMdaSysTableDao.findMdaSysTablePageList(page, mdaSysTableVo);
    }

    @Override
    public List<MdaSysTable> findMdaSysTableList(MdaSysTableVo mdaSysTableVo) throws BaseException {
        return iMdaSysTableDao.findMdaSysTableList(mdaSysTableVo);
    }

    public MdaSysTable getById(String tableId) throws BaseException {
        if (StringUtils.isBlank(tableId)) {
            throw new ParamRequiredException("Id不能为空");
        }
        return super.get(tableId);
    }

    public void saveT(MdaSysTable mdaSysTable) throws BaseException {
        super.saveOrUpdate(mdaSysTable);
    }

    public void updateT(MdaSysTable mdaSysTable) throws BaseException {
        super.saveOrUpdate(mdaSysTable);
    }

    public void deleteById(String tableId) throws BaseException {
        super.delete(tableId);
    }
}
