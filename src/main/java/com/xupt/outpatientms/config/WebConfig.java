package com.xupt.outpatientms.config;

import com.xupt.outpatientms.interceptor.AuthorityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthorityInterceptor authorityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorityInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/pictureUpload")
                .excludePathPatterns("/smsmsgs/sendCode/*")
                .excludePathPatterns("/user/register","/user/login","/user/checkUserTelUnique")
                .excludePathPatterns("/swagger-ui.html","/swagger-resources/**","/webjars/**","/swagger-ui.html/**","/v2/**");
    }


}
