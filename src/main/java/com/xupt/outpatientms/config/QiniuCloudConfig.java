package com.xupt.outpatientms.config;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import java.io.File;

@Configuration
@ConditionalOnClass({Servlet.class, StandardServletMultipartResolver.class, MultipartConfigElement.class})
@ConditionalOnProperty(prefix = "spring.http.multipart", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(MultipartProperties.class)
public class QiniuCloudConfig {

    private static final Logger logger = LoggerFactory.getLogger(QiniuCloudConfig.class);

    private final MultipartProperties multipartProperties;

    private static final String ACCESS_KEY = "uyQDTdeyrWBw3T5IXGbk1sImC5ysaNgp1aLcCyF4";
    private static final String SECRET_KEY = "aQZwCPKctuP7MG7XoKbzM_vfMC-7oJvr2mgnhKW5";

    public static final String baseUploadUrl =
            System.getProperty("os.name").toLowerCase().contains("windows")
                                            ? "D:\\temp":"/home/kafm/.temp";

    public QiniuCloudConfig(MultipartProperties multipartProperties) {
        this.multipartProperties = multipartProperties;
    }

    /**
     * 上传配置
     */
    @Bean
    @ConditionalOnMissingBean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        File dir = new File(baseUploadUrl);
        if(!dir.exists() && !dir.mkdir()){
            logger.info("failed locate baseUploadUrl");
        }else logger.info("qiniuCloud locate " + baseUploadUrl);
        factory.setLocation(baseUploadUrl);
        return factory.createMultipartConfig();
    }

    /**
     * 注册解析器
     */
    @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
    @ConditionalOnMissingBean(MultipartResolver.class)
    public StandardServletMultipartResolver multipartResolver() {
        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
        multipartResolver.setResolveLazily(this.multipartProperties.isResolveLazily());
        return multipartResolver;
    }

    /**
     * 华南机房
     */
    @Bean
    public com.qiniu.storage.Configuration qiniuConfig() {
        return new com.qiniu.storage.Configuration(Zone.autoZone());
    }

    /**
     * 构建一个七牛上传工具实例
     */
    @Bean
    public UploadManager uploadManager() {
        return new UploadManager(qiniuConfig());
    }


    /**
     * 认证信息实例
     */
    @Bean
    public Auth auth() {
        return Auth.create(ACCESS_KEY, SECRET_KEY);
    }

    /**
     * 构建七牛空间管理实例
     */
    @Bean
    public BucketManager bucketManager() {
        return new BucketManager(auth(), qiniuConfig());
    }

    /**
     * 配置gson为json解析工具
     */
    @Bean
    public Gson gson() {
        return new Gson();
    }
}
