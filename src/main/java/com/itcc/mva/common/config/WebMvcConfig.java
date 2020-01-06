package com.itcc.mva.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @author whoami
 */
@Configuration
public class  WebMvcConfig implements WebMvcConfigurer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolverConfig() {
        logger.info("loading the config of multipartResolverConfig");
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
//        resolver.setResolveLazily(true);
//        resolver.setMaxInMemorySize(40960);
//上传文件大小 100M 100*1024*1024
        resolver.setMaxUploadSize(100 * 1024 * 1024);
        return resolver;
    }
}