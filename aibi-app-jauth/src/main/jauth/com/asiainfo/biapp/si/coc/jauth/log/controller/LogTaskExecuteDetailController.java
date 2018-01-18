
package com.asiainfo.biapp.si.coc.jauth.log.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.biapp.si.coc.jauth.frame.controller.BaseController;
import com.asiainfo.biapp.si.coc.jauth.frame.json.JSONResult;
import com.asiainfo.biapp.si.coc.jauth.frame.page.JQGridPage;
import com.asiainfo.biapp.si.coc.jauth.frame.service.BaseService;
import com.asiainfo.biapp.si.coc.jauth.frame.util.LogUtil;
import com.asiainfo.biapp.si.coc.jauth.frame.util.WebResult;
import com.asiainfo.biapp.si.coc.jauth.log.entity.LogTaskExecuteDetail;
import com.asiainfo.biapp.si.coc.jauth.log.service.ILogTaskExecuteDetailService;
import com.asiainfo.biapp.si.coc.jauth.log.vo.LogTaskExecuteDetailVo;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.LocTaskExeInfo;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.LocTaskExeInfoService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.utils.SessionInfoHolder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * Title : LogTaskExecuteDetailController
 * <p/>
 * Description : 
 * <p/>
 * CopyRight : CopyRight (c) 2017
 * <p/>
 * Company : 北京亚信智慧数据科技有限公司
 * <p/>
 * JDK Version Used : JDK 5.0 +
 * <p/>
 * Modification History	:
 * <p/>
 * <pre>NO.    Date    Modified By    Why & What is modified</pre>
 * <pre>1    2017年10月24日    panweiwei        Created</pre>
 * <p/>
 *
 * @author  panweiwei
 * @version 1.0.0.2017年10月24日
 */

@Api(value="32.04-系统调度日志",description="系统调度日志")
@RequestMapping("api/log/taskexecute")
@RestController
public class LogTaskExecuteDetailController  extends BaseController<LogTaskExecuteDetail>{
    
    public static final String RETURN_SUCCESS = "success";

    @Autowired
    private LocTaskExeInfoService locTaskExeInfoService;
    
	@Autowired
	private ILogTaskExecuteDetailService logtaskexecuteService;

    @Autowired
    private SessionInfoHolder sessionInfoHolder;
	
    @Override
    protected BaseService<LogTaskExecuteDetail, String> getBaseService() {
        return logtaskexecuteService;
    }
    /**
     * 
     * Description: 分页查询方法
     *
     * @param page
     * @param cols
     */
    @ApiOperation(value="显示系统调度日志(分页形式)")
    @ApiImplicitParams({
        @ApiImplicitParam(name="cols",value="列名称",required=true,paramType="query",dataType="string",defaultValue="startTime,exeType,status,returnMsg,userId"),
        @ApiImplicitParam(name="stratTimeStart",value="调度时间(开始)",required=false,paramType="query",dataType="string"),
        @ApiImplicitParam(name="startTimeEnd",value="调度时间(结束)",required=false,paramType="query",dataType="string"),
        @ApiImplicitParam(name="status",value="响应状态",required=false,paramType="query",dataType="string"),
        @ApiImplicitParam(name="userId",value="执行人",required=false,paramType="query",dataType="string")
    })
    @RequestMapping(value="/taskexecutePage/query",method=RequestMethod.POST,produces={ MediaType.APPLICATION_JSON_VALUE })
    public String queryPageByParams(@ModelAttribute JQGridPage<LogTaskExecuteDetail> page,String cols,
            @ApiIgnore LogTaskExecuteDetailVo logTaskExecuteDetailVo){
        JQGridPage<LogTaskExecuteDetail> taskexecuteList = logtaskexecuteService.findTaskExeList(page, logTaskExecuteDetailVo);
        return JSONResult.page2Json(taskexecuteList,cols); 
    }
    /**
     * 
     * Description: 保存方法
     *
     * @param logTaskExecuteDetail
     */
    @ApiOperation(value="保存")
    @ApiImplicitParams({
        @ApiImplicitParam(name="userId",value="执行人",required=false,paramType="query",dataType="string"),
        @ApiImplicitParam(name="sysId",value="系统进程",required=false,paramType="query",dataType="string"),
        @ApiImplicitParam(name="taskExtId",value="调度id",required=false,paramType="query",dataType="string"),
        @ApiImplicitParam(name="startTime",value="调度时间",required=false,paramType="query",dataType="date"),
        @ApiImplicitParam(name="status",value="响应状态",required=false,paramType="query",dataType="string"),
        @ApiImplicitParam(name="resultMsg",value="响应信息",required=false,paramType="query",dataType="string"),
        @ApiImplicitParam(name="nodeName",value="节点名称",required=false,paramType="query",dataType="string")
    })
    @RequestMapping(value="/save",method=RequestMethod.POST)
    public String saveLogTaskExecuteDetail(@ApiIgnore LogTaskExecuteDetail logTaskExecuteDetail){
        LocTaskExeInfo locTaskExeInfo = locTaskExeInfoService.get(logTaskExecuteDetail.getTaskExtId());
        logTaskExecuteDetail.setExeParams(locTaskExeInfo.getSysId());  
        logtaskexecuteService.save(logTaskExecuteDetail);
        return RETURN_SUCCESS;
    }

    /**
     * @describe 入库
     * @author hongfb
     * @param
     * @date 2018-1-16
     */
    @ApiOperation(value="保存任务")
    @RequestMapping(value="/taskSave",method=RequestMethod.POST)
    public String taskSave() {
        System.out.println(this.getClass().getSimpleName()+".taskSave()"+ new Date().toLocaleString());
        System.out.println("5s一次入库开始。。。");
        logtaskexecuteService.taskSave();
        return RETURN_SUCCESS;
    }
    /**
     * @describe 入库
     * @author hongfb
     * @param
     * @date 2018-1-16
     */
    @ApiOperation(value="测试保存任务")
    @RequestMapping(value="/testSave",method=RequestMethod.POST)
    public String testSave() {
        System.out.println(this.getClass().getSimpleName()+".save()"+new Date().toLocaleString());
        System.out.println("一次日志请求开始。。。");
        
        long s = System.currentTimeMillis();
        LogTaskExecuteDetail log = new LogTaskExecuteDetail();
        log.setUserId("admin");
        log.setStartTime(new Date());
        log.setExeType("1");
        log.setTaskExtId(Thread.currentThread().getName());
        log.setStatus(String.valueOf(WebResult.Code.OK));
        log.setEndTime(new Date());
        logtaskexecuteService.save(log);
        
        long es = System.currentTimeMillis() - s;
        System.out.println("es:"+es+"ms");
        if (es <= 1000) {
            Long ms = 1000 - es;
            System.out.println("ms:"+ms+"ms");
            System.out.println("睡"+(ms/1000.0)+"秒。");
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                LogUtil.error("error in sleep!");
            }
            System.out.println("睡醒啦。");
        }
        return RETURN_SUCCESS;
    }
    
    
}
