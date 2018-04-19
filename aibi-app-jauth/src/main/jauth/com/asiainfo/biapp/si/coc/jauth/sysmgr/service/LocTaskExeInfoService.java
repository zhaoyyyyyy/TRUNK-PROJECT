package com.asiainfo.biapp.si.coc.jauth.sysmgr.service;

import com.asiainfo.biapp.si.coc.jauth.frame.page.JQGridPage;
import com.asiainfo.biapp.si.coc.jauth.frame.service.BaseService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.LocTaskExeInfo;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.vo.LocTaskExeInfoVo;

/**
 * @describe
 * @author zhangnan
 * @date 2017年10月23日 下午1:52:27
 */
public interface LocTaskExeInfoService extends BaseService<LocTaskExeInfo, String> {

	public LocTaskExeInfo findOneByName(String taskExeName);
	
	public JQGridPage<LocTaskExeInfo> findLocTaskExeInfoList(JQGridPage<LocTaskExeInfo> page, LocTaskExeInfoVo locTaskExeInfoVo);

    /**
     * @Description:通过taskExeInfo执行调度任务 
     * @param token     token
     * @param isSchedule    是否是调度任务：ture：是；false：不是，请立即/延迟执行
     * @param locTask   LocTaskExeInfo实体
     * @param ms   延迟毫秒数
     * @return 执行成功或失败：并不代表任务线程的成功或失败
     */
    public boolean taskExeInfoSchedule(String token,boolean isSchedule, LocTaskExeInfo locTask, Long ms);
    

    /**
     *  判断是否是有效的cron表达式
     * @param cronExpression    cron表达式
     * @return
     */
    public boolean isCronExpression(final String cronExpression);
}
