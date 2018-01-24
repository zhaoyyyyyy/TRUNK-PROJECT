/*
 * @(#)InitComponent.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.sysmgr.component;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.asiainfo.biapp.si.coc.jauth.frame.ssh.extend.SpringContextHolder;
import com.asiainfo.biapp.si.coc.jauth.frame.util.LogUtil;
import com.asiainfo.biapp.si.coc.jauth.log.service.ILogInterfaceDetailService;

/**
 * Title : InitComponent
 * <p/>
 * Description : InitComponent
 * <p/>
 * CopyRight : CopyRight (c) 2018
 * <p/>
 * Company : 北京亚信智慧数据科技有限公司
 * <p/>
 * JDK Version Used : JDK 7.0 +
 * <p/>
 * Modification History :
 * <p/>
 * <pre>NO.    Date    Modified By    Why & What is modified</pre>
 * <pre>1    2018年1月23日    zhougz        Created</pre>
 * <p/>
 *
 * @author  zhougz
 * @version 1.0.0.2018年1月23日
 */

@Component
public class InitComponent implements CommandLineRunner {
	
	@Override
    public void run(String... args) throws Exception {
		startTaskProcess();
    }
	  
	/**
	 * 开始定时任务进程
	 * Description: 
	 *
	 * @return
	 */
	public boolean startTaskProcess() {
		//拿到一个bean
		ILogInterfaceDetailService logInterfaceDetailServiceImpl = (ILogInterfaceDetailService) SpringContextHolder.getBean("logInterfaceDetailServiceImpl");
		logInterfaceDetailServiceImpl.get("1");
		
		LogUtil.debug("开始加载配置好的定时任务");
		return true;
	}
}
