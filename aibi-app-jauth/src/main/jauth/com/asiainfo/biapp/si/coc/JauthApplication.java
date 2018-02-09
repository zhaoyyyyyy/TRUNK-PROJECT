package com.asiainfo.biapp.si.coc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import de.codecentric.boot.admin.config.EnableAdminServer;

/**
 * Sample application for demonstrating security with JWT Tokens
 * 
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
@SpringBootApplication
@EnableEurekaServer  //服务发现
@EnableAdminServer
@EnableConfigurationProperties
public class JauthApplication {
	
//    @Override  
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {  
//    		builder.sources(this.getClass());
//        return super.configure(builder);  
//    } 
    
	public static void main(String[] args) {
		SpringApplication.run(JauthApplication.class, args);
	}
	
	
	
}
