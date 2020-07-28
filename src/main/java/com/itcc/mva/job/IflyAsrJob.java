package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.entity.QuarkCallbackEntity;
import com.itcc.mva.service.IQuarkCallbackService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author whoami
 * 请求离线解析任务
 */
@Component
public class IflyAsrJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${jobtype.kdjob}")
    private int kdjob;

    @Autowired
    private IQuarkCallbackService iQuarkCallbackService;

    @Scheduled(cron = "0/5 * * * * ?")
    @SchedulerLock(name = "IflyBaseTableJob", lockAtMostFor = "3s", lockAtLeastFor = "3s")
    public void generateBaseTable() {
        if (Constant.JOB_KD == kdjob) {
            long startGenerateBaseTable = System.currentTimeMillis();
            iQuarkCallbackService.generateIflyBaseTable();
            long endGenerateBaseTable = System.currentTimeMillis() - startGenerateBaseTable;
            logger.info(">>> 任务名称:IflyBaseTableJob(科大生成科大基表) 总执行时间为: [" + endGenerateBaseTable + " ms]");
        }else{
            logger.info(">>> 任务名称:IflyBaseTableJob(科大生成科大基表) 停止工作");
        }
    }

    /**
     * 2
     */
    @Scheduled(cron = "0/5 * * * * ?")
    @SchedulerLock(name = "IflyPushToRmaWebJob", lockAtMostFor = "3s", lockAtLeastFor = "3s")
    public void pushToRmaIflyWeb() {
        long startPushToRmaIflyWeb = System.currentTimeMillis();
        /**
         * 先检查未转码过的ifly列表
         */
        List<QuarkCallbackEntity> rmaEntityList = iQuarkCallbackService.pushToIflyAudioTop(Constant.NO_RMA_IFLY);

        if (0 != rmaEntityList.size()) {
            logger.info(">>> 存在[IFLY录音转码]任务 。 开始时间 [" + new Date() + "]");
            for (int i = 0; i < rmaEntityList.size(); i++) {
                iQuarkCallbackService.addRmaIflyTask(rmaEntityList.get(i));
            }
            logger.info(">>> 存在[IFLY录音转码]任务 。 结束时间 [" + new Date() + "]");
        } else {
            logger.info(">>> 任务名称:IflyPushToRmaWebJob 暂时没有[IFLY录音转码]任务。");
        }
        long endPushToRmaIflyWeb = System.currentTimeMillis() - startPushToRmaIflyWeb;
        logger.info(">>> 任务名称:IflyPushToRmaWebJob(科大离线转码) 总执行时间为: [" + endPushToRmaIflyWeb + " ms]");
    }

    /**
     * 3
     */
    @Scheduled(cron = "0/5 * * * * ?")
    @SchedulerLock(name = "IflyPushToAudioJob", lockAtMostFor = "3s", lockAtLeastFor = "3s")
    public void pushToIflyAudio() {
        long startPushToIflyAudio = System.currentTimeMillis();
        /**
         * 先检查未解析过的ifly列表
         */
        List<QuarkCallbackEntity> asrEntityList = iQuarkCallbackService.queryIflyPendingTop(Constant.NO_PARSER_IFLY);

        if (0 != asrEntityList.size()) {
            logger.info(">>> 存在[IFLY解析]任务 。 开始时间 [" + new Date() + "]");
            for (int i = 0; i < asrEntityList.size(); i++) {
                iQuarkCallbackService.addIflyTask(asrEntityList.get(i));
            }
            logger.info(">>> 存在[IFLY解析]任务 。 结束时间 [" + new Date() + "]");
        } else {
            logger.info(">>> 任务名称:IflyPushToAudioJob 暂时没有[IFLY解析]任务。");
        }
        long endPushToIflyAudio = System.currentTimeMillis() - startPushToIflyAudio;
        logger.info(">>> 任务名称:IflyPushToAudioJob(科大离线解析) 总执行时间为: [" + endPushToIflyAudio + " ms]");

    }

    /**
     * 新版接口，需要停掉2和3
     */
    //@Scheduled(cron = "0/5 * * * * ?")
   //@SchedulerLock(name = "ISTIflyPushToAudioJob", lockAtMostFor = "3s", lockAtLeastFor = "3s")
    public void pushToISTIflyAudio() {
        long startPushToIflyAudio = System.currentTimeMillis();
        /**
         * 先检查未解析过的ifly列表
         */
        List<QuarkCallbackEntity> asrEntityList = iQuarkCallbackService.queryIstIflyPendingTop(Constant.NO_PARSER_IFLY);

        if (0 != asrEntityList.size()) {
            logger.info(">>> 存在新接口[IFLY解析]任务 。 开始时间 [" + new Date() + "]");
            for (int i = 0; i < asrEntityList.size(); i++) {
                iQuarkCallbackService.addistIflyTask(asrEntityList.get(i));
            }
            logger.info(">>> 存在新接口[IFLY解析]任务 。 结束时间 [" + new Date() + "]");
        } else {
            logger.info(">>> 任务名称:ISTIflyPushToAudioJob 暂时没有新接口[IFLY解析]任务。");
        }
        long endPushToIflyAudio = System.currentTimeMillis() - startPushToIflyAudio;
        logger.info(">>> 任务名称:ISTIflyPushToAudioJob(新接口科大离线解析) 总执行时间为: [" + endPushToIflyAudio + " ms]");

    }

}
