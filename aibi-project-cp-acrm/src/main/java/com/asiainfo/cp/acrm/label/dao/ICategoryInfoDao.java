/*
 * @(#)ICategoryInfoDao.java
 *
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.dao;

import java.util.List;

import com.asiainfo.cp.acrm.base.dao.BaseDao;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.label.entity.CategoryInfo;
import com.asiainfo.cp.acrm.label.vo.CategoryInfoVo;

/**
 * Title : ICategoryInfoDao
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
 * <pre>1    2017年11月20日     wangrd        Created</pre>
 * <p/>
 *
 * @author   wangrd
 * @version 1.0.0.2017年11月20日
 */
public interface ICategoryInfoDao extends BaseDao<CategoryInfo, String>{
    
    /**
     * 根据条件分页查询
     *
     * @param page
     * @param CategoryInfo
     * @return
     */
    public Page<CategoryInfo> findCategoryInfoPageList(Page<CategoryInfo> page, CategoryInfoVo categoryInfoVo);

    /**
     * 根据条件查询列表
     *
     * @param CategoryInfo
     * @return
     */
    public List<CategoryInfo> findCategoryInfoList(CategoryInfoVo categoryInfoVo);
}
