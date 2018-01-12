/*
 * @(#)DynamicTaskComponent.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.sysmgr.component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.asiainfo.biapp.si.coc.jauth.frame.util.StringUtil;

/**
 * Title : DynamicTaskComponent
 * <p/>
 * Description : 线程池任务调度工具类，能够动态增加/停止调度任务
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
 * <pre>1    2018年1月6日    hongfb        Created</pre>
 * <p/>
 *
 * @author  hongfb
 * @version 1.0.0.2018年1月6日
 */

@Component
public class DynamicTaskComponent {
 
    /** 线程池任务调度类，能够开启线程池进行任务调度 */
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    /** 定时计划任务池 */
    private Map<String, ScheduledFuture<?>> tasks = new HashMap<>();
 
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
       return new ThreadPoolTaskScheduler();
    }

    /**
     * 启动定时任务
     * @param taskId 任务id
     * @param task 具体任务
     * @param cron cron表达式
     */ 
    public String startTask(String taskId, Runnable task, String cron) {
        System.out.println("任务("+taskId+")正加入任务池。。。");
        if (this.isExistsTask(taskId)) {
            this.stopTask(taskId);// 先停止，在开启.
        }
        tasks.put(taskId, threadPoolTaskScheduler.schedule(task, new CronTrigger(cron)));
       
       return "startTask";
    }
 
    /**
     * 停止定时任务
     * @param taskId 任务id
     */ 
    public String stopTask(String taskId) {
        if (this.isExistsTask(taskId)) {
            tasks.get(taskId).cancel(true);
            tasks.remove(taskId);
        }
        
        return "stopTask";
    }

    /**
     * 某个定时任务是否存在
     * @param taskId 任务id
     */ 
    private boolean isExistsTask(String taskId) {
        boolean res = false;
        if (StringUtil.isNoneBlank(taskId)) {
            if (null != tasks.get(taskId)) {
                res = true;
            }
        }
        return res;
    }
    
 
}