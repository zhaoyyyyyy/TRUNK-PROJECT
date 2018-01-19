
package com.asiainfo.biapp.si.coc.jauth.log.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.biapp.si.coc.jauth.frame.dao.BaseDao;
import com.asiainfo.biapp.si.coc.jauth.frame.page.JQGridPage;
import com.asiainfo.biapp.si.coc.jauth.frame.service.impl.BaseServiceImpl;
import com.asiainfo.biapp.si.coc.jauth.log.dao.ILogInterfaceDetailDao;
import com.asiainfo.biapp.si.coc.jauth.log.entity.LogInterfaceDetail;
import com.asiainfo.biapp.si.coc.jauth.log.service.ILogInterfaceDetailService;
import com.asiainfo.biapp.si.coc.jauth.log.vo.LogInterfaceDetailVo;

@Service
@Transactional
public class LogInterfaceDetailServiceImpl extends BaseServiceImpl<LogInterfaceDetail,String> implements ILogInterfaceDetailService {

    /** 缓存要入库的实体的池子 */
    private List<LogInterfaceDetail> savePool = new ArrayList<>();
    
	@Autowired
	private ILogInterfaceDetailDao iLogInterfaceDetailDao;
	
    @Override
    protected BaseDao<LogInterfaceDetail, String> getBaseDao() {
        return iLogInterfaceDetailDao;
    }
    
    @Override
    public void save(LogInterfaceDetail model) {
        System.out.println(this.getClass().getSimpleName()+".save()");
        savePool.add(model);    //加入缓存，等待入库
    }
    
    @Override
    public void taskSave() {
        System.out.println(this.getClass().getSimpleName()+".taskSave()入库,缓存入库实体池子大小："+savePool.size());
        if (null != savePool && !savePool.isEmpty()) {  //入库
            for (LogInterfaceDetail model : savePool) {
                super.save(model);
            }
            savePool.clear();
        }
    }

	@Override
	public JQGridPage<LogInterfaceDetail> findLogInterList(JQGridPage<LogInterfaceDetail> page,
			LogInterfaceDetailVo logInterfaceDetailVo) {
		return iLogInterfaceDetailDao.findLogInterList(page, logInterfaceDetailVo);
	}

}
