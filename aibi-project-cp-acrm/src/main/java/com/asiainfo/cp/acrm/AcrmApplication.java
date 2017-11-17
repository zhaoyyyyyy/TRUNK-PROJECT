package com.asiainfo.cp.acrm;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AcrmApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(AcrmApplication.class).web(true).run(args);
	}

}