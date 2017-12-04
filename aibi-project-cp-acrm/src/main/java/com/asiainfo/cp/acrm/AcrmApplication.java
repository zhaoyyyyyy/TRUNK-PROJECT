package com.asiainfo.cp.acrm;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableAutoConfiguration
public class AcrmApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		new SpringApplicationBuilder(AcrmApplication.class).web(true).run(args);
	}
	
    @Override  
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {  
    		builder.sources(this.getClass());
        return super.configure(builder);  
    }  
}