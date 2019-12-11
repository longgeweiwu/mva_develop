package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DemoTimeTaskJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //lockAtLeastFor：保证在设置的期间类不执行多次任务，单位是毫秒，此处可以根据实际任务运行情况进行设置，
    //简单来说，一个每15分钟执行的任务，若每次任务执行的时间为几分钟，则可以设置lockAtLeastFor大于其最大估计最大执行时间
    //避免一次任务未执行完，下一个定时任务又启动了。
    //任务执行完，会自动释放锁。
//    @Scheduled(cron = "0/2 * * * * ?")
//    @SchedulerLock(name = "TaskScheduler_scheduledTask", lockAtMostFor = Constant.lockAtMostForTime, lockAtLeastFor = Constant.lockAtLeastForTime)
    public void run()
    {
        logger.info(new Date().toString());
        // System.out.print(new Date().toString() + "\n");
    }
}