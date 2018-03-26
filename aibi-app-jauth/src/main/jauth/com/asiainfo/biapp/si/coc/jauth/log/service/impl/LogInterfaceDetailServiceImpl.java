
package com.asiainfo.biapp.si.coc.jauth.log.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
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
    private static final String BEAN2TABEL_NAME = "LOC_LOG_INTER_DETAIL";
    private static final String BEAN2FIELD = "LOG_ID,USER_ID,SYS_ID,INTERFACE_NAME,INTERFACE_URL,OP_TIME,IP_ADDR,OUTPUT_PARAMS,INPUT_PARAMS";
    private static final String encode = "UTF-8"; 			//当前的文件的编码
    private static final String FILE_PATH = "/log/interfce/"; 		//推送的文件的目录名称
    private int bufferedRowSize = 1000;    //每次读取数据的条数
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
//        LogUtil.debug(this.getClass().getSimpleName()+".save()");

        savePool.add(model);    //加入缓存，等待入库
        
        //当日志数量大于poolSaveSize时，自动入库
        if (null != savePool && savePool.size() > poolSaveSize) {  //入库
	        	LogUtil.debug("auto insert DB...");
        		
            this.taskSave();
        }
    }
    
    @Override
    public void taskSave() {
//        LogUtil.debug(this.getClass().getSimpleName()+".taskSave()入库,缓存入库实体池子大小："+savePool.size());
        if (null != savePool && !savePool.isEmpty()) {  //入库
            for (LogInterfaceDetail model : savePool) {
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
    public boolean taskWriteLog() {
        LogUtil.debug(this.getClass().getSimpleName()+".taskWriteLog()");
		long s = System.currentTimeMillis();
        
        //1. 备份表，清空表
		//1.1 备份表
        String oldTableName = BEAN2TABEL_NAME;
        Date nowdate = new Date();
		String newTabelName = BEAN2TABEL_NAME + "_" + DateUtil.format(nowdate,DateUtil.FMT_DATE_YYYYMMDD);
		final String newTabelNameF = newTabelName;	//供线程使用
		Integer no = this.CreateTable(oldTableName, newTabelName, true);
		LogUtil.debug(oldTableName+"———bak———>"+newTabelName+" end,cost:"+((System.currentTimeMillis()-s)/1000L) + " s.");
		
        //1.2 计算总条数，清空表
		long s1 = System.currentTimeMillis();
		StringBuffer sqlb = new StringBuffer();
        sqlb.append("SELECT COUNT(1) from ").append(newTabelName);
        no = this.getCountSql(sqlb.toString(), new Object[0]);
        LogUtil.debug(newTabelName + " 表的总条数：" + no);
        LogUtil.debug(newTabelName + " 表的总条数sql："+sqlb.toString()+",cost:"+(System.currentTimeMillis()-s1) + " ms.");
		/*
        if (no == 0) {	//防止数据没进备份表
		//INSERT INTO aa SELECT * FROM a;
			sqlb.delete(0, sqlb.length()).append("INSERT INTO ").append(newTabelName).append(" select ")
				.append("* from ").append(oldTableName);
			LogUtil.debug("插入数据sql："+sqlb.toString());
			no = this.excuteSql(sqlb.toString(), new Object[0]);
			LogUtil.debug("插入数据总条数：" + no);
		}*/
        
        //清空表
        s1 = System.currentTimeMillis();
		no = this.truncateTable(oldTableName);	
		LogUtil.debug("truncate Table "+oldTableName+" end,cost:"+(System.currentTimeMillis()-s1) + " ms.");
		
		//2. 写文件
		new Thread(new Runnable() {
			@Override
			public void run() {
				long s = System.currentTimeMillis();
				
				boolean flag = false;
				ILogInterfaceDetailService logInterService = (ILogInterfaceDetailService) SpringContextHolder.getBean("logInterfaceDetailServiceImpl");
				
				StringBuffer sqlb = new StringBuffer();
				//2.1 统计总条数
				sqlb.append("SELECT COUNT(1) from ").append(newTabelNameF).append(" o ");
		        LogUtil.debug(newTabelNameF + " 表的总条数sql："+sqlb.toString());
				Integer countNO = logInterService.getCountSql(sqlb.toString(), new Object[0]);
				LogUtil.debug(newTabelNameF + " 表的总条数：" + countNO);
				if (null != countNO && countNO > 0) {  //写文件
		    			CoconfigService configService = (CoconfigService) SpringContextHolder.getBean("coconfigServiceImpl");
		            String fileName = "";  
		    			//2.2 本地缓冲目录
		            Coconfig localPathConf = configService.getCoconfigByKey("LOC_CONFIG_SYS_TEMP_PATH"); // 本地缓冲目录
		            if (null != localPathConf) {
			            if (StringUtil.isNotBlank(localPathConf.getConfigVal())) {//以缓冲目录为准
				            	fileName =  localPathConf.getConfigVal();
				            	if (localPathConf.getConfigVal().endsWith(File.separator)) {
				            		fileName = fileName.substring(0, fileName.length()-2);
				            	}
			            }
		            }
		            fileName += FILE_PATH + newTabelNameF + ".csv";
		        		LogUtil.debug(newTabelNameF+" bak file：" + fileName);
		            
		        		//2.3 页数，循环次数
		        		int pageNO = 1;
		        		if (countNO > bufferedRowSize) {
		        			pageNO = (int) Math.ceil(countNO / bufferedRowSize);
		        		}
		        		LogUtil.debug("读取数据库次数：" + pageNO);
		        		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		        		List<?> page = new ArrayList<>();
		        		Map<String, String> objMap = null;
		        		//2.4 写表头
		            flag = FileUtil.writeToTextFile1(dataList, BEAN2FIELD, fileName, encode);
		            sqlb = new StringBuffer(sqlb.toString().replace("COUNT(1)", "*")).append("ORDER BY o.OP_TIME asc")
		            		.append(" limit 0,")	.append(bufferedRowSize);
        	        		Object[] log = null;
        	        		//2.5 写数据
		        		for (int i = 0; i < pageNO; i++) {
		        			if (i != 0) {
		        				sqlb = new StringBuffer(sqlb.toString().replace(((i-1)*bufferedRowSize)+",", i*bufferedRowSize+","));
		        			}

		    		        LogUtil.debug(newTabelNameF + "的第"+(i+1)+"页数据sql："+sqlb.toString());
		        	        page = logInterService.findListBySql(sqlb.toString(), new Object[0]);
		        	        //数据结构转换
		        	        for (Object obj : page) {
		        	        		log = (Object[]) obj;
		        	        		objMap = new LinkedHashMap<>();	//此处有序写表结构
			        	        	objMap.put("logId", String.valueOf(log[0]));
			        	        	objMap.put("userId", String.valueOf(log[1]));
			        	        	objMap.put("sysId", String.valueOf(log[2]));
			        	        	objMap.put("interfaceName", String.valueOf(log[3]));
			        	        	objMap.put("interfaceUrl", String.valueOf(log[4]));
			        	        	objMap.put("opTime", String.valueOf(log[5]));
			        	        	objMap.put("ipAddr", String.valueOf(log[6]));
			        	        	objMap.put("outputParams", String.valueOf(log[7]));
			        	        	objMap.put("inputParams", String.valueOf(log[8]));
			        	        	
			        	        	dataList.add(objMap);
						}
		        	        //写数据
		        	        flag = FileUtil.writeToTextFile1(dataList, null, fileName, encode);
		        	        dataList.clear();
					}
		        }
				//2.6 删除备份表
				sqlb.delete(0, sqlb.length()).append("drop table if exists ").append(newTabelNameF);
				LogUtil.debug("删除备份表sql:"+sqlb.toString());
				countNO = logInterService.excuteSql(sqlb.toString(), new Object[0]);
				
				LogUtil.debug("Interface log bak end:" + flag + ",cost:"+((System.currentTimeMillis()-s)/1000L) + " s.");
			}
		}).start();
        
		return true;
    }
	@Override
	public JQGridPage<LogInterfaceDetail> findLogInterList(JQGridPage<LogInterfaceDetail> page,
			LogInterfaceDetailVo logInterfaceDetailVo) {
		return iLogInterfaceDetailDao.findLogInterList(page, logInterfaceDetailVo);
	}

}
