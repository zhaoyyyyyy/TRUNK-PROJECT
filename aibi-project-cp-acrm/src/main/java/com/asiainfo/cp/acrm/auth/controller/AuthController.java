package com.asiainfo.cp.acrm.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.cp.acrm.auth.model.TokenRequestModel;
import com.asiainfo.cp.acrm.auth.model.User;
import com.asiainfo.cp.acrm.auth.service.IUserService;
import com.asiainfo.cp.acrm.auth.utils.TokenModel;
import com.asiainfo.cp.acrm.base.controller.BaseController;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.utils.WebResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "01 认证及权限",description="应用认证及授权相关接口")
@RequestMapping("api")
@RestController
public class AuthController extends BaseController<User> {

	@Autowired
	private IUserService userService;

	/**
	 * 认证中心认证接口
	 * @param appkey
	 * @return
	 */
	@ApiOperation(value="通过颁发的APPKEY拿到具有时效性的TOKEN")
	@RequestMapping(value = "/token", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public WebResult<TokenModel> getToken(
			@ApiParam(value = "认证中心分配的Appkey=EA735FB0554CFB9E6890D4362D7B446F", required = true) @RequestBody TokenRequestModel appkey) {
		WebResult<TokenModel> webResult = new WebResult<TokenModel>();
		TokenModel token = null;
		try {
			token = userService.getTokenByUsernamePassword(appkey.getAppkey(), "test1234");
		} catch (BaseException e) {
			return webResult.fail(e);
		}
		return webResult.success("登录成功", token);
	}
}
