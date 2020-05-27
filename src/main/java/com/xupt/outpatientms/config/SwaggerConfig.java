package com.xupt.outpatientms.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xupt.outpatientms.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * http://progject-name/swagger-ui.html
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("OutpatientMS API Doc")
                .description("医疗门诊管理系统接口文档及测试页面")
                .version("1.0")
                .build();
    }

}
