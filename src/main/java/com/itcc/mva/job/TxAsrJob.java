package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.entity.TxAsrEntity;
import com.itcc.mva.service.ITxService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TxAsrJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${jobtype.txjob}")
    private int txjob;

    @Autowired
    private ITxService iTxService;

    @Scheduled(cron = "0/5 * * * * ?")
    @SchedulerLock(name = "TxBaseTableJob", lockAtMostFor = "3s", lockAtLeastFor = "3s")
    public void generateBaseTable() {
        if (Constant.JOB_TX == txjob) {
            long startGenerateBaseTable = System.currentTimeMillis();
            iTxService.generateTxBaseTable();
            long endGenerateBaseTable = System.currentTimeMillis() - startGenerateBaseTable;
            logger.info(">>> 任务名称:TxBaseTableJob(腾讯生成腾讯基表) 总执行时间为: [" + endGenerateBaseTable + " ms]");
        }else{
            logger.info(">>> 任务名称:TxBaseTableJob(腾讯生成腾讯基表) 停止工作");
        }
    }

    @Scheduled(cron = "0/15 * * * * ?")
    @SchedulerLock(name = "syncFileToServiceJob", lockAtMostFor = "13s", lockAtLeastFor = "13s")
    public void syncFileToSever() {
        long startsyncFileTx = System.currentTimeMillis();
        /**
         * 查询阿里解析结果列表
         */
        List<TxAsrEntity> asrEntityList = iTxService.queryTxFileTop(Constant.NO_FILE_TX);

        if (0 != asrEntityList.size()) {
            logger.info(">>> 存在[TX同步录音]任务 。 开始时间 [" + new Date() + "]");
            for (int i = 0; i < asrEntityList.size(); i++) {
                iTxService.uploadTxFile(asrEntityList.get(i));
            }
            logger.info(">>> 存在[TX同步录音]任务 。 结束时间 [" + new Date() + "]");
        } else {
            logger.info(">>> 任务名称:syncFileToServiceJob 暂时没有[TX同步录音]任务。");
        }
        long endSyncFileTx = System.currentTimeMillis() - startsyncFileTx;
        logger.info(">>> 任务名称:syncFileToServiceJob(TX同步录音) 总执行时间为: [" + endSyncFileTx + " ms]");
    }

    @Scheduled(cron = "0/5 * * * * ?")
    @SchedulerLock(name = "TxPushToAudioJob", lockAtMostFor = "3s", lockAtLeastFor = "3s")
    public void pushToTxAudio() {
        long startPushToTxAudio = System.currentTimeMillis();
        /**
         * 先检查未解析过的Tx列表
         */
        List<TxAsrEntity> asrEntityList = iTxService.queryTxPendingTop(Constant.NO_PARSER_TX);

        if (0 != asrEntityList.size()) {
            logger.info(">>> 存在[TX解析]任务 。 开始时间 [" + new Date() + "]");
            for (int i = 0; i < asrEntityList.size(); i++) {
                iTxService.addTxTask(asrEntityList.get(i));
            }
            logger.info(">>> 存在[TX解析]任务 。 结束时间 [" + new Date() + "]");
        } else {
            logger.info(">>> 任务名称:TxPushToAudioJob 暂时没有[TX解析]任务。");
        }
        long endPushToTxAudio = System.currentTimeMillis() - startPushToTxAudio;
        logger.info(">>> 任务名称:TxPushToAudioJob(腾讯离线解析) 总执行时间为: [" + endPushToTxAudio + " ms]");

    }

    @Scheduled(cron = "0/5 * * * * ?")
    @SchedulerLock(name = "TxQueryAndSetAudio", lockAtMostFor = "3s", lockAtLeastFor = "3s")
    public void queryTxAudio() {
        long startQueryTxAudio = System.currentTimeMillis();
        /**
         * 查询腾讯解析结果列表
         */
        List<TxAsrEntity> asrEntityList = iTxService.queryTxResultTop(Constant.NO_PARSER_TX);

        if (0 != asrEntityList.size()) {
            logger.info(">>> 存在[Tx查询并更新基表]任务 。 开始时间 [" + new Date() + "]");
            for (int i = 0; i < asrEntityList.size(); i++) {
                iTxService.queryAndSetTxBase(asrEntityList.get(i));
            }
            logger.info(">>> 存在[Tx查询并更新基表]任务 。 结束时间 [" + new Date() + "]");
        } else {
            logger.info(">>> 任务名称:queryAndSetTxAudio 暂时没有[Tx查询并更新基表]任务。");
        }
        long endQueryTxAudio = System.currentTimeMillis() - startQueryTxAudio;
        logger.info(">>> 任务名称:queryAndSetTxAudio(腾讯查询并更新基表离线解析) 总执行时间为: [" + endQueryTxAudio + " ms]");
    }
}
