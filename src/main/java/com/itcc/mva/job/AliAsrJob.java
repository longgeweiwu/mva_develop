package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.entity.AliAsrEntity;
import com.itcc.mva.service.IAliTransferService;
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
public class AliAsrJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${jobtype.aljob}")
    private int aljob;

    @Autowired
    private IAliTransferService iAliTransferService;

    @Scheduled(cron = "* 0/1 * * * ?")
    @SchedulerLock(name = "AliBaseTableJob", lockAtMostFor = "40s", lockAtLeastFor = "40s")
    public void generateBaseTable() {
        if (Constant.JOB_AL == aljob) {
            long startGenerateBaseTable = System.currentTimeMillis();
            iAliTransferService.generateAliBaseTable();
            long endGenerateBaseTable = System.currentTimeMillis() - startGenerateBaseTable;
            logger.info(">>> 任务名称:AliBaseTableJob(阿里生成科大基表) 总执行时间为: [" + endGenerateBaseTable + " ms]");
        }else{
            logger.info(">>> 任务名称:AliBaseTableJob(阿里生成科大基表) 停止工作");
        }
    }


    @Scheduled(cron = "* 0/2 * * * ?")
    @SchedulerLock(name = "AliPushToAudioJob", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    public void pushToAliAudio() {
        long startPushToAliAudio = System.currentTimeMillis();
        /**
         * 先检查未解析过的ALI列表
         */
        List<AliAsrEntity> asrEntityList = iAliTransferService.queryAliPendingTop(Constant.NO_PARSER_ALI);

        if (0 != asrEntityList.size()) {
            logger.info(">>> 存在[ALI解析]任务 。 开始时间 [" + new Date() + "]");
            for (int i = 0; i < asrEntityList.size(); i++) {
                iAliTransferService.addAliTask(asrEntityList.get(i));
            }
            logger.info(">>> 存在[ALI解析]任务 。 结束时间 [" + new Date() + "]");
        } else {
            logger.info(">>> 任务名称:PushToAliAudioJob 暂时没有[ALI解析]任务。");
        }
        long endPushToAliAudio = System.currentTimeMillis() - startPushToAliAudio;
        logger.info(">>> 任务名称:PushToAliAudioJob(阿里离线解析) 总执行时间为: [" + endPushToAliAudio + " ms]");

    }

    @Scheduled(cron = "* 0/2 * * * ?")
    @SchedulerLock(name = "AliQueryAndSetAudio", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    public void queryAliAudio() {
        long startQueryAliAudio = System.currentTimeMillis();
        /**
         * 查询阿里解析结果列表
         */
        List<AliAsrEntity> asrEntityList = iAliTransferService.queryAliResultTop(Constant.NO_PARSER_ALI);

        if (0 != asrEntityList.size()) {
            logger.info(">>> 存在[ALI查询并更新基表]任务 。 开始时间 [" + new Date() + "]");
            for (int i = 0; i < asrEntityList.size(); i++) {
                iAliTransferService.queryAndSetAliBase(asrEntityList.get(i));
            }
            logger.info(">>> 存在[ALI查询并更新基表]任务 。 结束时间 [" + new Date() + "]");
        } else {
            logger.info(">>> 任务名称:queryAndSetAliAudio 暂时没有[ALI查询并更新基表]任务。");
        }
        long endQueryAliAudio = System.currentTimeMillis() - startQueryAliAudio;
        logger.info(">>> 任务名称:queryAndSetAliAudio(阿里查询并更新基表离线解析) 总执行时间为: [" + endQueryAliAudio + " ms]");
    }

}
