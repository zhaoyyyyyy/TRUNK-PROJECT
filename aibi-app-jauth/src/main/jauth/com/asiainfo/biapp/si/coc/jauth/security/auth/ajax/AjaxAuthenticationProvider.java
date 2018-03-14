package com.asiainfo.biapp.si.coc.jauth.security.auth.ajax;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.asiainfo.biapp.si.coc.jauth.security.model.UserContext;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.Role;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.entity.User;
import com.asiainfo.biapp.si.coc.jauth.sysmgr.service.UserService;

/**
 * 
 * @author zhougz3
 *   调用user接口
 *
 * 2017-07-29
 */
@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;

    @Value("${demo.security.jwt.tokenSigningKey}")  
    private String tokenSigningKey; 
    
    
    @Autowired
    public AjaxAuthenticationProvider(final UserService userService, final BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        
        User user = new User();;
        
        if(tokenSigningKey.equals(password)){   //单点登录部分
        	Role role = new Role();
        	user.setId(username);
        	user.setRealName(username);
        	user.setUserName(username);
        	role.setRoleName("普通用户");
        	Set<Role> set = new HashSet<Role>();
        	set.add(role);
        	user.setRoleSet(set);
        }else{    //用户名密码登录部分
        	user = userService.getUserByName(username);
        	if (!encoder.matches(password, user.getPassword())  ) {
        		throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        	}
        	if(user.getStatus()==2){
        	    throw new BadCredentialsException("This account does not take effect");
        	}
        	if (user.getRoleSet() == null) {
        		throw new InsufficientAuthenticationException("User has no roles assigned");
        	}
        }
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role r:user.getRoleSet()){
            authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
        }
        
        UserContext userContext = UserContext.create(user.getId(),user.getUserName(), authorities);
        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
