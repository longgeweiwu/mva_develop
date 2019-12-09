package com.itcc.mva.job;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 解析JSON 任务JOB
 * @author whoami
 */
@Component
public class AsrJsonParseJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "* 0/2 * * * ?")
    @SchedulerLock(name = "AsrJsonParseJob", lockAtMostFor = "1m", lockAtLeastFor ="1m")
    public void run()
    {

    }
}
