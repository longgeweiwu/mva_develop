package com.itcc.mva.job;

import com.itcc.mva.common.utils.Constant;
import com.itcc.mva.entity.IntelligentAsrEntity;
import com.itcc.mva.service.IAsrJsonParseService;
import com.itcc.mva.service.IPushToMvaService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class PushToMvaService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IAsrJsonParseService iAsrJsonParseService;

    @Autowired
    private IPushToMvaService iPushToMvaService;

    @Scheduled(cron = "* 0/2 * * * ?")
    @SchedulerLock(name = "PushToMvaJob", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    public void pushInfo()
    {
        List<IntelligentAsrEntity> waitingSend = iAsrJsonParseService.queryWaitingSend(Constant.NO_PARSER);
        if(0 != waitingSend.size()) {
            logger.info(">>> 存在[推送]任务 。 开始时间 ["+new Date()+"]");
            for (int i = 0; i < waitingSend.size(); i++) {
                iPushToMvaService.singleSendToMvaService(waitingSend.get(i));
            }
            logger.info(">>> 存在[推送]任务 。 结束时间 ["+new Date()+"]");
        }else{
            logger.info(">>> 任务名称:PushToMvaJob 暂时没有[推送]任务。");
        }
    }
}
