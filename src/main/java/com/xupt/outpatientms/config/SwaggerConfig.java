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
                .description("\n所有ErrCode定义如下："+
                "0:操作成功\t1:操作失败\t2:未登录\t3:参数错误\t"+
                "详细信息保存在errMsg中")
                .version("1.0")
                .build();
    }

}
