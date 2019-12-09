package com.itcc.mva.common.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
//defaultLockAtMostFor 指定在执行节点结束时应保留锁的默认时间使用ISO8601 Duration格式
//作用就是在被加锁的节点挂了时，无法释放锁，造成其他节点无法进行下一任务
//这里默认30s
//关于ISO8601 Duration格式用的不到，具体可上网查询下相关资料，应该就是一套规范，规定一些时间表达方式
@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "30s")
public  class SchedLockConfig {

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }

}