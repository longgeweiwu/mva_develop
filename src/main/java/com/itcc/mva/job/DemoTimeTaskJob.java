package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DemoTimeTaskJob {

    @Scheduled(cron = "0/2 * * * * ?")
    @SchedulerLock(name = "TaskScheduler_scheduledTask", lockAtMostFor = Constant.lockAtMostForTime, lockAtLeastFor = Constant.lockAtLeastForTime)
    public void run()
    {
        System.out.print(new Date().toString() + "\n");
    }
}