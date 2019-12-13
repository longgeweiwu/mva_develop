package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.common.utils.Tools;
import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.service.IQuarkCallbackService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class IflyAsrJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IQuarkCallbackService iQuarkCallbackService;

    //@Scheduled(cron = "* 0/2 * * * ?")
    //@SchedulerLock(name = "PushToIflyAudioJob", lockAtMostFor = "1m", lockAtLeastFor ="1m")
    public void pushToIflyAudio() {
        /**
         * 先检查未解析过的ifly列表
         */
        List<IntelligentAsrEntity> asrEntityList = iQuarkCallbackService.queryIflyPendingTop(Constant.NO_PARSER_IFLY);

        if(0 != asrEntityList.size()){
            logger.info(">>> 存在[IFLY解析]任务 。 开始时间 ["+new Date()+"]");
            for (int i = 0; i < asrEntityList.size(); i++) {
                iQuarkCallbackService.addIflyTask(asrEntityList.get(i));
            }
            logger.info(">>> 存在[IFLY解析]任务 。 结束时间 ["+new Date()+"]");
        }else{
            logger.info(">>> 任务名称:AsrJsonParseJob 暂时没有[IFLY解析]任务。");
        }

    }

}
