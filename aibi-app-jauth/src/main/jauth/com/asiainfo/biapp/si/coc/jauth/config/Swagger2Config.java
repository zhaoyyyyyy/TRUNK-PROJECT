package com.asiainfo.biapp.si.coc.jauth.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.asiainfo.biapp.si.coc"))
//                .paths(PathSelectors.any())
//                .build();
//    }

    @Bean  
    public Docket api(){  
        ParameterBuilder tokenPar = new ParameterBuilder();  
        List<Parameter> pars = new ArrayList<Parameter>();  
        tokenPar.name("X-Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();  
        pars.add(tokenPar.build());  
        return new Docket(DocumentationType.SWAGGER_2)  
            .select()  
            //.apis(RequestHandlerSelectors.basePackage("com.asiainfo.biapp.si.coc"))
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.any())
            .build()  
            .globalOperationParameters(pars)  
            .apiInfo(apiInfo());  
    }  
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("COC RESTful APIs")
                .description("coc product copyright")
                //.termsOfServiceUrl("http://blog.didispace.com/")
                .contact("aibi")
                .version("0.1")
                .build();
    }

}
