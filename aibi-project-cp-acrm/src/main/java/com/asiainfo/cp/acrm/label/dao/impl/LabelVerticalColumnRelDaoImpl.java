/*
 * @(#)LabelVerticalColumnRelDaoImpl.java
 *
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.asiainfo.cp.acrm.base.dao.BaseDaoImpl;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.base.utils.StringUtil;
import com.asiainfo.cp.acrm.label.dao.ILabelVerticalColumnRelDao;
import com.asiainfo.cp.acrm.label.entity.LabelVerticalColumnRel;
import com.asiainfo.cp.acrm.label.vo.LabelVerticalColumnRelVo;

/**
 * Title : LabelVerticalColumnRelDaoImpl
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
 * <pre>1    2017年11月21日     wangrd        Created</pre>
 * <p/>
 *
 * @author  wangrd
 * @version 1.0.0.2017年11月21日
 */
@Repository
public class LabelVerticalColumnRelDaoImpl extends BaseDaoImpl<LabelVerticalColumnRel, String> implements ILabelVerticalColumnRelDao{

    public Page<LabelVerticalColumnRel> findLabelVerticalColumnRelPageList(Page<LabelVerticalColumnRel> page,
            LabelVerticalColumnRelVo labelVerticalColumnRelVo) {
        Map<String, Object> params = new HashMap<>();
        StringBuffer hql = new StringBuffer("from LabelVerticalColumnRel l where 1=1 ");
        if(StringUtil.isNoneBlank(labelVerticalColumnRelVo.getLabelId()) && (null != labelVerticalColumnRelVo.getColumnId())){
            hql.append("and l.labelId = :labelId and l.columnId = :columnId ");
            params.put("labelId", labelVerticalColumnRelVo.getLabelId());
            params.put("columnId", labelVerticalColumnRelVo.getColumnId());
        }
        if(null != labelVerticalColumnRelVo.getLabelTypeId()){
            hql.append("and l.labelTypeId = :labelTypeId ");
            params.put("labelTypeId", labelVerticalColumnRelVo.getLabelTypeId());
        }
        if(null != labelVerticalColumnRelVo.getIsMustColumn()){
            hql.append("and l.isMustColumn = :isMustColumn ");
            params.put("isMustColumn", labelVerticalColumnRelVo.getIsMustColumn());
        }
        if(null != labelVerticalColumnRelVo.getSortNum()){
            hql.append("and l.sortNum = :sortNum ");
            params.put("sortNum", labelVerticalColumnRelVo.getSortNum());
        }
        return super.findPageByHql(page, hql.toString(), params);
    }

    public List<LabelVerticalColumnRel> findLabelVerticalColumnRelList(
            LabelVerticalColumnRelVo labelVerticalColumnRelVo) {
        Map<String, Object> params = new HashMap<>();
        StringBuffer hql = new StringBuffer("from LabelVerticalColumnRel l where 1=1 ");
        if(StringUtil.isNoneBlank(labelVerticalColumnRelVo.getLabelId()) && (null != labelVerticalColumnRelVo.getColumnId())){
            hql.append("and l.labelId = :labelId and l.columnId = :columnId ");
            params.put("labelId", labelVerticalColumnRelVo.getLabelId());
            params.put("columnId", labelVerticalColumnRelVo.getColumnId());
        }
        if(null != labelVerticalColumnRelVo.getLabelTypeId()){
            hql.append("and l.labelTypeId = :labelTypeId ");
            params.put("labelTypeId", labelVerticalColumnRelVo.getLabelTypeId());
        }
        if(null != labelVerticalColumnRelVo.getIsMustColumn()){
            hql.append("and l.isMustColumn = :isMustColumn ");
            params.put("isMustColumn", labelVerticalColumnRelVo.getIsMustColumn());
        }
        if(null != labelVerticalColumnRelVo.getSortNum()){
            hql.append("and l.sortNum = :sortNum ");
            params.put("sortNum", labelVerticalColumnRelVo.getSortNum());
        }
        return super.findListByHql(hql.toString(), params);
    }

}
