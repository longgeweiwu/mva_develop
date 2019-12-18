package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.entity.QuarkCallbackEntity;
import com.itcc.mva.service.IQuarkCallbackService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IQuarkCallbackService iQuarkCallbackService;

    @Scheduled(cron = "* 0/2 * * * ?")
    @SchedulerLock(name = "IflyBaseTableJob", lockAtMostFor = "1m", lockAtLeastFor ="1m")
    public void  generateBaseTable() {
        long start_generateBaseTable=System.currentTimeMillis();
        iQuarkCallbackService.generateIflyBaseTable();
        long end_generateBaseTable=System.currentTimeMillis()-start_generateBaseTable;
        logger.info(">>> 任务名称:IflyBaseTableJob 总执行时间为: ["+ end_generateBaseTable+"]");
    }

    @Scheduled(cron = "* 0/2 * * * ?")
    @SchedulerLock(name = "pushToRmaIflyWebJob", lockAtMostFor = "1m", lockAtLeastFor ="1m")
    public void  pushToRmaIflyWeb() {
        long start_pushToRmaIflyWeb=System.currentTimeMillis();
        /**
         * 先检查未转码过的ifly列表
         */
        List<QuarkCallbackEntity> rmaEntityList = iQuarkCallbackService.pushToIflyAudioTop(Constant.NO_RMA_IFLY);

        if(0 != rmaEntityList.size()){
            logger.info(">>> 存在[IFLY录音转码]任务 。 开始时间 ["+new Date()+"]");
            for (int i = 0; i < rmaEntityList.size(); i++) {
                iQuarkCallbackService.addRmaIflyTask(rmaEntityList.get(i));
            }
            logger.info(">>> 存在[IFLY录音转码]任务 。 结束时间 ["+new Date()+"]");
        }else{
            logger.info(">>> 任务名称:pushToRmaIflyWebJob 暂时没有[IFLY录音转码]任务。");
        }
        long end_pushToRmaIflyWeb=System.currentTimeMillis()-start_pushToRmaIflyWeb;
        logger.info(">>> 任务名称:pushToRmaIflyWebJob 总执行时间为: ["+ end_pushToRmaIflyWeb+"]");
    }

    @Scheduled(cron = "* 0/2 * * * ?")
    @SchedulerLock(name = "PushToIflyAudioJob", lockAtMostFor = "1m", lockAtLeastFor ="1m")
    public void pushToIflyAudio() {
        long start_pushToIflyAudio=System.currentTimeMillis();
        /**
         * 先检查未解析过的ifly列表
         */
        List<QuarkCallbackEntity> asrEntityList = iQuarkCallbackService.queryIflyPendingTop(Constant.NO_PARSER_IFLY);

        if(0 != asrEntityList.size()){
            logger.info(">>> 存在[IFLY解析]任务 。 开始时间 ["+new Date()+"]");
            for (int i = 0; i < asrEntityList.size(); i++) {
                iQuarkCallbackService.addIflyTask(asrEntityList.get(i));
            }
            logger.info(">>> 存在[IFLY解析]任务 。 结束时间 ["+new Date()+"]");
        }else{
            logger.info(">>> 任务名称:AsrJsonParseJob 暂时没有[IFLY解析]任务。");
        }
        long end_pushToIflyAudio=System.currentTimeMillis()-start_pushToIflyAudio;
        logger.info(">>> 任务名称:PushToIflyAudioJob 总执行时间为: ["+ end_pushToIflyAudio+"]");

    }

}
