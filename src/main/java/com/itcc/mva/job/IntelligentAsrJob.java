package com.itcc.mva.job;

import com.itcc.mva.service.IIntelligentTransferService;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ASR解析
 * @author LCL
 */
@Component
@Slf4j
public class IntelligentAsrJob {

    @Autowired
    private IIntelligentTransferService intelligentTransferService;

    /**
     * 生产录音解析基表
     * @Author LCL
     */

    @Scheduled(cron = "* 0/2 * * * ?")
    @SchedulerLock(name = "IntelligentAsrJob", lockAtMostFor = "1m", lockAtLeastFor ="1m")
    public void generateBaseTable() {
        intelligentTransferService.generateBaseTable();
        log.info("IntelligentAsrJob-------------generateBaseTable调用了***************************");
    }

    /**
     * 录音解析
     * @Author LCL
     */

    @Scheduled(cron = "* 0/2 * * * ?")
    @SchedulerLock(name = "IntelligentAsrJob", lockAtMostFor = "1m", lockAtLeastFor ="1m")
    public void asr() {
        intelligentTransferService.asr();
        log.info("IntelligentAsrJob-------------asr调用了***************************");
    }
}
