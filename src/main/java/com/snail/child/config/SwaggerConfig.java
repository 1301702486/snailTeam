package com.example.huigu.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableWebMvc
@ComponentScan(basePackages = {"com.example.huigu.controller"})
public class SwaggerConfig {

    @Bean
    public Docket customDocket() {
        Contact contact = new Contact("zhengz", "zhengflf@163.com", "zhengflf@163.com");
        ApiInfo apiInfo =  new ApiInfoBuilder()
                .title("回顾前两周所学内容")
                .description("细化与了解")
                .contact(contact)
                .version("1.0")
                .build();
          return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo);
    }
}
