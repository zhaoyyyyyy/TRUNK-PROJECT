/*
 * @(#)IMdaSysTableDao.java
 *
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.dao;

import java.util.List;

import com.asiainfo.cp.acrm.base.dao.BaseDao;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.label.entity.MdaSysTable;
import com.asiainfo.cp.acrm.label.vo.MdaSysTableVo;

/**
 * Title : IMdaSysTableDao
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
public interface IMdaSysTableDao extends BaseDao<MdaSysTable, String> {

    /**
     * @Describe 根据分页条件查询
     * @author lilin7
     * @param page
     * @param mdaSysTableVo
     * @return
     */
    public Page<MdaSysTable> findMdaSysTablePageList(Page<MdaSysTable> page, MdaSysTableVo mdaSysTableVo);

    /**
     * @Describe 根据条件查询列表
     * @author lilin7
     * @param page
     * @param mdaSysTableVo
     * @return
     */
    public List<MdaSysTable> findMdaSysTableList(MdaSysTableVo mdaSysTableVo);
}
