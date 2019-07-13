package com.snail.child.config;


import com.snail.child.res.Url;
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
@ComponentScan(basePackages = {"com.snail.child.controller"})
public class SwaggerConfig {

    @Bean
    public Docket customDocket() {
        Contact contact = new Contact("流浪宝贝", Url.baseUrl + Url.webMapping + "/index.html", "liulangbaobei@yeah.net");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("流浪宝贝接口测试")
                .contact(contact)
                .build();
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo);
    }
}
