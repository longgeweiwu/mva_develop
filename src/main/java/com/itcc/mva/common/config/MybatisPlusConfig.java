package com.itcc.mva.common.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;
/**
 * @author whoami
 */
@Configuration
@EnableTransactionManagement
@ComponentScan
//@MapperScan("com.itcc.mva.mapper*")
public class MybatisPlusConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
    /**
     * 打印 sql
     */
    @Bean
    @Profile({"test"})// 设置 test 环境开启
    public PerformanceInterceptor performanceInterceptor() {
        logger.info("loading the config of MybatisPlusConfig");
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        //格式化sql语句
        Properties properties = new Properties();
        properties.setProperty("format", "true");
        performanceInterceptor.setProperties(properties);
        return performanceInterceptor;
    }
}
