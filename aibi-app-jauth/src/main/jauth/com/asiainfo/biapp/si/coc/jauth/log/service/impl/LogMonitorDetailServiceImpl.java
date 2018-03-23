
package com.asiainfo.biapp.si.coc.jauth.log.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.biapp.si.coc.jauth.frame.dao.BaseDao;
import com.asiainfo.biapp.si.coc.jauth.frame.page.JQGridPage;
import com.asiainfo.biapp.si.coc.jauth.frame.service.impl.BaseServiceImpl;
import com.asiainfo.biapp.si.coc.jauth.frame.ssh.extend.SpringContextHolder;
import com.asiainfo.biapp.si.coc.jauth.frame.util.DateUtil;
import com.asiainfo.biapp.si.coc.jauth.frame.util.FileUtil;
import com.asiainfo.biapp.si.coc.jauth.frame.util.LogUtil;
import com.asiainfo.biapp.si.coc.jauth.frame.util.StringUtil;
import com.asiainfo.biapp.si.coc.jauth.log.dao.ILogMonitorDetailDao;
import com.asiainfo.biapp.si.coc.jauth.log.entity.LogMonitorDetail;
import com.asiainfo.biapp.si.coc.jauth.log.service.ILogMonitorDetailService;
import com.asiainfo.biapp.si.coc.jauth.log.vo.LogMonitorDetailVo;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Coconfig;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.CoconfigService;

@Service
@Transactional
public class LogMonitorDetailServiceImpl extends BaseServiceImpl<LogMonitorDetail,String> implements ILogMonitorDetailService {

    /** 缓存要入库的实体的池子 */
    private List<LogMonitorDetail> savePool = new LinkedList<>();

    private int poolSaveSize = 200;
    private int bufferedRowSize = 10000;    //每次读取数据的条数
    
	@Autowired
	private ILogMonitorDetailDao iLogMonitorDetailDao;
	public LogMonitorDetailServiceImpl() {
		CoconfigService configService = (CoconfigService) SpringContextHolder.getBean("coconfigServiceImpl");
        Coconfig poolSaveSizeConf = configService.getCoconfigByKey("LOC_CONFIG_APP_LOG_POOL_SAVE_SIZE");
        if (null != poolSaveSizeConf) {
	        	if (StringUtil.isNotEmpty(poolSaveSizeConf.getConfigVal())) {
	        		poolSaveSize = Integer.parseInt(poolSaveSizeConf.getConfigVal());
	        	}
        }
        
        Coconfig pageSzieConf = configService.getCoconfigByKey("EXPORT_TO_FILE_PAGESIZE"); // 查询每页大小
        if (null != pageSzieConf) {
	        	if (StringUtil.isNotEmpty(pageSzieConf.getConfigVal())) {
	        		bufferedRowSize = Integer.parseInt(pageSzieConf.getConfigVal());
	        	}
	    }
        
	}
	
    @Override
    protected BaseDao<LogMonitorDetail, String> getBaseDao() {
        return iLogMonitorDetailDao;
    }
    
    @Override
    public void save(LogMonitorDetail model) {
//        LogUtil.debug(this.getClass().getSimpleName()+".save()");
        
        savePool.add(model);    //加入缓存，等待入库

        //当日志数量大于poolSaveSize时，自动入库
        if (null != savePool && savePool.size() > poolSaveSize) {  //入库
        		LogUtil.debug("auto insert DB...");
    		
            this.taskSave();
        }
    }
    
    public void saveRightNow(LogMonitorDetail model){
//    	LogUtil.debug(this.getClass().getSimpleName()+".saveRightNow()");
    	super.save(model);
    }
    
    @Override
    public void taskSave() {
//        LogUtil.debug(this.getClass().getSimpleName()+".taskSave()入库,缓存入库实体池子大小："+savePool.size());
        
        if (null != savePool && !savePool.isEmpty()) {  //入库
            for (LogMonitorDetail model : savePool) {
                //解决:org.hibernate.PersistentObjectException: detached entity passed to persist:
                if (StringUtil.isNotBlank(model.getLogId())) {
                    model.setLogId(null);
                }
//                LogUtil.debug("入库："+model.toString());
                super.save(model);
            }
            savePool.clear();
        }
    }
    
    @Override
    public JQGridPage<LogMonitorDetail> findLogMonitorList(JQGridPage<LogMonitorDetail> page,
            LogMonitorDetailVo logMonitorDetailVo) {
        return iLogMonitorDetailDao.findLogMonitorList(page, logMonitorDetailVo);
    }
    
    
}
