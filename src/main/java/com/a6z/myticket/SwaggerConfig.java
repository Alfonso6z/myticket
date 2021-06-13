package com.a6z.myticket;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configura el Swagger
 * @author Alfonso6z
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())//.basePackage("com.a6z.myticket"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());

    }
    
    // TODO: Llenar apiInfo con la información correcta
    private ApiInfo apiInfo(){
        return new ApiInfo(
            "Api MiTicket",
            "Api para generar tickets",
            "Version 1.0.0",
            "Terms of service",
            new Contact("miTicket","www.miticket.com","miticket@gmail.com"),
            "License of API","API license URL", Collections.emptyList()
        );
    }
}
