package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.service.IPushToMvaService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PushToMvaService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IPushToMvaService iPushToMvaService;

    @Scheduled(cron = "0/2 * * * * ?")
    @SchedulerLock(name = "PushToMvaJob", lockAtMostFor = Constant.lockAtMostForTime, lockAtLeastFor = Constant.lockAtLeastForTime)
    public void pushInfo()
    {
        iPushToMvaService.sendToMvaService();
    }
}
