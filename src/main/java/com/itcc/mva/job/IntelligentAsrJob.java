package com.itcc.mva.job;

import com.itcc.mva.service.IIntelligentTransferService;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ASR解析
 * @author LCL
 */
@Component
public class IntelligentAsrJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IIntelligentTransferService intelligentTransferService;

    /**
     * 生产录音解析基表
     * @Author LCL
     */

    @Scheduled(cron = "* 0/2 * * * ?")
    @SchedulerLock(name = "IntelligentBaseJob", lockAtMostFor = "1m", lockAtLeastFor ="1m")
    public void generateBaseTable() {
        long start_IntelligentBaseJob=System.currentTimeMillis();
        intelligentTransferService.generateBaseTable();
        //logger.info("IntelligentAsrJob-------------generateBaseTable调用了***************************");
        long end_IntelligentBaseJob=System.currentTimeMillis()-start_IntelligentBaseJob;
        logger.info(">>> 任务名称:IntelligentBaseJob(捷通基本生成) 总执行时间为: ["+ end_IntelligentBaseJob+"]");
    }

    /**
     * 录音解析
     * @Author LCL
     */

    @Scheduled(cron = "* 0/2 * * * ?")
    @SchedulerLock(name = "IntelligentAsrJob", lockAtMostFor = "1m", lockAtLeastFor ="1m")
    public void asr() {
        long start_IntelligentAsrJob=System.currentTimeMillis();
        intelligentTransferService.asr();
        //log.info("IntelligentAsrJob-------------asr调用了***************************");
        long end_IntelligentAsrJob=System.currentTimeMillis()-start_IntelligentAsrJob;
        logger.info(">>> 任务名称:IntelligentAsrJob(捷通离线解析) 总执行时间为: ["+ end_IntelligentAsrJob+"]");
    }
}
