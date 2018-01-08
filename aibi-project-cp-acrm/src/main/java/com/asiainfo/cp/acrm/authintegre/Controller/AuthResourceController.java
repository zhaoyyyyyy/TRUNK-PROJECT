package com.asiainfo.cp.acrm.authintegre.Controller;

import com.ai.secframe.client.OrgmodelClient;
import com.ai.secframe.client.SecframeClient;
import com.ai.secframe.orgmodel.bo.BOSecStaffBean;
import com.ai.secframe.orgmodel.ivalues.IBOSecDistrictValue;
import com.ai.secframe.orgmodel.ivalues.IBOSecOrganizeValue;
import com.ai.secframe.orgmodel.ivalues.IBOSecStaffValue;
import com.asiainfo.cp.acrm.authintegre.vo.AuthResourceResult;
import com.asiainfo.cp.acrm.authintegre.vo.ReqStaff;
import com.asiainfo.cp.acrm.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
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


    @ApiOperation(value = "根据用户ID获取用户信息")
    @RequestMapping(value = "/userdata", method = RequestMethod.POST)
    public AuthResourceResult userdata(HttpServletRequest request, @RequestBody ReqStaff staff) {

        AuthResourceResult arresult = new AuthResourceResult();
        try {
            IBOSecStaffValue value = (BOSecStaffBean)OrgmodelClient.getStaffById(staff.getStaffid());
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

    @ApiOperation(value = "根据操作员ID，实体ID与行为ID查询操作员是否具有对该实体的操作权限")
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

    @ApiOperation(value = "获取用户的数据权限是通过用户所归属的组织的行政区域和业务类型的属性来获取")
    @RequestMapping(value = "/userauth", method = RequestMethod.POST)
    public AuthResourceResult userauth(HttpServletRequest request, @RequestBody ReqStaff staff) {

        AuthResourceResult arresult = new AuthResourceResult();
        try {
            IBOSecOrganizeValue orgauth = OrgmodelClient.getOrganizeByOrgId (staff.getOrgId());
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

    @ApiOperation(value = "根据districtId查询区域信息")
    @RequestMapping(value = "/districtinfo", method = RequestMethod.POST)
    public AuthResourceResult districtinfo(HttpServletRequest request, @RequestBody ReqStaff staff) {

        AuthResourceResult arresult = new AuthResourceResult();
        try {
            IBOSecDistrictValue value = OrgmodelClient.getDistrictById(staff.getDistrictId());
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
}
