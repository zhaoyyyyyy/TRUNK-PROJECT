package com.asiainfo.biapp.si.coc.jauth.api;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.biapp.si.coc.jauth.frame.page.JQGridPage;
import com.asiainfo.biapp.si.coc.jauth.security.auth.JwtAuthenticationToken;
import com.asiainfo.biapp.si.coc.jauth.security.auth.jwt.extractor.TokenExtractor;
import com.asiainfo.biapp.si.coc.jauth.security.auth.jwt.verifier.TokenVerifier;
import com.asiainfo.biapp.si.coc.jauth.security.config.JwtSettings;
import com.asiainfo.biapp.si.coc.jauth.security.config.WebSecurityConfig;
import com.asiainfo.biapp.si.coc.jauth.security.exceptions.InvalidJwtToken;
import com.asiainfo.biapp.si.coc.jauth.security.model.UserContext;
import com.asiainfo.biapp.si.coc.jauth.security.model.token.JwtToken;
import com.asiainfo.biapp.si.coc.jauth.security.model.token.JwtTokenFactory;
import com.asiainfo.biapp.si.coc.jauth.security.model.token.RawAccessJwtToken;
import com.asiainfo.biapp.si.coc.jauth.security.model.token.RefreshToken;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Group;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Organization;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Resource;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Role;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.User;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.ResourceService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.UserService;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.vo.ResourceVo;

/**
 * End-point for retrieving logged-in user details.
 * 
 * @author vladimir.stankovic
 *
 * Aug 4, 2016
 */
@Api(value = "001-认证与授权接口",description="登录授权")
@RequestMapping("api/auth")
@RestController
public class JAuthApi {
	@Autowired private JwtTokenFactory tokenFactory;
	@Autowired private JwtSettings jwtSettings;
	@Autowired private UserService userService;
	@Autowired private ResourceService resourceService;
	@Autowired private TokenVerifier tokenVerifier;
	@Autowired @Qualifier("jwtHeaderTokenExtractor") private TokenExtractor tokenExtractor;
	
	
    @RequestMapping(value="/me", method=RequestMethod.GET)
    public @ResponseBody UserContext get(JwtAuthenticationToken token) {
        return (UserContext) token.getPrincipal();
    }
    
    
    /**
     * 
     * Description: 
     *
     * @param token
     * @param type
     * @return
     */
    @ApiOperation(value="通过token 拿到数据权限")
    @ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "用户秘钥", required = true, paramType = "query" ,dataType = "string" ),
		@ApiImplicitParam(name = "type", value = "类型,来源于数据字典的【组织类型】例如：实体组织,虚拟组织", required = false, paramType = "query" ,dataType = "string")
	})
    @RequestMapping(value="/permission/data", method=RequestMethod.GET)
    public @ResponseBody List<Organization> dataPermission(String token,String type) {
    	String userName = getUserNameByToken(token);
        
        //得到用户的组织权限
        List<Organization> list = new ArrayList<Organization>();
        User user = userService.getUserByName(userName);
        if(user != null && user.getGroupSet() != null){
        	for(Group group : user.getGroupSet()){
        	    for(Organization org : group.getOrganizationSet()){
        	        if(!list.contains(org)){
        	            list.add(org);
        	        }
        	    }
        	}
        }
        return list;
    }
    
    /**
     * 
     * Description: 
     *
     * @param token
     * @param type
     * @return
     */
    @ApiOperation(value="通过token 拿到资源权限")
    @ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "用户秘钥", required = true, paramType = "query" ,dataType = "string" ),
		@ApiImplicitParam(name = "type", value = "类型,来源于数据字典的【资源类型】例如：菜单,按钮,页面元素", required = false, paramType = "query" ,dataType = "string")
	})
    @RequestMapping(value="/permission/resource", method=RequestMethod.GET)
    public @ResponseBody List<Resource> resourcePermission(String token,String type) {
        
    	String userName = getUserNameByToken(token);
        
        //得到用户的组织权限
        User user = userService.getUserByName(userName);
        
        JQGridPage<Resource> page = new JQGridPage<Resource>();
        
        if(user != null && user.getRoleSet() != null){
        	ResourceVo resourceVo = new ResourceVo();
        	resourceVo.setRoleSet(user.getRoleSet());
        	page.setPageSize(100);
        	resourceService.findResourceList(page, resourceVo);
        }
        
        return page.getData();
    }
    
    /**
     * 
     * Description: 
     *
     * @param loginRequest
     * @return
     */
    @ApiOperation(value="通过用户名密码得到token", notes="通过用户名密码得到token,{\"username\":\"admin\",\"password\": \"test1234\"}")
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public @ResponseBody JwtToken login(String loginRequest) {
        return null;
    }
    
    /**
     * 
     * Description: 
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping(value="/token", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM));
        
        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey());
        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtToken();
        }

        String subject = refreshToken.getSubject();
        User user = userService.getUserByName(subject);

        if (user.getRoleSet() == null) throw new InsufficientAuthenticationException("User has no roles assigned");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role r:user.getRoleSet()){
            authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
        }
        

        UserContext userContext = UserContext.create(user.getId(),user.getUserName(), authorities);

        return tokenFactory.createAccessJwtToken(userContext);
    }
    
    
    
    /**
     * 通过token得到用户名
     * Description: 
     *
     * @param token
     * @return
     */
    private String getUserNameByToken(String token){
    	//通过token拿到
        String tokenPayload = tokenExtractor.extract(token);
        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        Jws<Claims> jwsClaims =  rawToken.parseClaims(jwtSettings.getTokenSigningKey());
        String userName = jwsClaims.getBody().getSubject();
        return userName;
    }
}
