package com.itcc.mva.job;

import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TimeTaskJob {

    private static final int lockTime = 1000;

    @Scheduled(cron = "0/2 * * * * ?")
    @SchedulerLock(name = "TaskScheduler_scheduledTask", lockAtMostFor = lockTime, lockAtLeastFor = lockTime)
    public void run()
    {
        System.out.print(new Date().toString() + "\n");
    }
}