/*
 * @(#)DynamicTaskUtil.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.sysmgr.task.service;

import java.util.Map;

/**
 * Title : DynamicTaskUtil
 * <p/>
 * Description : 线程池任务调度工具类里的任务接口
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

public interface IDynamicTask extends Runnable {

    /**
     * @Description:传入参数
     * @param Map<String, Object> parameters
     */
    public void initParameters(Map<String, Object> parameters);
    
    /**
     * @Description:to do anything you what
     */
    public void run();
    
}
