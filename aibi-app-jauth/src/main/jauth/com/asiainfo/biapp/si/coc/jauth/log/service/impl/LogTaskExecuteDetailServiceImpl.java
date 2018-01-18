
package com.asiainfo.biapp.si.coc.jauth.log.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.biapp.si.coc.jauth.frame.dao.BaseDao;
import com.asiainfo.biapp.si.coc.jauth.frame.page.JQGridPage;
import com.asiainfo.biapp.si.coc.jauth.frame.service.impl.BaseServiceImpl;
import com.asiainfo.biapp.si.coc.jauth.log.dao.ILogTaskExecuteDetailDao;
import com.asiainfo.biapp.si.coc.jauth.log.entity.LogTaskExecuteDetail;
import com.asiainfo.biapp.si.coc.jauth.log.service.ILogTaskExecuteDetailService;
import com.asiainfo.biapp.si.coc.jauth.log.vo.LogTaskExecuteDetailVo;

@Service
@Transactional
public class LogTaskExecuteDetailServiceImpl extends BaseServiceImpl<LogTaskExecuteDetail,String> implements ILogTaskExecuteDetailService {

    /** 缓存要入库的实体的池子 */
    private List<LogTaskExecuteDetail> savePool = new ArrayList<>();
    
	@Autowired
	private ILogTaskExecuteDetailDao iLogTaskExecuteDetailDao;

	@Override
	protected BaseDao<LogTaskExecuteDetail, String> getBaseDao() {
		return iLogTaskExecuteDetailDao;
	}

    @Override
    public void save(LogTaskExecuteDetail model) {
        System.out.println(this.getClass().getSimpleName()+".save()");
        savePool.add(model);    //加入缓存，等待入库
    }
    
    @Override
    public void taskSave() {
        System.out.println(this.getClass().getSimpleName()+".taskSave()入库"+savePool.size());
        if (null != savePool && !savePool.isEmpty()) {  //入库
            for (LogTaskExecuteDetail model : savePool) {
                super.save(model);
            }
            savePool.clear();
        }
    }
	
    @Override
    public JQGridPage<LogTaskExecuteDetail> findTaskExeList(JQGridPage<LogTaskExecuteDetail> page,
            LogTaskExecuteDetailVo logTaskExecuteDetailVo) {
        return iLogTaskExecuteDetailDao.findTaskExeList(page, logTaskExecuteDetailVo);
    }
    

}
