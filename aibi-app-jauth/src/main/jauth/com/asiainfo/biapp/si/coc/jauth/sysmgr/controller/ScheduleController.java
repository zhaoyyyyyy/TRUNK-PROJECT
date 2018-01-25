/*
 * @(#)ScheduleController.java
 *
 * CopyRight (c) 2018 北京亚信智慧数据科技有限公司 保留所有权利。
 */

package com.asiainfo.biapp.si.coc.jauth.sysmgr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.biapp.si.coc.jauth.common.ServiceConstants;
import com.asiainfo.biapp.si.coc.jauth.frame.controller.BaseController;
import com.asiainfo.biapp.si.coc.jauth.frame.exception.BaseException;
import com.asiainfo.biapp.si.coc.jauth.frame.json.JSONResult;
import com.asiainfo.biapp.si.coc.jauth.frame.page.JQGridPage;
import com.asiainfo.biapp.si.coc.jauth.frame.service.BaseService;
import com.asiainfo.biapp.si.coc.jauth.frame.ssh.extend.SpringContextHolder;
import com.asiainfo.biapp.si.coc.jauth.frame.util.LogUtil;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.component.DynamicTaskComponent;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Coconfig;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.LocTaskExeInfo;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.CoconfigService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.LocTaskExeInfoService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.vo.LocTaskExeInfoVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Title : ScheduleController
 * <p/>
 * Description :
 * <p/>
 * CopyRight : CopyRight (c) 2017
 * <p/>
 * Company : 北京亚信智慧数据科技有限公司
 * <p/>
 * JDK Version Used : JDK 1.8 +
 * <p/>
 * Modification History :
 * <p/>
 * 
 * <pre>
 * NO.    Date    Modified By    Why & What is modified
 * </pre>
 * 
 * <pre>
 * 1    2017年10月23日    chenchao3        Created
 * </pre>
 * <p/>
 *
 * @author chenchao3
 * @version 5.0.0.2017年10月23日
 */

@Api(value = "31.03-调度管理", description = "调度相关操作")
@RequestMapping("api/schedule")
@RestController
public class ScheduleController extends BaseController<LocTaskExeInfo> {

    private static final String LOC_CONFIG_SYS_TIMED_TASK = "LOC_CONFIG_SYS_TIMED_TASK";

    @Autowired
    private LocTaskExeInfoService locTaskExeInfoService;

    @Autowired
    private CoconfigService coconfigService;

    private static final String SUCCESS = "success";

    @Override
    protected BaseService<LocTaskExeInfo, String> getBaseService() {
        return locTaskExeInfoService;
    }

    /**
     * 构造树
     */
    @ApiOperation(value = "构造树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "sec", value = "异步处理", required = false, paramType = "query", dataType = "string", defaultValue = "true") })
    @RequestMapping(value = "/tree", method = RequestMethod.POST, produces = { MediaType.ALL_VALUE })
    public String renderOrgTree(String sec, Integer id) {
        if ("true".equals(sec)) {
            StringBuffer htmlC = new StringBuffer();
            LocTaskExeInfo locTaskExeInfo = locTaskExeInfoService.get(id);
            return getSubTree(locTaskExeInfo.getChildren(), htmlC);
        } else {
            LocTaskExeInfo locTaskExeInfo = locTaskExeInfoService.get(1);
            StringBuffer html = new StringBuffer();
            return getTree(locTaskExeInfo, html);
        }
    }

    // 子节点
    private String getTree(LocTaskExeInfo locTaskExeInfo, StringBuffer html) {
        html.append("<li id='").append(locTaskExeInfo.getTaskExeId()).append("' name='")
            .append(locTaskExeInfo.getTaskExeId()).append("' selectable='true'><span class='text'>")
            .append(locTaskExeInfo.getTaskExeName()).append("</span>");
        html.append("</span><ul  class='ajax'>");
        html.append("<li>{url: " + this.getRequest().getContextPath() + "/api/schedule/tree?sec=true&&id="
                + locTaskExeInfo.getTaskExeId() + "}</li></ul></li>");
        return html.toString();
    }

    // 最小节点
    private String getLeaf(LocTaskExeInfo locTaskExeInfo, StringBuffer htmlCon) {
        htmlCon.append("<li id='").append(locTaskExeInfo.getTaskExeId()).append("' name='")
            .append(locTaskExeInfo.getTaskExeId()).append("' selectable='true'");
        htmlCon.append(">");
        htmlCon.append("<span class='text'>");
        htmlCon.append(locTaskExeInfo.getTaskExeName());
        htmlCon.append("</span>");
        htmlCon.append("</li>");
        return htmlCon.toString();
    }

    private String getSubTree(Set<LocTaskExeInfo> set, StringBuffer htmlCon) {
        for (LocTaskExeInfo locTaskExeInfo : set) {
            if (!locTaskExeInfo.getChildren().isEmpty()) {
                getTree(locTaskExeInfo, htmlCon);
            } else {
                getLeaf(locTaskExeInfo, htmlCon);
            }
        }
        return htmlCon.toString();
    }

    /**
     * Description: 根据任务ID查询调度接口
     * 
     * @param taskId
     *        任务ID
     * @return
     */
    @ApiOperation(value = "分页显示列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cols", value = "列名称", required = true, paramType = "query", dataType = "string", defaultValue = "taskExeName,sysId,taskExeTime,exeStatus,exeType,taskExeId"),
            @ApiImplicitParam(name = "taskExeName", value = "名称", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "exeStatus", value = "状态", required = false, paramType = "query", dataType = "string") })
    @RequestMapping(value = "/taskExeInfo/query", method = RequestMethod.POST, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public String queryTaskExeInfo(@ModelAttribute JQGridPage<LocTaskExeInfo> page,
            @ApiIgnore LocTaskExeInfoVo locTaskExeInfoVo, String cols) {
        JQGridPage<LocTaskExeInfo> locTaskExeInfoList = locTaskExeInfoService.findLocTaskExeInfoList(page,
            locTaskExeInfoVo);
        return JSONResult.page2Json(locTaskExeInfoList, cols);
    }

    /**
     * 根据ID得到信息
     */
    @ApiOperation(value = "通过id查询")
    @ApiImplicitParam(name = "exeId", value = "id", required = true, paramType = "query", dataType = "int")
    @RequestMapping(value = "/taskExeInfo/get", method = RequestMethod.POST, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> getById(int exeId) {
        Map<String, Object> map = new HashMap<>();
        LocTaskExeInfo locTaskExeInfo = locTaskExeInfoService.get(exeId);
        map.put("locTaskExeInfo", locTaskExeInfo);
        return map;
    }

    /**
     * Description: 删除调度接口 taskExeId 调度编码
     * 
     * @return
     */
    @ApiOperation(value = "删除调度")
    @ApiImplicitParam(name = "taskExeId", value = "id", required = true, paramType = "query", dataType = "int")
    @RequestMapping(value = "/taskExeInfo/delete", method = RequestMethod.POST)
    public void deleteTaskExeInfo(Integer taskExeId) {
        locTaskExeInfoService.delete(taskExeId);
        
        //停止调度任务
        DynamicTaskComponent dSTaskUtil = (DynamicTaskComponent)SpringContextHolder.getBean("dynamicTaskComponent");
        dSTaskUtil.stopTask(String.valueOf(taskExeId));
    }

    /**
     * Description: 增加或修改调度接口
     * 
     * @param taskId
     *        任务ID
     * @param taskExeName
     *        任务执行的名字，不能重复
     * @param sysId
     *        任务执行参数
     * @param taskExeTime
     *        任务执行时间或延迟时间
     * @param parentExeId
     *        父执行信息ID
     * @param exeType
     *        执行类型 0.无；1.延迟执行；2.按执行时间执行
     * @return
     */
    @ApiOperation(value = "新增或修改调度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskExeId", value = "调度ID", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "taskExeName", value = "任务名称", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "sysId", value = "参数", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "taskExeTime", value = "执行参数", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "parentExeId", value = "父执行ID", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "exeType", value = "执行类型", required = false, paramType = "query", dataType = "string") })
    @RequestMapping(value = "/taskExeInfo/save", method = RequestMethod.POST)
    public String saveTaskExeInfo(@ApiIgnore LocTaskExeInfo locTask) {
        if (!"1".equals(locTask.getParentExeId()) && StringUtils.isBlank(locTask.getTaskExeTime())) {
            return "notime";
        }
        if (locTask.getTaskExeId() != null) {   //修改
            LocTaskExeInfo oldLocTask = locTaskExeInfoService.get(locTask.getTaskExeId());
            oldLocTask.setTaskId(locTask.getTaskId());
            oldLocTask.setTaskExeName(locTask.getTaskExeName());
            oldLocTask.setSysId(locTask.getSysId());
            oldLocTask.setExeType(locTask.getExeType());
            oldLocTask.setTaskExeTime(locTask.getTaskExeTime().replace(",", " ").trim());
            locTask = oldLocTask;
        } else {    //新增
            if (!SUCCESS.equals(this.queryNameExist(locTask.getTaskExeName()))) {
                return "failure";
            }
            if (StringUtils.isBlank(locTask.getTaskExeTime())) {
                locTask.setExeType(String.valueOf(ServiceConstants.TaskExeInfo.EXE_TYPE_DELAY));
            } else {
                
            }
            //默认任务是停止的
            locTask.setExeStatus(String.valueOf(ServiceConstants.TaskExeInfo.EXE_STATUS_NO));
            locTask.setTaskExeTime(locTask.getTaskExeTime().replace(",", " ").trim());
        }
        locTaskExeInfoService.saveOrUpdate(locTask);
        
        return SUCCESS;
    }

    /**
     * Description: 查询调度名称是否重复接口
     * 
     * @param taskExeName
     *        任务执行的名字，不能重复
     * @return
     */
    @ApiOperation(value = "查找是否重名")
    @ApiImplicitParam(name = "taskExeName", value = "名称", required = true, paramType = "query", dataType = "string")
    @RequestMapping(value = "/taskExeInfo/queryNameExist", method = RequestMethod.POST)
    public String queryNameExist(String taskExeName) {
        if (StringUtils.isBlank(taskExeName)) {
            return "empty";
        }
        LocTaskExeInfo locTaskExeInfo = locTaskExeInfoService.findOneByName(taskExeName);
        if (locTaskExeInfo != null) {
            return "exits";
        }
        return SUCCESS;
    }

    /**
     * Description: 调度状态修改接口
     * 
     * @param taskExeId
     *        需要修改的调度ID
     * @param exeStatus
     *        状态 0：停止；1：启动
     * @return
     */
    @ApiOperation(value = "修改状态")
    @ApiImplicitParam(name = "taskExeId", value = "ID", required = true, paramType = "query", dataType = "int")
    @RequestMapping(value = "/taskExeInfo/updateStatus", method = RequestMethod.POST)
    public void updateTaskExeInfoStatus(Integer taskExeId) {
        LocTaskExeInfo task = locTaskExeInfoService.get(taskExeId);
        // 原状态启用
        if (String.valueOf(ServiceConstants.TaskExeInfo.EXE_STATUS_YES).equals(task.getExeStatus())) {
            task.setExeStatus(String.valueOf(ServiceConstants.TaskExeInfo.EXE_STATUS_NO));
            
            //按执行时间周期执行
            if (String.valueOf(ServiceConstants.TaskExeInfo.EXE_TYPE_CIRCLE).equals(task.getExeType())) {
                //停止调度任务
                DynamicTaskComponent dSTaskUtil = (DynamicTaskComponent)SpringContextHolder.getBean("dynamicTaskComponent");
                dSTaskUtil.stopTask(String.valueOf(task.getTaskExeId()));
            }
        } else if (String.valueOf(ServiceConstants.TaskExeInfo.EXE_STATUS_NO).equals(task.getExeStatus())) {// 原状态停止
            task.setExeStatus(String.valueOf(ServiceConstants.TaskExeInfo.EXE_STATUS_YES));
            
            //启动调度任务
            //按执行时间执行
            boolean isSchedule = true;
            Long ms = -1L;
            if (String.valueOf(ServiceConstants.TaskExeInfo.EXE_TYPE_DELAY).equals(task.getExeType())) {
                //延迟执行
                isSchedule = false;
                ms = Integer.parseInt(task.getTaskExeTime().trim()) * 1000L;
            }
            String token = null;
            try {
                token = this.getToken();
            } catch (BaseException e) {
                LogUtil.error("获取当前请求的token出错");
            }
            locTaskExeInfoService.taskExeInfoSchedule(token,isSchedule, task, ms);
        }
        locTaskExeInfoService.saveOrUpdate(task);
    }

    /**
     * Description: 手动启动调度接口
     * 
     * @param taskExeId
     *        任务执行的ID
     * @param exeUserId
     *        操作人ID (???)
     * @return
     */
    @ApiOperation(value = "立即执行")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "taskExeId", value = "ID", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "sysId", value = "参数（可选）", paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "/taskExeInfo/start", method = RequestMethod.POST)
    public Map<String, Object> exeTaskExeInfo(Integer taskExeId, String sysId) {
        Map<String, Object> resMap = new HashMap<>();
        
        LocTaskExeInfo locTask = locTaskExeInfoService.get(taskExeId);
        LocTaskExeInfo task = (LocTaskExeInfo) locTask.clone();
        task.setSysId(sysId);

        String token = null;
        try {
            token = this.getToken();
        } catch (BaseException e) {
            LogUtil.error("获取当前请求的token出错");
        }
        resMap.put("res", locTaskExeInfoService.taskExeInfoSchedule(token,false, task, -1L));
        
        return resMap;
    }
    
    /**
     * Description: 根据父类key获取内容
     * 
     * @param ParentKey
     *        父类key
     * @return
     */
    @ApiOperation(value = "根据父类key获取配置内容")
    @ApiImplicitParam(name = "ParentKey", value = "ParentKey", required = true, paramType = "query", dataType = "string")
    @RequestMapping(value = "/taskExeInfo/getCoconfigByParentKey", method = RequestMethod.POST)
    public List<Coconfig> getCoconfigByParentKey(String ParentKey) {
        String pk = ParentKey;
        if (StringUtils.isBlank(ParentKey)) {
            pk = LOC_CONFIG_SYS_TIMED_TASK;
        }
        List<Coconfig> coconfigs = coconfigService.getCoconfigByParentKey(pk);
        
        return coconfigs;
    }
    
    
    
}
