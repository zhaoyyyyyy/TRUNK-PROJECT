package com.asiainfo.cp.acrm.authintegre.controller;

import com.ai.secframe.client.OrgmodelClient;
import com.ai.secframe.client.SecframeClient;
import com.ai.secframe.orgmodel.bo.BOSecStaffBean;
import com.ai.secframe.orgmodel.ivalues.IBOSecDistrictValue;
import com.ai.secframe.orgmodel.ivalues.IBOSecOperatorValue;
import com.ai.secframe.orgmodel.ivalues.IBOSecOrganizeValue;
import com.ai.secframe.orgmodel.ivalues.IBOSecStaffValue;
import com.asiainfo.cp.acrm.authintegre.vo.AuthResourceResult;
import com.asiainfo.cp.acrm.authintegre.vo.ReqStaff;
import com.asiainfo.cp.acrm.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;


@Api(value = "04 获取用户资源权限",description="包装OCRM提供的相关接口并对外提供rest服务")
@RequestMapping("api/auth")
@RestController
public class AuthResourceController extends BaseController {

    Log log = LogFactory.getLog(AuthResourceController.class);
    private static final int SUCESS_CODE = 0;
    private static final int NODATA_CODE = -1;
    private static final int FAIL_CODE = -2;
    private static final String SUCESS_MSG = "成功";
    private static final String NODATA_MSG = "数据为空";
    private static final String FAIL_MSG = "失败";


    @ApiOperation(value = "根据员工ID获取用户信息")
    @ApiImplicitParam(name = "operatorId", value = "操作员ID(数字)", required = true, paramType = "query", dataType = "string")
    @RequestMapping(value = "/userdata", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
    public AuthResourceResult userdata(long operatorId) {

        AuthResourceResult arresult = new AuthResourceResult();
        try {
            IBOSecStaffValue value = (BOSecStaffBean)OrgmodelClient.getStaffById(operatorId);
            Map staffvalue = value.getProperties();
            if(value != null){
                arresult.setCode(SUCESS_CODE);
                arresult.setMsg(SUCESS_MSG);
                arresult.setData(staffvalue);
            }else{
                arresult.setCode(NODATA_CODE);
                arresult.setMsg(NODATA_MSG);
                arresult.setData(null);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            arresult.setCode(FAIL_CODE);
            arresult.setMsg(FAIL_MSG);
            arresult.setData(null);
        }
        return arresult;
    }

    //暂时屏蔽
    //@ApiOperation(value = "根据操作员ID，实体ID与行为ID查询操作员是否具有对该实体的操作权限")
    @RequestMapping(value = "/optauth", method = RequestMethod.POST)
    public AuthResourceResult optauth(HttpServletRequest request, @RequestBody ReqStaff staff) {

        AuthResourceResult arresult = new AuthResourceResult();
        try {
            Map map = SecframeClient.checkEntityPermission(staff.getOperId(), staff.getEntId(),staff.getPrivId());
            if(map != null){
                arresult.setCode(SUCESS_CODE);
                arresult.setMsg(SUCESS_MSG);
                arresult.setData(map);
            }else{
                arresult.setCode(NODATA_CODE);
                arresult.setMsg(NODATA_MSG);
                arresult.setData(null);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            arresult.setCode(FAIL_CODE);
            arresult.setMsg(FAIL_MSG);
            arresult.setData(null);
        }
        return arresult;
    }

    @ApiOperation(value = "通过组织ID获取组织信息")
    @RequestMapping(value = "/organization", method = RequestMethod.POST)
    @ApiImplicitParam(name = "orgId", value = "组织ID(数字)", required = true, paramType = "query", dataType = "string")
    public AuthResourceResult userauth(long orgId) {

        AuthResourceResult arresult = new AuthResourceResult();
        try {
            IBOSecOrganizeValue orgauth = OrgmodelClient.getOrganizeByOrgId (orgId);
            Map orginfo = orgauth.getProperties();

            if(orgauth != null){
                arresult.setCode(SUCESS_CODE);
                arresult.setMsg(SUCESS_MSG);
                arresult.setData(orginfo);
            }else{
                arresult.setCode(NODATA_CODE);
                arresult.setMsg(NODATA_MSG);
                arresult.setData(null);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            arresult.setCode(FAIL_CODE);
            arresult.setMsg(FAIL_MSG);
            arresult.setData(null);
        }
        return arresult;
    }

    @ApiOperation(value = "根据区域ID查询区域信息")
    @RequestMapping(value = "/district", method = RequestMethod.POST)
    @ApiImplicitParam(name = "districtId", value = "区域ID", required = true, paramType = "query", dataType = "string")
    public AuthResourceResult district(String districtId) {

        AuthResourceResult arresult = new AuthResourceResult();
        try {
            IBOSecDistrictValue value = OrgmodelClient.getDistrictById(districtId);
            Map districtinfo = value.getProperties();

            if(value != null){
                arresult.setCode(SUCESS_CODE);
                arresult.setMsg(SUCESS_MSG);
                arresult.setData(districtinfo);
            }else{
                arresult.setCode(NODATA_CODE);
                arresult.setMsg(NODATA_MSG);
                arresult.setData(null);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            arresult.setCode(FAIL_CODE);
            arresult.setMsg(FAIL_MSG);
            arresult.setData(null);
        }
        return arresult;
    }
    
    
    @ApiOperation(value = "获取用户信息和权限")
    @ApiImplicitParam(name = "operatorId", value = "操作员ID(数字)", required = true, paramType = "query", dataType = "string")
    @RequestMapping(value = "/userinfo", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
    public AuthResourceResult userinfo(long operatorId) {

        long startTime=System.currentTimeMillis();
        AuthResourceResult arresult = new AuthResourceResult();
        try {
            Map<String,Object> map = new HashMap<>();
            //根据员工ID获取用户信息
            
            IBOSecOperatorValue operatorvalue =  OrgmodelClient.getOperatorById(operatorId);
            Map opevalue = operatorvalue.getProperties();
            long staffId = Long.valueOf(String.valueOf(opevalue.get("STAFF_ID"))).longValue();
            IBOSecStaffValue stafvalue = (BOSecStaffBean)OrgmodelClient.getStaffById(staffId);
            Map staffvalue = stafvalue.getProperties();
            //通过组织ID获取组织信息
            long orgId = Long.valueOf(String.valueOf(staffvalue.get("ORGANIZE_ID"))).longValue();
            IBOSecOrganizeValue orgauth = OrgmodelClient.getOrganizeByOrgId (orgId);
            Map orginfo = orgauth.getProperties();
            //根据区域ID查询区域信息
            IBOSecDistrictValue disvalue = OrgmodelClient.getDistrictById(orginfo.get("DISTRICT_ID").toString());
            Map districtinfo = disvalue.getProperties();
            
            
            map.put("staffvalue", staffvalue);
            map.put("orginfo", orginfo);
            map.put("districtinfo", districtinfo);
            
            if(stafvalue != null){
                arresult.setCode(SUCESS_CODE);
                arresult.setMsg(SUCESS_MSG);
                arresult.setData(map);
            }else{
                arresult.setCode(NODATA_CODE);
                arresult.setMsg(NODATA_MSG);
                arresult.setData(null);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            arresult.setCode(FAIL_CODE);
            arresult.setMsg(FAIL_MSG);
            arresult.setData(null);
        }
        long endTime=System.currentTimeMillis();
        System.out.println("程序运行时间： "+(endTime-startTime)/1000+"s");
        return arresult;
    }
}
