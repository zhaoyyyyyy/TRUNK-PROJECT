/*
 * @(#)IMdaSysTableService.java
 *
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.service;

import java.util.List;

import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.base.service.BaseService;
import com.asiainfo.cp.acrm.label.entity.MdaSysTable;
import com.asiainfo.cp.acrm.label.vo.MdaSysTableVo;

/**
 * Title : IMdaSysTableService
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
public interface IMdaSysTableService extends BaseService<MdaSysTable, String> {

    /**
     * @Describe 根据分页条件查询
     * @author lilin7
     * @param page
     * @param mdaSysTableVo
     * @return
     */
    public Page<MdaSysTable> findMdaSysTablePageList(Page<MdaSysTable> page, MdaSysTableVo mdaSysTableVo)
            throws BaseException;

    /**
     * @Describe 根据条件查询列表
     * @author lilin7
     * @param page
     * @param mdaSysTableVo
     * @return
     */
    public List<MdaSysTable> findMdaSysTableList(MdaSysTableVo mdaSysTableVo) throws BaseException;

    /**
     * @Describe 通过ID得到一个实体
     * @author lilin7
     * @param tableId
     * @return
     */
    public MdaSysTable getById(String tableId) throws BaseException;

    /**
     * @Describe 新增一个实体
     * @author lilin7
     * @param mdaSysTableVo
     * @return
     */
    public void saveT(MdaSysTable mdaSysTable) throws BaseException;

    /**
     * @Describe 修改一个实体
     * @author lilin7
     * @param mdaSysTableVo
     * @return
     */
    public void updateT(MdaSysTable mdaSysTable) throws BaseException;

    /**
     * @Describe 通过Id删除一个实体
     * @author lilin7
     * @param tableId
     * @return
     */
    public void deleteById(String tableId) throws BaseException;
}
