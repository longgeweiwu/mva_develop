package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.service.IIntelligentTransferService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ASR解析
 *
 * @author LCL
 */
@Component
public class IntelligentAsrJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${jobtype.jtjob}")
    private int jtjob;

    @Autowired
    private IIntelligentTransferService intelligentTransferService;

    /**
     * 生产录音解析基表
     *
     * @Author LCL
     */

    @Scheduled(cron = "* 0/1 * * * ?")
    @SchedulerLock(name = "JtBaseJob", lockAtMostFor = "40s", lockAtLeastFor = "40s")
    public void generateBaseTable() {
        if (Constant.JOB_JT == jtjob) {
            long startIntelligentBaseJob = System.currentTimeMillis();
            intelligentTransferService.generateBaseTable();
            long endIntelligentBaseJob = System.currentTimeMillis() - startIntelligentBaseJob;
            logger.info(">>> 任务名称:JtBaseJob(捷通基本生成) 总执行时间为: [" + endIntelligentBaseJob + " ms]");
        }else{
            logger.info(">>> 任务名称:JtBaseJob(捷通基本生成) 停止工作");
        }
    }

    /**
     * 录音解析
     *
     * @Author LCL
     */

    @Scheduled(cron = "* 0/2 * * * ?")
    @SchedulerLock(name = "JtAsrJob", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    public void asr() {
        long startIntelligentAsrJob = System.currentTimeMillis();
        intelligentTransferService.asr();
        long endIntelligentAsrJob = System.currentTimeMillis() - startIntelligentAsrJob;
        logger.info(">>> 任务名称:JtAsrJob(捷通离线解析) 总执行时间为: [" + endIntelligentAsrJob + " ms]");
    }
}
