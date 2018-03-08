package com.asiainfo.cp.acrm;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@EnableCaching
public class AcrmApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		new SpringApplicationBuilder(AcrmApplication.class).web(true).run(args);
	}
	
    @Override  
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {  
    		builder.sources(this.getClass());
        return super.configure(builder);  
    }  

	/**
	 * 配置过滤器
	 * @return
	 */
//	@Bean
//	public FilterRegistrationBean someFilterRegistration() {
//		FilterRegistrationBean registration = new FilterRegistrationBean();
//		registration.addUrlPatterns("/api/sso/*");
//		registration.addInitParameter("impl-classname", "com.ai.sso.external.DefaultPopedomImpl");
//		registration.addInitParameter("ALLOWPATH","gif;jpg;jpeg;png");
//		registration.setEnabled(true);
//		registration.setName("ssoFilter");
//		registration.setFilter(ssoFilter());
//		registration.setOrder(Integer.MIN_VALUE);//控制filter执行顺序,值越小越靠前执行
//		return registration;
//	}

//	/**
//	 * 创建一个bean
//	 * @return
//	 */
//	@Bean(name = "ssoFilter")
//	public Test2Filter ssoFilter() {
//		return new Test2Filter();
//	}
	
}