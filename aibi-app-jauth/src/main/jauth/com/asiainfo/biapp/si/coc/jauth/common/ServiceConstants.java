/*
 * @(#)ServiceConstants.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.common;

/**
 * Title : ServiceConstants
 * <p/>
 * Description : 表级常量
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

public class ServiceConstants {
	
    public interface TaskExeInfo{
        
        /** 
         * 执行类型:EXE_TYPE:1.延迟执行；2.按执行时间周期执行
         */
        /** 执行类型:EXE_TYPE:1.延迟执行 */
        public static final int EXE_TYPE_DELAY = 1;
        /** 执行类型:EXE_TYPE:2.按执行时间周期执行 */
        public static final int EXE_TYPE_CIRCLE = 2;
        
        /** 
         * 状态:EXE_STATUS:2：停止，1：启动
         */
        /** 状态:1：启动; */
        public static final int EXE_STATUS_YES = 1;
        /** 状态:2：停止; */
        public static final int EXE_STATUS_NO = 2;
        
        
    }
    
    
}
