package com.asiainfo.cp.acrm.authintegre.filter;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.sso.external.IPopedom;
import com.ai.sso.util.Resource;
import com.asiainfo.cp.acrm.base.utils.LogUtil;

public class Test2Filter implements Filter{
    private ArrayList arrPathList = new ArrayList();
    private String strImplClassName = "";
    public static String isLog = "false";
    private static String crmSSOServerName = Resource.getStrValue("MAIN_PAGE");
    private static String return_info = Resource.getStrValue("RETURN_TAG");
    private IPopedom ipopedom = null;

    public Test2Filter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.strImplClassName = filterConfig.getInitParameter("impl-classname");
        isLog = filterConfig.getInitParameter("ISLOG");
        if(isLog == null) {
            isLog = "false";
        }

        String allPath = filterConfig.getInitParameter("ALLOWPATH");
        if(allPath == null) {
            allPath = "";
        }

        String[] tempArr = allPath.split(";");

        for(int e = 0; e < tempArr.length; ++e) {
            this.arrPathList.add(tempArr[e]);
        }

        try {
            Class var8 = Class.forName(this.strImplClassName);
            this.ipopedom = (IPopedom)var8.newInstance();
        } catch (ClassNotFoundException var5) {
            var5.printStackTrace();
        } catch (InstantiationException var6) {
            var6.printStackTrace();
        } catch (IllegalAccessException var7) {
            var7.printStackTrace();
        }

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;

        try {
            boolean e = this.ipopedom.setFirstPopedom(httpRequest, httpResponse, this.arrPathList, crmSSOServerName, return_info);
            if(e) {
                filterChain.doFilter(request, response);
            }else {
            		this.responseMsg(httpResponse,return_info);
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }
    
	private void responseMsg(HttpServletResponse response,String msg) {
		//没有权限      、将实体对象转换为JSON Object转换
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			LogUtil.error("接口响应失败", e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

    public void destroy() {
        this.arrPathList = new ArrayList();
        this.ipopedom = null;
    }

    public static String getCRMSSOServerName() {
        return crmSSOServerName;
    }
}
