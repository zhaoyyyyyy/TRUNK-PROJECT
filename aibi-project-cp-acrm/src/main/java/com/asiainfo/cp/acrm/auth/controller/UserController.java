package com.asiainfo.cp.acrm.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.cp.acrm.auth.model.User;
import com.asiainfo.cp.acrm.auth.service.IUserService;
import com.asiainfo.cp.acrm.auth.utils.TokenModel;
import com.asiainfo.cp.acrm.base.controller.BaseController;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.utils.WebResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Api(value = "用户权限相关接口")
@RequestMapping("api/user")
@RestController
public class UserController extends BaseController<User>{
	
	@Autowired
	private IUserService userService;
	/**
	 * 
	 * @param userName
	 * @param password
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "string") 
	})
	@RequestMapping(value="/login", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
	public WebResult<TokenModel> login(String username,String password){
		WebResult<TokenModel> webResult = new WebResult<TokenModel>();
		
		TokenModel token = null;
		try {
			token = userService.getTokenByUsernamePassword(username, password);
		} catch (BaseException e) {
			return webResult.fail(e);
		}
		return webResult.success("登录成功",token );
	}
}
