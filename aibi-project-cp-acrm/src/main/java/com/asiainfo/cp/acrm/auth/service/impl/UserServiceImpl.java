package com.asiainfo.cp.acrm.auth.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.asiainfo.cp.acrm.auth.model.Organization;
import com.asiainfo.cp.acrm.auth.model.Resource;
import com.asiainfo.cp.acrm.auth.model.User;
import com.asiainfo.cp.acrm.auth.service.IUserService;
import com.asiainfo.cp.acrm.auth.utils.TokenModel;
import com.asiainfo.cp.acrm.base.dao.BaseDao;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.exception.ParamRequiredException;
import com.asiainfo.cp.acrm.base.exception.UserAuthException;
import com.asiainfo.cp.acrm.base.service.impl.BaseServiceImpl;
import com.asiainfo.cp.acrm.base.utils.HttpUtil;
import com.asiainfo.cp.acrm.base.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 
 * Title : 用户相关业务实现层
 * <p/>
 * Description : 
 * <p/>
 * CopyRight : CopyRight (c) 2017
 * <p/>
 * Company : 北京亚信智慧数据科技有限公司
 * <p/>
 * JDK Version Used : JDK 1.8 +
 * <p/>
 * Modification History	:
 * <p/>
 * <pre>NO.    Date    Modified By    Why & What is modified</pre>
 * <pre>1    2017年11月7日    Administrator        Created</pre>
 * <p/>
 *
 * @author  zhougz3
 * @version 1.0.0.2017年11月7日
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User, String> implements IUserService{

	@Value("${jauth-url}")  
    private String jauthUrl; 

	@Override
	protected BaseDao<User, String> getBaseDao() {
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see com.asiainfo.biapp.si.loc.auth.service.IUserService#getTokenByUsernamePassword(java.lang.String, java.lang.String)
	 */
	@Override
	public TokenModel getTokenByUsernamePassword(String username, String password) throws BaseException{
		if(StringUtil.isEmpty(username)){
			throw new ParamRequiredException("用户名不能为空");
		}
		if(StringUtil.isEmpty(password)){
			throw new ParamRequiredException("密码不能为空");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("username", username);
		map.put("password", password);
		
		try{
			String	tokenStr = HttpUtil.sendPost(jauthUrl+"/api/auth/login", map);
			JSONObject jsObject = JSONObject.fromObject(tokenStr);
			TokenModel tokenModel = new TokenModel();
			tokenModel.setToken(jsObject.getString("token"));
			tokenModel.setRefreshToken(jsObject.getString("refreshToken"));
			return tokenModel;
		}catch(Exception e){
			throw new UserAuthException("错误的用户名/密码");
			
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see com.asiainfo.biapp.si.loc.auth.service.IUserService#getUserByToken(java.lang.String)
	 */
	@Override
	public User getUserByToken(String token) throws BaseException{
		
		String username = null;
		String userId = null;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("token", token);
		
		//拿到用户名
		try{
			
			String tokenStr = HttpUtil.sendGet(jauthUrl+"/api/auth/me", params);
			JSONObject jsObject = JSONObject.fromObject(tokenStr);
			username = jsObject.getString("username");
			userId = jsObject.getString("userId");
		}catch(Exception e){
			throw new UserAuthException("无效的token");
		}
		User user = new User();
		user.setUserName(username);
		user.setUserId(userId);
		
		//拿到数据权限
		try{
			List<Organization> organizationPrivaliege = new ArrayList<Organization>();
			
			
			String dataJson = HttpUtil.sendGet(jauthUrl+"/api/auth/permission/data", params);
			
			//TODO 把data Json 转成 orglist
			user.setOrganizationPrivaliege(organizationPrivaliege);
			
		}catch(Exception e){
			throw new UserAuthException("获取用户数据权限失败",e);
		}
		
		//拿到资源权限
		try{
			List<Resource> resourcePrivaliege = new ArrayList<Resource>();
			
			String resourceJson = HttpUtil.sendGet(jauthUrl+"/api/auth/permission/resource", params);
			
			//TODO 把data Json 转成 orglist
			user.setResourcePrivaliege(resourcePrivaliege);
		
		}catch(Exception e){
			throw new UserAuthException("获取用户资源权限失败",e);
		}
		
		return user;
	}

	
	
}
