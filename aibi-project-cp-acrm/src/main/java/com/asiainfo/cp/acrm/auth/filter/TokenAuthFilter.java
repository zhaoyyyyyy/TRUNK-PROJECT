package com.asiainfo.cp.acrm.auth.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import com.asiainfo.cp.acrm.auth.model.User;
import com.asiainfo.cp.acrm.auth.utils.AuthUtils;
import com.asiainfo.cp.acrm.base.exception.BaseException;
import com.asiainfo.cp.acrm.base.exception.UserAuthException;
import com.asiainfo.cp.acrm.base.utils.ServletUtil;

@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/api/*", filterName = "tokenAuthFilter")
public class TokenAuthFilter implements Filter {
	
    Log log = LogFactory.getLog(TokenAuthFilter.class);

    private final String ignoreUrls = "/api/user/login;/api/token;/api/sso/islogin;/api/sso/userinfo";
    private String[] excludedPageArray;
    /**
     * 忽略地址
     */
   
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	  if (null != ignoreUrls && ignoreUrls.length()!=0) { 
              excludedPageArray = ignoreUrls.split(String.valueOf(';'));     
          }    
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest)servletRequest;
    	HttpServletResponse response = (HttpServletResponse)servletResponse;
    	
    	//response.setHeader("Access-Control-Allow-Origin", "*");  
    	//response.setHeader("Access-Control-Allow-Origin", "http://crm.chinapost.com:8444");  
    	response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));  
    	response.setHeader("Access-Control-Allow-Credentials", "true");
    	
    	response.setHeader("Access-Control-Allow-Origin", "http://crm.chinapost.com:8444");  
    	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
    	response.setHeader("Access-Control-Max-Age", "3600");  
    	response.setHeader("Access-Control-Allow-Headers", "");
    	response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Content-Type,X-Authorization,Access-Control-Allow-Origin");

    	//1.判断是否在忽略范围内
    	boolean isExcludedPage = false;  
    	for (String page : excludedPageArray) {
             // 判断当前URL是否与例外页面相同
             if(request.getServletPath().contains(page)){ // 从第2个字符开始取（把前面的/去掉）
                 isExcludedPage = true;     
                 break;     
             }     
        }
    	
    	//2.如果不在忽略范围之内
    	if(isExcludedPage){
    		filterChain.doFilter(servletRequest,servletResponse);
    	}else{
    		//1.请求JAUTH 判断IP地址是否在白名单范围内 TODO
    		
    		//2.拿到用户信息，如果没有token或者无效则报错
    		User user = null;
    		try {
    			user = AuthUtils.getLoginUser(request);
    		} catch (BaseException e) {
    			ServletUtil.responseMsg(response, e);//直接输出异常
    		}
    		if(user != null){
    			//3.判断用户是否能够访问该地址接口权限 TODO
    			filterChain.doFilter(servletRequest,servletResponse);
    		}else{
    			ServletUtil.responseMsg(response, new UserAuthException("无效的token"));//直接输出异常
    		}
    	}
        
    }

    @Override
    public void destroy() {

    }
}