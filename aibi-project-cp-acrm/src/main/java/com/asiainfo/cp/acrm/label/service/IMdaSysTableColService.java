/*
 * @(#)IMdaSysTableColService.java
 *
 * CopyRight (c) 2017 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.cp.acrm.label.service;

import java.util.List;

import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.page.Page;
import com.asiainfo.cp.acrm.base.service.BaseService;
import com.asiainfo.cp.acrm.label.entity.MdaSysTableColumn;
import com.asiainfo.cp.acrm.label.vo.MdaSysTableColumnVo;

/**
 * Title : IMdaSysTableColService
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
 * 1    2017年11月21日    lilin7        Created
 * </pre>
 * <p/>
 *
 * @author lilin7
 * @version 1.0.0.2017年11月21日
 */
public interface IMdaSysTableColService extends BaseService<MdaSysTableColumn, String> {

    /**
     * Description: 根据分页条件查询
     *
     * @param page
     * @param mdaSysTableColumnVo
     * @return
     * @throws BaseException
     */
    public Page<MdaSysTableColumn> findMdaSysTableColPageList(Page<MdaSysTableColumn> page,
            MdaSysTableColumnVo mdaSysTableColumnVo) throws BaseException;

    /**
     * Description:根据条件查询列表
     *
     * @param mdaSysTableColumnVo
     * @return
     * @throws BaseException
     */
    public List<MdaSysTableColumn> findMdaSysTableColList(MdaSysTableColumnVo mdaSysTableColumnVo) throws BaseException;

    /**
     * Description:根据列id得到一个实体
     *
     * @param columnId
     * @return
     */
    public MdaSysTableColumn getById(String columnId) throws BaseException;

    /**
     * Description:新增一个实体
     *
     * @param mdaSysTableColumn
     * @throws BaseException
     */
    public void saveT(MdaSysTableColumn mdaSysTableColumn) throws BaseException;

    /**
     * Description:修改
     *
     * @param mdaSysTableColumn
     */
    public void updateT(MdaSysTableColumn mdaSysTableColumn) throws BaseException;

    /**
     * Description: 通过Id删除一个实体
     *
     * @param columnId
     * @throws BaseException
     */
    public void deleteById(String columnId) throws BaseException;
}
