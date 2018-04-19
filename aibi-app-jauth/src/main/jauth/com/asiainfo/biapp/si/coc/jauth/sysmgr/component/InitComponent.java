/*
 * @(#)InitComponent.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.sysmgr.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.asiainfo.biapp.si.coc.jauth.common.ServiceConstants;
import com.asiainfo.biapp.si.coc.jauth.frame.page.JQGridPage;
import com.asiainfo.biapp.si.coc.jauth.frame.ssh.extend.SpringContextHolder;
import com.asiainfo.biapp.si.coc.jauth.frame.util.LogUtil;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.LocTaskExeInfo;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.LocTaskExeInfoService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.vo.LocTaskExeInfoVo;

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

    @Autowired
    private LocTaskExeInfoService locTaskExeInfoService;
	
	@Override
    public void run(String... args) throws Exception {
		startTaskProcess();
    }
	  
	/**
	 * Description: 开始定时任务进程
	 *
	 * @return
	 */
	public boolean startTaskProcess() {
		LogUtil.debug("加载配置好的定时任务 start...");
		//把调度中心已经配置好的周期性的已启动的任务，随项目启动而启动起来
		LocTaskExeInfoService taskExeInfoService = (LocTaskExeInfoService) SpringContextHolder.getBean("locTaskExeInfoServiceImpl");
		
		JQGridPage<LocTaskExeInfo> page = new JQGridPage<LocTaskExeInfo>(1,Integer.MAX_VALUE);
		LocTaskExeInfoVo taskExeInfoVo = new LocTaskExeInfoVo();
		taskExeInfoVo.setExeType(String.valueOf(ServiceConstants.TaskExeInfo.EXE_TYPE_CIRCLE));
		taskExeInfoVo.setExeStatus(String.valueOf(ServiceConstants.TaskExeInfo.EXE_STATUS_YES));
		
        JQGridPage<LocTaskExeInfo> taskExeInfos = taskExeInfoService.findLocTaskExeInfoList(page, taskExeInfoVo);
		if (null != taskExeInfos) {
		    LogUtil.debug("配置好自启动任务的个数是："+taskExeInfos.getData().size());
		    String token = null;
            boolean isSchedule = true;  //按执行时间执行
            Long delayTime = -1L;   //延迟毫秒数，周期性任务，无延迟
		    for (LocTaskExeInfo taskExeInfo : taskExeInfos.getData()) {
		        //启动调度任务
	            locTaskExeInfoService.taskExeInfoSchedule(token,isSchedule, taskExeInfo, delayTime);
            }
		}
		LogUtil.debug("加载配置好的定时任务 end...");
		
		return true;
	}
	
	
}
