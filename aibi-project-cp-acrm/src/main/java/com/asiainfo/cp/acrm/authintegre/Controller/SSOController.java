package com.asiainfo.cp.acrm.authintegre.Controller;

import com.ai.sso.external.IPopedom;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.cp.acrm.authintegre.vo.CnPost;
import com.asiainfo.cp.acrm.base.controller.BaseController;
import com.asiainfo.cp.acrm.authintegre.vo.IsLogin;
import com.asiainfo.cp.acrm.authintegre.vo.UserInfo;
import com.asiainfo.cp.acrm.base.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.ArrayList;
import static com.ai.sso.util.SessionClient.getSessionId;
import static com.ai.sso.util.SessionClient.getSign;
import static com.ai.sso.util.SessionClient.getUserInfo;

@Api(value = "03 单点登录开放API",description="包装SSO单点接口并对外提供rest服务")
@RequestMapping("api/sso")
@RestController
public class SSOController extends BaseController {

    private IPopedom ipopedom = null;
    private ArrayList arrPathList = new ArrayList();

    @ApiOperation(value = "验证是否已登录")
    @RequestMapping(value = "/islogin", method = RequestMethod.GET)
    public IsLogin islogin(HttpServletRequest request, HttpServletResponse response) {

        boolean loginflg = false;
        IsLogin islogin = new IsLogin();
        try {
            Class var8 = Class.forName("com.ai.sso.external.DefaultPopedomImpl");
            this.ipopedom = (IPopedom)var8.newInstance();
            loginflg = this.ipopedom.setFirstPopedom(request, response,arrPathList,"","");
            this.ipopedom = null;
            response.reset();
        } catch (Exception e) {
            e.printStackTrace();;
        }
        islogin.setIslogin(loginflg);
        return islogin;
    }

    @ApiOperation(value = "获取登录用户信息")
    @RequestMapping(value = "/userinfo", method = RequestMethod.GET)
    public UserInfo userinfo(HttpServletRequest request) {

        UserInfo userinfo = new UserInfo();
        try {
            String sessionid = getSessionId(request);
            String cnpost = getUserInfo(request);
            String sign = getSign(request);

            if(!StringUtil.isEmpty(sessionid)){
                String decodecnpost = URLDecoder.decode(cnpost, "utf-8");
                JSONObject cnpostjson = JSONObject.parseObject(decodecnpost);
                Boolean isflg = (Boolean) cnpostjson.get("success");
                if(isflg){
                    CnPost cnpostbean = new CnPost();
                    cnpostbean.setId((String)cnpostjson.get("id"));
                    cnpostbean.setUsername((String)cnpostjson.get("username"));
                    cnpostbean.setOrgId((String)cnpostjson.get("orgId"));
                    cnpostbean.setOrgName((String)cnpostjson.get("orgName"));
                    cnpostbean.setDistrictId((String)cnpostjson.get("districtId"));
                    cnpostbean.setAvatar((String)cnpostjson.get("avatar"));
                    userinfo.setCnpost(cnpostbean);
                }
            }
            userinfo.setSession_id(sessionid);
            userinfo.setSign(sign);
        } catch (Exception e) {
            e.getMessage();
        }
        return userinfo;
    }


}
