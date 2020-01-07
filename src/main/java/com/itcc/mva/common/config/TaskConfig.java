package com.itcc.mva.common.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @描述: 多线程执行定时任务
 * @日期 2020年1月7日
 * @author whoami
 */
@Configuration
public class TaskConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * @描述: 所有的定时任务都放在一个线程池中,定时任务启动时使用不同的线程
     * @return
     * @日期 2020年1月7日
     */
    @Bean
    public TaskScheduler taskScheduler() {
        logger.info("loading the config of taskSchedulerConfig");
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // 设置scheduler执行线程为3个
        scheduler.setPoolSize(4);
        return scheduler;
    }
}