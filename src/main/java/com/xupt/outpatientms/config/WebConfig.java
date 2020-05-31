package com.xupt.outpatientms.config;

import com.xupt.outpatientms.interceptor.AuthorityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorityInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/user/register","/user/login","/user/checkUserTelUnique")
                .excludePathPatterns("/swagger-ui.html","/swagger-resources/**","/webjars/**","/swagger-ui.html/**","/v2/**");
    }


}
