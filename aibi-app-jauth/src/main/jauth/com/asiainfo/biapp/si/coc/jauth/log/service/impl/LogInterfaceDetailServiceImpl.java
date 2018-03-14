
package com.asiainfo.biapp.si.coc.jauth.log.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.biapp.si.coc.jauth.frame.dao.BaseDao;
import com.asiainfo.biapp.si.coc.jauth.frame.page.JQGridPage;
import com.asiainfo.biapp.si.coc.jauth.frame.service.impl.BaseServiceImpl;
import com.asiainfo.biapp.si.coc.jauth.frame.ssh.extend.SpringContextHolder;
import com.asiainfo.biapp.si.coc.jauth.frame.util.LogUtil;
import com.asiainfo.biapp.si.coc.jauth.frame.util.StringUtil;
import com.asiainfo.biapp.si.coc.jauth.log.dao.ILogInterfaceDetailDao;
import com.asiainfo.biapp.si.coc.jauth.log.entity.LogInterfaceDetail;
import com.asiainfo.biapp.si.coc.jauth.log.service.ILogInterfaceDetailService;
import com.asiainfo.biapp.si.coc.jauth.log.vo.LogInterfaceDetailVo;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Coconfig;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.CoconfigService;

@Service
@Transactional
public class LogInterfaceDetailServiceImpl extends BaseServiceImpl<LogInterfaceDetail,String> implements ILogInterfaceDetailService {

    /** 缓存要入库的实体的池子 */
    private List<LogInterfaceDetail> savePool = new LinkedList<>();

    private int poolSaveSize = 200;
	public LogInterfaceDetailServiceImpl() {
		CoconfigService configService = (CoconfigService) SpringContextHolder.getBean("coconfigServiceImpl");
        Coconfig poolSaveSizeConf = configService.getCoconfigByKey("LOC_CONFIG_APP_LOG_POOL_SAVE_SIZE");
        if (null != poolSaveSizeConf) {
	        	if (StringUtil.isNotEmpty(poolSaveSizeConf.getConfigVal())) {
	        		poolSaveSize = Integer.parseInt(poolSaveSizeConf.getConfigVal());
	        	}
        }
	}
    
	@Autowired
	private ILogInterfaceDetailDao iLogInterfaceDetailDao;
	
    @Override
    protected BaseDao<LogInterfaceDetail, String> getBaseDao() {
        return iLogInterfaceDetailDao;
    }
    
    @Override
    public void save(LogInterfaceDetail model) {
        LogUtil.debug(this.getClass().getSimpleName()+".save()");

        savePool.add(model);    //加入缓存，等待入库
        
        //当日志数量大于poolSaveSize时，自动入库
        if (null != savePool && savePool.size() > poolSaveSize) {  //入库
	        	LogUtil.debug("auto insert DB...");
        		
            this.taskSave();
        }
    }
    
    @Override
    public void taskSave() {
        LogUtil.debug(this.getClass().getSimpleName()+".taskSave()入库,缓存入库实体池子大小："+savePool.size());
        if (null != savePool && !savePool.isEmpty()) {  //入库
            for (LogInterfaceDetail model : savePool) {
                //解决:org.hibernate.PersistentObjectException: detached entity passed to persist:
                if (StringUtil.isNotBlank(model.getLogId())) {
                    model.setLogId(null);
                }
                LogUtil.debug("入库："+model.toString());
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
