package com.asiainfo.cp.acrm.auth.service;

import com.asiainfo.cp.acrm.auth.model.User;
import com.asiainfo.cp.acrm.auth.utils.TokenModel;
import com.asiainfo.cp.acrm.base.exception.BaseException;

/**
 * 用户业务处理层
 * @author zhougz3
 */
public interface IUserService {
	
	/**
	 * 登录方法，通过用户名密码拿到token
	 * @param userName 用户名
	 * @param password 密码
	 * @return TokenModel   秘钥  包括当前秘钥跟刷新秘钥
	 */
	public TokenModel getTokenByUsernamePassword(String userName,String password) throws BaseException;
	
	/**
	 * 通过用户身份token(秘钥)拿到用户信息
	 * @return
	 */
	public User getUserByToken(String token) throws BaseException;

}
