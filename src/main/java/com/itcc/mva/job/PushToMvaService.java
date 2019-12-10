package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PushToMvaService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0/2 * * * * ?")
    @SchedulerLock(name = "PushToMvaJob", lockAtMostFor = Constant.lockAtMostForTime, lockAtLeastFor = Constant.lockAtLeastForTime)
    public void pushInfo()
    {

    }
}
